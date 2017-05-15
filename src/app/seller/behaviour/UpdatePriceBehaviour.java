package app.seller.behaviour;

import app.ontology.Book;
import app.ontology.Offer;
import app.ontology.SendOffer;
import app.ontology.WinOffer;
import app.ontology.impl.DefaultBook;
import app.ontology.impl.DefaultOffer;
import app.ontology.impl.DefaultSendOffer;
import app.ontology.impl.DefaultWinOffer;
import app.seller.SellerAgent;
import jade.content.lang.Codec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class UpdatePriceBehaviour extends TickerBehaviour {
    private String bookTitle;
    private SellerAgent sellerAgent;
    private AID potentialWinner;

    private float currentPrice;
    private float step;
    private boolean firstTime;

    public UpdatePriceBehaviour(SellerAgent sellerAgent, String bookTitle) {
        super(sellerAgent, 10000);

        this.bookTitle = bookTitle;
        this.sellerAgent = sellerAgent;
        this.firstTime = true;
    }

    protected void onTick() {
        float oldPrice = this.sellerAgent.getBooks().getFirstValue(this.bookTitle);
        this.step = this.sellerAgent.getBooks().getSecondValue(this.bookTitle);
        this.currentPrice = oldPrice + this.step;

        try {
            ACLMessage message = new ACLMessage(ACLMessage.CFP);
            message.setLanguage(this.sellerAgent.getCodec().getName());
            message.setOntology(this.sellerAgent.getOntology().getName());

            if(this.addReceiversToMessage(message)) {
                if(!this.firstTime) {
                    this.currentPrice -= this.step;
                }

                WinOffer winOffer = new DefaultWinOffer();
                Offer offer = new DefaultOffer();
                Book book = new DefaultBook();

                book.setTitle(this.bookTitle);
                offer.setBook(book);
                offer.setPrice(this.currentPrice);
                winOffer.setWonOffer(offer);

                message.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                this.sellerAgent.getContentManager().fillContent(message, new Action(this.sellerAgent.getAID(), winOffer));

                this.sellerAgent.removeBook(this.bookTitle);
                this.sellerAgent.removeBehaviour(this);
            } else {
                if(this.firstTime) {
                    this.currentPrice += this.step;
                }

                SendOffer sendOffer = new DefaultSendOffer();
                Offer offer = new DefaultOffer();
                Book book = new DefaultBook();

                book.setTitle(this.bookTitle);
                offer.setBook(book);
                offer.setPrice(this.currentPrice);
                sendOffer.setOffer(offer);

                this.sellerAgent.getContentManager().fillContent(message, new Action(this.sellerAgent.getAID(), sendOffer));

                this.sellerAgent.getBooks().put(this.bookTitle, this.currentPrice, step);
                this.sellerAgent.getGui().updatePrice(this.bookTitle, this.currentPrice);
            }

            this.sellerAgent.send(message);
            this.firstTime = false;
        } catch (Codec.CodecException e) {
            e.printStackTrace();
        } catch (OntologyException e) {
            e.printStackTrace();
        }
    }

    private boolean addReceiversToMessage(ACLMessage message) {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("book " + this.bookTitle);
            serviceDescription.setName("book " + this.bookTitle);
            template.addServices(serviceDescription);

            DFAgentDescription[] matchingAgents = DFService.search(this.sellerAgent, template);

            for(DFAgentDescription agent : matchingAgents) {
                message.addReceiver(agent.getName());
            }

            switch(matchingAgents.length) {
                case 0:
                    this.currentPrice -= this.step;
                    message.addReceiver(this.potentialWinner);

                    return this.potentialWinner != null;
                case 1:
                    this.potentialWinner = matchingAgents[0].getName();
                    return true;
                default:
                    this.potentialWinner = matchingAgents[new Random().nextInt(matchingAgents.length)].getName();
                    return false;
            }
        } catch (FIPAException ignored) { }

        return false;
    }
}