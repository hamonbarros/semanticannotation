/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.service.ontology;

/**
 *
 * @author Fabio
 */
public class InvalidOntologyPathException extends Exception{

    public InvalidOntologyPathException(String ontologyPath) {
          super("Caminho da ontologia ? inv?lido:"+ontologyPath);
    }

}
