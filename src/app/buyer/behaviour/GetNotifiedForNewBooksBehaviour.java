package app.buyer.behaviour;

import app.buyer.BuyerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GetNotifiedForNewBooksBehaviour extends CyclicBehaviour {
    private BuyerAgent buyerAgent;

    public GetNotifiedForNewBooksBehaviour(BuyerAgent buyerAgent) {
        this.buyerAgent = buyerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage message = this.buyerAgent.receive(messageTemplate);

        if(message != null) {
            for(String book : message.getContent().split("\\|\\|")) {
                this.buyerAgent.getGui().addBook(book);
            }
        } else {
            this.block();
        }
    }
}