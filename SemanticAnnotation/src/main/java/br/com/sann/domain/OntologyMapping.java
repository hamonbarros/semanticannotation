/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "ontologymapping")
public class OntologyMapping implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_MAPEAMENTO", sequenceName = "ontologymapping_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_MAPEAMENTO")
	private Integer id;

	@Column(name = "externalontology")
	private String externalontology;

	@Column(name = "mappingontology")
	private String mappingontology;

	@JoinColumn(name = "internalontology", referencedColumnName = "id")
	@ManyToOne
	private Ontology internalontology;

	public OntologyMapping() {
	}

	public OntologyMapping(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExternalontology() {
		return externalontology;
	}

	public void setExternalOntology(String externalontology) {
		this.externalontology = externalontology;
	}

	public String getMappingOntology() {
		return mappingontology;
	}

	public void setMappingOntology(String mappingontology) {
		this.mappingontology = mappingontology;
	}

	public Ontology getInternalOntology() {
		return internalontology;
	}

	public void setInternalOntology(Ontology internalontology) {
		this.internalontology = internalontology;
	}

	@Override
	public String toString() {
		return "br.com.swsgis.model.Ontologymapping[id=" + id + "]";
	}

}
