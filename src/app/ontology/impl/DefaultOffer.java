package app.ontology.impl;


import app.ontology.*;

/**
* Protege name: Offer
* @author OntologyBeanGenerator v4.1
* @version 2017/05/14, 01:45:38
*/
public class DefaultOffer implements Offer {

  private static final long serialVersionUID = 5193118644451756461L;

  private String _internalInstanceName = null;

  public DefaultOffer() {
    this._internalInstanceName = "";
  }

  public DefaultOffer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: price
   */
   private float price;
   public void setPrice(float value) { 
    this.price=value;
   }
   public float getPrice() {
     return this.price;
   }

   /**
   * Protege name: book
   */
   private Book book;
   public void setBook(Book value) { 
    this.book=value;
   }
   public Book getBook() {
     return this.book;
   }

}
