// file: AuctionOntology.java generated by ontology bean generator.  DO NOT EDIT, UNLESS YOU ARE REALLY SURE WHAT YOU ARE DOING!
package app.ontology;

import jade.content.onto.*;
import jade.content.schema.*;

/** file: AuctionOntology.java
 * @author OntologyBeanGenerator v4.1
 * @version 2017/05/14, 01:45:38
 */
public class AuctionOntology extends jade.content.onto.Ontology  {

  private static final long serialVersionUID = 5193118644451756461L;

  //NAME
  public static final String ONTOLOGY_NAME = "auction";
  // The singleton instance of this ontology
  private static Ontology theInstance = new AuctionOntology();
  public static Ontology getInstance() {
     return theInstance;
  }


   // VOCABULARY
    public static final String SENDOFFER_OFFER="offer";
    public static final String SENDOFFER="SendOffer";
    public static final String WINOFFER_WONOFFER="wonOffer";
    public static final String WINOFFER="WinOffer";
    public static final String OFFER_BOOK="book";
    public static final String OFFER_PRICE="price";
    public static final String OFFER="Offer";
    public static final String BOOK_TITLE="title";
    public static final String BOOK="Book";

  /**
   * Constructor
  */
  private AuctionOntology(){ 
    super(ONTOLOGY_NAME, BasicOntology.getInstance());
    try { 

    // adding Concept(s)
    ConceptSchema bookSchema = new ConceptSchema(BOOK);
    add(bookSchema, app.ontology.Book.class);
    add(bookSchema, app.ontology.impl.DefaultBook.class);
    ConceptSchema offerSchema = new ConceptSchema(OFFER);
    add(offerSchema, app.ontology.Offer.class);
    add(offerSchema, app.ontology.impl.DefaultOffer.class);

    // adding AgentAction(s)
    AgentActionSchema winOfferSchema = new AgentActionSchema(WINOFFER);
    add(winOfferSchema, app.ontology.WinOffer.class);
    add(winOfferSchema, app.ontology.impl.DefaultWinOffer.class);
    AgentActionSchema sendOfferSchema = new AgentActionSchema(SENDOFFER);
    add(sendOfferSchema, app.ontology.SendOffer.class);
    add(sendOfferSchema, app.ontology.impl.DefaultSendOffer.class);

    // adding AID(s)

    // adding Predicate(s)


    // adding fields
    bookSchema.add(BOOK_TITLE, (TermSchema)getSchema(BasicOntology.STRING), ObjectSchema.OPTIONAL);
    offerSchema.add(OFFER_PRICE, (TermSchema)getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
    offerSchema.add(OFFER_BOOK, bookSchema, ObjectSchema.OPTIONAL);
    winOfferSchema.add(WINOFFER_WONOFFER, offerSchema, ObjectSchema.OPTIONAL);
    sendOfferSchema.add(SENDOFFER_OFFER, offerSchema, ObjectSchema.OPTIONAL);

    // adding name mappings

    // adding inheritance

   }catch (java.lang.Exception e) {e.printStackTrace();}
  }
}
