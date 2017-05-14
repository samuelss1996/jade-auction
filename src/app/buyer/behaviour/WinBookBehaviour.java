package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import app.ontology.WinOffer;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WinBookBehaviour extends CyclicBehaviour {
    private BuyerAgent buyerAgent;

    public WinBookBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                MessageTemplate.and(MessageTemplate.MatchOntology(this.buyerAgent.getOntology().getName()),
                        MessageTemplate.MatchLanguage(this.buyerAgent.getCodec().getName())));
        ACLMessage message = this.buyerAgent.receive(messageTemplate);

        if(message != null) {
            try {
                Action action = (Action) this.buyerAgent.getContentManager().extractContent(message);
                WinOffer winOffer = (WinOffer) action.getAction();

                String bookTitle = winOffer.getWonOffer().getBook().getTitle();
                float price = winOffer.getWonOffer().getPrice();

                this.buyerAgent.deregisterBook(bookTitle);

                if(this.buyerAgent.getBooks().get(bookTitle) >= price) {
                    this.buyerAgent.getGui().bookWon(bookTitle, price);
                } else {
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