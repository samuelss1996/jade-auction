package app.ontology;



/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2017/05/14, 01:45:38
*/
public interface Offer extends jade.content.Concept {

   /**
   * Protege name: price
   */
   public void setPrice(float value);
   public float getPrice();

   /**
   * Protege name: book
   */
   public void setBook(Book value);
   public Book getBook();

}
