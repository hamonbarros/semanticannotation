/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.service.ontology;

/**
 *
 * @author Fabio
 */
public class OntologyNotFoundException extends OntologyParserException{

       public OntologyNotFoundException(String ontologyName){
           super("Ontologia n?o encontrada:"+ontologyName);
       }

}
