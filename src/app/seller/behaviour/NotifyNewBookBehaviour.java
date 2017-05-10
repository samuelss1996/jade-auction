package app.seller.behaviour;

import app.seller.SellerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Locale;

public class NotifyNewBookBehaviour extends OneShotBehaviour {
    private SellerAgent sellerAgent;
    private String book;

    public NotifyNewBookBehaviour(SellerAgent sellerAgent, String book) {
        this.sellerAgent = sellerAgent;
        this.book = book;
    }

    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        message.setLanguage("English");
        message.setOntology("EnglishAuctionOntology");
        message.setContent(String.format(Locale.US, "%s", this.book));

        this.addReceiverToMessage(message);
        this.sellerAgent.send(message);
    }

    private void addReceiverToMessage(ACLMessage message) {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("buyer");
            serviceDescription.setName("BookBuyer");
            template.addServices(serviceDescription);

            for(DFAgentDescription agent : DFService.search(this.sellerAgent, template)) {
                message.addReceiver(agent.getName());
            }
        } catch (FIPAException ignored) { }
    }
}