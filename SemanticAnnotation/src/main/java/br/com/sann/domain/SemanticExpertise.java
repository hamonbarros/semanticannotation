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
 * @author Hamons
 */
@Entity
@Table(name = "semanticexpertise")
public class SemanticExpertise implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SEMANTIC_EXPERTISE", sequenceName = "semanticexpertise_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SEMANTIC_EXPERTISE")
	private Integer id;

	@Column(name = "frequency")
	private Integer frequency;

	@Column(name = "degree")
	private Double degree;

	@JoinColumn(name = "concept", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept concept;

	@JoinColumn(name = "service", referencedColumnName = "id")
	@ManyToOne
	private Service service;

	public SemanticExpertise() {
	}

	public SemanticExpertise(Service service, OntologyConcept concept,
			Integer frequency, Double degree) {
		this.service = service;
		this.concept = concept;
		this.frequency = frequency;
		this.degree = degree;
	}

	public SemanticExpertise(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Double getDegree() {
		return degree;
	}

	public void setDegree(Double degree) {
		this.degree = degree;
	}

	public OntologyConcept getConcept() {
		return concept;
	}

	public void setConcept(OntologyConcept concept) {
		this.concept = concept;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public String toString() {
		return "Provedor:" + service.getServiceProvider().getName()
				+ "   Serviço:" + service.getId() + "   Conceito:"
				+ this.concept.getConcept() + "   Frequencia:" + this.frequency
				+ "   Grau:" + this.degree;
	}

}
