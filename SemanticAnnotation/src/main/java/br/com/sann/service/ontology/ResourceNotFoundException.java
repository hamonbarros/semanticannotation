/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.service.ontology;

/**
 *
 * @author Ranielly
 */
public class ResourceNotFoundException extends OntologyParserException {

       public ResourceNotFoundException(String resourceName){
             super("Resource Not Found:"+resourceName);
       }
    
}
