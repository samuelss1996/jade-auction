package app.seller.behaviour;

import app.seller.SellerAgent;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Locale;
import java.util.Random;

public class UpdatePriceBehaviour extends TickerBehaviour {
    private String book;
    private SellerAgent sellerAgent;
    private AID potentialWinner;

    private float currentPrice;
    private float step;

    public UpdatePriceBehaviour(SellerAgent sellerAgent, String book) {
        super(sellerAgent, 10000);

        this.book = book;
        this.sellerAgent = sellerAgent;
    }

    protected void onTick() {
        float oldPrice = this.sellerAgent.getBooks().getFirstValue(this.book);
        this.step = this.sellerAgent.getBooks().getSecondValue(this.book);
        this.currentPrice = oldPrice + this.step;

        ACLMessage message = new ACLMessage(ACLMessage.CFP);
        message.setLanguage("English");
        message.setOntology("EnglishAuctionOntology");

        if(this.addReceiversToMessage(message)) {
            message.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            message.setContent(String.format(Locale.US, "%s||%f", this.book, this.currentPrice));

            this.sellerAgent.removeBook(this.book);
            this.sellerAgent.removeBehaviour(this);
        } else {
            message.setContent(String.format(Locale.US, "%s||%f", this.book, this.currentPrice));

            this.sellerAgent.getBooks().put(this.book, this.currentPrice, step);
            this.sellerAgent.getGui().updatePrice(this.book, this.currentPrice);
        }

        this.sellerAgent.send(message);
    }

    private boolean addReceiversToMessage(ACLMessage message) {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("book " + this.book);
            serviceDescription.setName("book " + this.book);
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