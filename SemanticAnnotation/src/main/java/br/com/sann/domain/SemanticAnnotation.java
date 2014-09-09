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
@Table(name = "semanticannotation")
public class SemanticAnnotation implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_ANNOTATION", sequenceName = "semanticannotation_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_ANNOTATION")
	private Integer id;

	@JoinColumn(name = "conceptid", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept concept;

	@JoinColumn(name = "featuretypeid", referencedColumnName = "id")
	@ManyToOne
	private SpatialData featureType;

	public SemanticAnnotation() {
	}

	public SemanticAnnotation(Integer id) {
		this.id = id;
	}

	public SemanticAnnotation(SpatialData featureType, OntologyConcept concept) {
		this.featureType = featureType;
		this.concept = concept;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OntologyConcept getConceptid() {
		return concept;
	}

	public void setConceptid(OntologyConcept conceptid) {
		this.concept = conceptid;
	}

	public SpatialData getFeaturetypeid() {
		return featureType;
	}

	public void setFeaturetypeid(SpatialData featuretypeid) {
		this.featureType = featuretypeid;
	}

}
