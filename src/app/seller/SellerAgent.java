package app.seller;

import app.seller.util.TwoValuesHashMap;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class SellerAgent extends Agent {
    private TwoValuesHashMap<String, Float, Float> books;

    @Override
    protected void setup() {
        this.books = new TwoValuesHashMap<String, Float, Float>();

        this.books.put("Soy un libro", 15.5f, 5.5f);
        this.addBehaviour(new UpdatePriceBehaviour());
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

                ACLMessage message = new ACLMessage(ACLMessage.PROPOSE);
                message.setLanguage("English");
                message.setOntology("EnglishAuctionOntology");
                message.setContent(String.format("%s||%f", key, newPrice));

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

                for(DFAgentDescription agent : DFService.search(myAgent, template)) {
                    message.addReceiver(agent.getName());
                }
            } catch (FIPAException ignored) { }
        }
    }
}