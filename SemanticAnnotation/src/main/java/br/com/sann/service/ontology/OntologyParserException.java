

package br.com.sann.service.ontology;

/**
 *
 * @author Fabio
 */
public class OntologyParserException extends Exception {
    
      public OntologyParserException(Exception e){
            super(e);
      }
      
      public OntologyParserException(String message){
           super(message);
      }

}
