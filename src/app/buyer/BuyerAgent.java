package app.buyer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;

public class BuyerAgent extends Agent {
    private HashMap<String, Float> books;

    @Override
    protected void setup() {
        this.books = new HashMap<String, Float>();
        this.books.put("Soy un libro", 25.5f);

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("buyer");
            serviceDescription.setName("BookBuyer");

            agentDescription.setName(this.getAID());
            agentDescription.addServices(serviceDescription);
            DFService.register(this, agentDescription);

            this.addBehaviour(new CheckOffersBehaviour());
        } catch (FIPAException ignored) { }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException ignored) {}
    }

    private class CheckOffersBehaviour extends CyclicBehaviour {
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage message = BuyerAgent.this.receive(messageTemplate);

            if(message != null) {
                String[] fields = message.getContent().split("||");
                String book = fields[0];
                float proposedPrice = Float.valueOf(fields[0]);

                if(BuyerAgent.this.books.get(book) != null && BuyerAgent.this.books.get(book) >= proposedPrice) {
                    ACLMessage reply = message.createReply();
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(book);

                    BuyerAgent.this.send(reply);
                }
            } else {
                this.block();
            }
        }
    }
}