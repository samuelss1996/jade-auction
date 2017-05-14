package app.ontology.impl;


import app.ontology.*;

/**
* Protege name: WinOffer
* @author OntologyBeanGenerator v4.1
* @version 2017/05/14, 01:45:38
*/
public class DefaultWinOffer implements WinOffer {

  private static final long serialVersionUID = 5193118644451756461L;

  private String _internalInstanceName = null;

  public DefaultWinOffer() {
    this._internalInstanceName = "";
  }

  public DefaultWinOffer(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: wonOffer
   */
   private Offer wonOffer;
   public void setWonOffer(Offer value) { 
    this.wonOffer=value;
   }
   public Offer getWonOffer() {
     return this.wonOffer;
   }

}
