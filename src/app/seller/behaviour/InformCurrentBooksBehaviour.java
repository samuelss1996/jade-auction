package app.seller.behaviour;

import app.seller.SellerAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InformCurrentBooksBehaviour extends CyclicBehaviour {
    private SellerAgent sellerAgent;

    public InformCurrentBooksBehaviour(SellerAgent sellerAgent) {
        this.sellerAgent = sellerAgent;
    }

    public void action() {
        MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage message = this.sellerAgent.receive(messageTemplate);
        StringBuilder stringBuilder = new StringBuilder();

        if (message != null && !this.sellerAgent.getBooks().isEmpty()) {
            ACLMessage reply = message.createReply();
            reply.setPerformative(ACLMessage.INFORM);

            for(String book : this.sellerAgent.getBooks().keySet()) {
                stringBuilder.append(book).append("||");
            }

            reply.setContent(stringBuilder.toString());
            this.sellerAgent.send(reply);
        } else {
            this.block();
        }
    }
}
