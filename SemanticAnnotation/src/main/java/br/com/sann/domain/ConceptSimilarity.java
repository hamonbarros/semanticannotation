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
@Table(name = "conceptsimilarity")
public class ConceptSimilarity implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SIMILARIDADE", sequenceName = "conceptsimilarity_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SIMILARIDADE")
	private Integer id;

	@Column(name = "similarity")
	private double similarity;

	@Column(name = "relationship")
	private int relationship;

	@JoinColumn(name = "searchconcept", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept searchconcept;

	@JoinColumn(name = "databaseconcept", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept databaseconcept;

	public ConceptSimilarity() {
	}

	public ConceptSimilarity(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OntologyConcept getSearchConcept() {
		return searchconcept;
	}

	public void setSearchConcept(OntologyConcept searchconcept) {
		this.searchconcept = searchconcept;
	}

	public OntologyConcept getDatabaseConcept() {
		return databaseconcept;
	}

	public void setDatabaseConcept(OntologyConcept databaseconcept) {
		this.databaseconcept = databaseconcept;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public String toString() {
		return "SearchConcept:" + this.searchconcept.getConcept()
				+ "   DatabaseConcept:" + this.databaseconcept.getConcept()
				+ "   Similarity:" + this.similarity;

	}

	public int getRelationship() {
		return relationship;
	}

	public void setRelationship(int relationship) {
		this.relationship = relationship;
	}

}
