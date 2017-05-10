package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WinBookBehaviour extends CyclicBehaviour {
    private BuyerAgent buyerAgent;

    public WinBookBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage message = this.buyerAgent.receive(messageTemplate);

        if(message != null) {
            String[] fields = message.getContent().split("\\|\\|");
            String book = fields[0];
            float price = Float.valueOf(fields[1]);

            this.buyerAgent.deregisterBook(book);

            if(this.buyerAgent.getBooks().get(book) >= price) {
                this.buyerAgent.getGui().bookWon(book);
            } else {
                this.buyerAgent.getGui().bookPriceExceeded(book);
            }

            this.buyerAgent.getBooks().remove(book);
        } else {
            this.block();
        }
    }
}