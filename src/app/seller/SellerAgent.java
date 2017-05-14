package app.seller;

import app.ontology.AuctionOntology;
import app.seller.behaviour.UpdatePriceBehaviour;
import app.seller.util.TwoValuesHashMap;
import jade.content.lang.Codec;
import jade.content.lang.leap.LEAPCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

public class SellerAgent extends Agent {
    private Codec codec;
    private Ontology ontology;

    private TwoValuesHashMap<String, Float, Float> books;
    private SellerGui gui;

    @Override
    protected void setup() {
        this.codec = new LEAPCodec();
        this.ontology = AuctionOntology.getInstance();
        this.getContentManager().registerLanguage(this.codec);
        this.getContentManager().registerOntology(this.ontology);

        this.books = new TwoValuesHashMap<String, Float, Float>();
        this.gui = SellerGui.start();
        this.gui.setAgent(this).setVisible(true);

        try {
            DFAgentDescription agentDescription = new DFAgentDescription();
            ServiceDescription serviceDescription = new ServiceDescription();

            serviceDescription.setType("seller");
            serviceDescription.setName("SellerAgent");

            agentDescription.setName(this.getAID());
            agentDescription.addServices(serviceDescription);
            DFService.register(this, agentDescription);
        } catch (FIPAException ignored) { }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException ignored) {}
    }

    public void addBook(String title, float initialPrice, float increment) {
        this.books.put(title, initialPrice, increment);

        this.addBehaviour(new UpdatePriceBehaviour(this, title));
    }

    public TwoValuesHashMap<String,Float,Float> getBooks() {
        return books;
    }

    public SellerGui getGui() {
        return gui;
    }

    public void removeBook(String book) {
        this.books.remove(book);
    }

    public Codec getCodec() {
        return codec;
    }

    public Ontology getOntology() {
        return ontology;
    }
}