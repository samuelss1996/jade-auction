package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class RequestCurrentBooksBehaviour extends OneShotBehaviour {
    private BuyerAgent buyerAgent;

    public RequestCurrentBooksBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.setLanguage("English");
        message.setOntology("EnglishAuctionOntology");

        this.addReceiversToMessage(message);
        this.buyerAgent.send(message);
    }

    private void addReceiversToMessage(ACLMessage message) {
        try {
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("seller");
            template.addServices(serviceDescription);

            for(DFAgentDescription agent : DFService.search(this.buyerAgent, template)) {
                message.addReceiver(agent.getName());
            }
        } catch (FIPAException ignored) { }
    }
}