package app.seller;

import app.seller.util.TwoValuesHashMap;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Locale;

public class SellerAgent extends Agent {
    private TwoValuesHashMap<String, Float, Float> books;

    @Override
    protected void setup() {
        this.books = new TwoValuesHashMap<String, Float, Float>();
        SellerGui.start().setAgent(this).setVisible(true);

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("seller");
            serviceDescription.setName("BookSeller");

            agentDescription.setName(this.getAID());
            agentDescription.addServices(serviceDescription);
            DFService.register(this, agentDescription);
        } catch (FIPAException ignored) { }

        this.addBehaviour(new UpdatePriceBehaviour());
        this.addBehaviour(new InformCurrentBooksBehaviour());
    }

    public void addBook(String title, float initialPrice, float increment) {
        this.books.put(title, initialPrice, increment);
        this.addBehaviour(new NotifyNewBookBehaviour(title, initialPrice));
    }

    private class UpdatePriceBehaviour extends TickerBehaviour {

        public UpdatePriceBehaviour() {
            super(SellerAgent.this, 10000);
        }

        protected void onTick() {
            for(String key : SellerAgent.this.books.keySet()) {
                float oldPrice = SellerAgent.this.books.getFirstValue(key);
                float step = SellerAgent.this.books.getSecondValue(key);
                float newPrice = oldPrice + step;

                SellerAgent.this.books.put(key, newPrice, step);

                ACLMessage message = new ACLMessage(ACLMessage.CFP);
                message.setLanguage("English");
                message.setOntology("EnglishAuctionOntology");
                message.setContent(String.format(Locale.US, "%s||%f", key, newPrice));

                this.addReceiversToMessage(message);
                SellerAgent.this.send(message);
            }
        }

        private void addReceiversToMessage(ACLMessage message) {
            try {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();

                serviceDescription.setType("buyer");
                template.addServices(serviceDescription);

                for(DFAgentDescription agent : DFService.search(SellerAgent.this, template)) {
                    message.addReceiver(agent.getName());
                }
            } catch (FIPAException ignored) { }
        }
    }

    private class InformCurrentBooksBehaviour extends CyclicBehaviour {
        public void action() {
            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage message = SellerAgent.this.receive(messageTemplate);
            StringBuilder stringBuilder = new StringBuilder();

            if (message != null) {
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.INFORM);

                for(String book : SellerAgent.this.books.keySet()) {
                    float price = SellerAgent.this.books.getFirstValue(book);
                    stringBuilder.append(book).append("||").append(price).append("|||");
                }

                reply.setContent(stringBuilder.toString());
                SellerAgent.this.send(reply);
            } else {
                this.block();
            }
        }
    }

    private class NotifyNewBookBehaviour extends OneShotBehaviour {
        private String book;
        private float currentPrice;

        public NotifyNewBookBehaviour(String book, float currentPrice) {
            this.book = book;
            this.currentPrice = currentPrice;
        }

        public void action() {
            ACLMessage message = new ACLMessage(ACLMessage.INFORM);
            message.setLanguage("English");
            message.setOntology("EnglishAuctionOntology");
            message.setContent(String.format(Locale.US, "%s||%f", this.book, this.currentPrice));

            this.addReceiverToMessage(message);
            SellerAgent.this.send(message);
        }

        private void addReceiverToMessage(ACLMessage message) {
            try {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();

                serviceDescription.setType("buyer");
                serviceDescription.setName("BookBuyer");
                template.addServices(serviceDescription);

                for(DFAgentDescription agent : DFService.search(SellerAgent.this, template)) {
                    message.addReceiver(agent.getName());
                }
            } catch (FIPAException ignored) { }
        }
    }
}