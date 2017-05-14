package app.ontology.impl;


import app.ontology.*;

/**
* Protege name: Book
* @author OntologyBeanGenerator v4.1
* @version 2017/05/14, 01:45:38
*/
public class DefaultBook implements Book {

  private static final long serialVersionUID = 5193118644451756461L;

  private String _internalInstanceName = null;

  public DefaultBook() {
    this._internalInstanceName = "";
  }

  public DefaultBook(String instance_name) {
    this._internalInstanceName = instance_name;
  }

  public String toString() {
    return _internalInstanceName;
  }

   /**
   * Protege name: title
   */
   private String title;
   public void setTitle(String value) { 
    this.title=value;
   }
   public String getTitle() {
     return this.title;
   }

}
