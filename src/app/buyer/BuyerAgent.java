package app.buyer;

import app.buyer.behaviour.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.util.leap.Iterator;

import java.util.HashMap;

public class BuyerAgent extends Agent {
    private HashMap<String, Float> books;
    private BuyerGui gui;

    @Override
    protected void setup() {
        this.books = new HashMap<String, Float>();
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

        this.addBehaviour(new RequestCurrentBooksBehaviour(this));
        this.addBehaviour(new UpdateOffersBehaviour(this));
        this.addBehaviour(new GetNotifiedForNewBooksBehaviour(this));
        this.addBehaviour(new WinBookBehaviour(this));
    }

    public void deregisterBook(String book) {
        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            agentDescription.setName(this.getAID());

            DFAgentDescription completeAgentDescription  = DFService.search(this, agentDescription)[0];

            Iterator servicesIterator = completeAgentDescription.getAllServices();
            while(servicesIterator.hasNext()) {
                ServiceDescription service = (ServiceDescription) servicesIterator.next();
                if(service.getType().equals("book " + book) && service.getName().equals("book " + book)) {
                    servicesIterator.remove();
                    break;
                }
            }

            DFService.modify(this, completeAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException ignored) {}
    }

    public void bidForBook(String book, float maxPrice) {
        this.books.put(book, maxPrice);
        this.addBehaviour(new BidBookBehaviour(this, book));
    }

    public HashMap<String,Float> getBooks() {
        return books;
    }

    public BuyerGui getGui() {
        return gui;
    }
}