package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class UpdateOffersBehaviour extends CyclicBehaviour {
    private BuyerAgent buyerAgent;

    public UpdateOffersBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage message = this.buyerAgent.receive(messageTemplate);

        if(message != null) {
            String[] fields = message.getContent().split("\\|\\|");
            String book = fields[0];
            float proposedPrice = Float.valueOf(fields[1]);

            if(this.buyerAgent.getBooks().get(book) != null && this.buyerAgent.getBooks().get(book) >= proposedPrice) {
                ACLMessage reply = message.createReply();
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContent(book);

                this.buyerAgent.getGui().updatePrice(book, proposedPrice);
                this.buyerAgent.send(reply);
            } else {
                this.buyerAgent.deregisterBook(book);
                this.buyerAgent.getGui().bookPriceExceeded(book);
            }
        } else {
            this.block();
        }
    }
}