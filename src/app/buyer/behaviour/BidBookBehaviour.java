package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class BidBookBehaviour extends OneShotBehaviour {
    private BuyerAgent buyerAgent;
    private String book;

    public BidBookBehaviour(BuyerAgent buyerAgent, String book) {
        this.buyerAgent = buyerAgent;
        this.book = book;
    }

    public void action() {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();
            agentDescription.setName(this.buyerAgent.getAID());

            DFAgentDescription completeAgentDescription  = DFService.search(this.buyerAgent, agentDescription)[0];

            serviceDescription.setType("book " + this.book);
            serviceDescription.setName("book " + this.book);

            completeAgentDescription.addServices(serviceDescription);
            DFService.modify(this.buyerAgent, completeAgentDescription);
        } catch (FIPAException ignored) { }
    }
}