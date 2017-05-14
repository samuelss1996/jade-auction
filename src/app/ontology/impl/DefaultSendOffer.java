package app.ontology.impl;


import app.ontology.*;

/**
* Protege name: SendOffer
* @author OntologyBeanGenerator v4.1
* @version 2017/05/14, 01:45:38
*/
public class DefaultSendOffer implements SendOffer {

  private static final long serialVersionUID = 5193118644451756461L;

  private String _internalInstanceName = null;

  public DefaultSendOffer() {
    this._internalInstanceName = "";
  }

  public DefaultSendOffer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: offer
   */
   private Offer offer;
   public void setOffer(Offer value) { 
    this.offer=value;
   }
   public Offer getOffer() {
     return this.offer;
   }

}
