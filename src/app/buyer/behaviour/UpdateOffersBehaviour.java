package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import app.ontology.SendOffer;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UpdateOffersBehaviour extends CyclicBehaviour {
    private BuyerAgent buyerAgent;

    public UpdateOffersBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.and(MessageTemplate.MatchOntology(this.buyerAgent.getOntology().getName()),
                        MessageTemplate.MatchLanguage(this.buyerAgent.getCodec().getName())));
        ACLMessage message = this.buyerAgent.receive(messageTemplate);

        if(message != null) {
            try {
                Action action = (Action) this.buyerAgent.getContentManager().extractContent(message);
                SendOffer sendOffer = (SendOffer) action.getAction();

                String bookTitle = sendOffer.getOffer().getBook().getTitle();
                float proposedPrice = sendOffer.getOffer().getPrice();

                if(this.buyerAgent.getBooks().get(bookTitle) != null && this.buyerAgent.getBooks().get(bookTitle) >= proposedPrice) {
                    this.buyerAgent.getGui().updatePrice(bookTitle, proposedPrice);
                } else {
                    this.buyerAgent.deregisterBook(bookTitle);
                    this.buyerAgent.getGui().bookPriceExceeded(bookTitle);
                }
            } catch (UngroundedException e) {
                e.printStackTrace();
            } catch (OntologyException e) {
                e.printStackTrace();
            } catch (Codec.CodecException e) {
                e.printStackTrace();
            }
        } else {
            this.block();
        }
    }
}