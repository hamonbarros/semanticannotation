/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.service.ontology;

/**
 *
 * @author Fabio
 */
public class InvalidModeException extends OntologyParserException{

    public InvalidModeException() {
          super("N?o pode salvar ontologia em modo HTTP");
    }

}
