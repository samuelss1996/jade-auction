package app.buyer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;

public class BuyerAgent extends Agent {
    private HashMap<String, Float> books;
    private BuyerGui gui;

    @Override
    protected void setup() {
        this.books = new HashMap<String, Float>();
        this.books.put("Soy un libro", 25.5f);

        this.gui = BuyerGui.start();
        this.gui.setAgent(this).setVisible(true);

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("buyer");
            serviceDescription.setName("BookBuyer");

            agentDescription.setName(this.getAID());
            agentDescription.addServices(serviceDescription);
            DFService.register(this, agentDescription);
        } catch (FIPAException ignored) { }

        this.addBehaviour(new RequestCurrentBooksBehaviour());
        this.addBehaviour(new CheckOffersBehaviour());
        this.addBehaviour(new GetNotifiedForNewBooks());
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
                String[] fields = message.getContent().split("\\|\\|");
                String book = fields[0];
                float proposedPrice = Float.valueOf(fields[1]);

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

    private class GetNotifiedForNewBooks extends CyclicBehaviour {
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage message = BuyerAgent.this.receive(messageTemplate);

            if(message != null) {
                for(String line : message.getContent().split("\\|\\|\\|")) {
                    String[] fields = line.split("\\|\\|");
                    String book = fields[0];
                    float price = Float.valueOf(fields[1]);

                    BuyerAgent.this.gui.addBook(book, price);
                }
            } else {
                this.block();
            }
        }
    }

    private class RequestCurrentBooksBehaviour extends OneShotBehaviour {
        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.setLanguage("English");
            message.setOntology("EnglishAuctionOntology");

            this.addReceiversToMessage(message);
            BuyerAgent.this.send(message);
        }

        private void addReceiversToMessage(ACLMessage message) {
            try {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();

                serviceDescription.setType("seller");
                template.addServices(serviceDescription);

                for(DFAgentDescription agent : DFService.search(BuyerAgent.this, template)) {
                    message.addReceiver(agent.getName());
                }
            } catch (FIPAException ignored) { }
        }
    }
}