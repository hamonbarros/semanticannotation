package br.com.sann.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "ontologyconcept")
public class OntologyConcept implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_CONCEITO", sequenceName = "ontologyconcept_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_CONCEITO")
	private Integer id;

	@Column(name = "concept")
	private String concept;

	@Column(name = "conceptname")
	private String conceptName;

	@Column(name = "normalizedname")
	private String normalizedName;

	// @Column(name = "relevance")
	@Transient
	private float relevance;

	// @Column(name = "vector")
	@Transient
	private String vector;

	@OneToMany(mappedBy = "searchconcept")
	private Collection<ConceptSimilarity> similaritiesAsQueryConcept;

	@OneToMany(mappedBy = "databaseconcept")
	private Collection<ConceptSimilarity> similaritiesAsDatabaseConcept;

	@OneToMany(mappedBy = "concept", cascade = { CascadeType.ALL })
	private Collection<SemanticExpertise> semanticExpertises;

	@OneToMany(mappedBy = "concept", cascade = { CascadeType.ALL })
	private Collection<SemanticAnnotation> semanticAnnotations;

	public OntologyConcept() {
	}

	public OntologyConcept(String uri) {
		this.concept = uri;
	}

	public OntologyConcept(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	public Collection<ConceptSimilarity> getSimilaritiesAsQueryConcept() {
		return similaritiesAsQueryConcept;
	}

	public void setSimilaritiesAsQueryConcept(
			Collection<ConceptSimilarity> conceptsimilarityCollection) {
		this.similaritiesAsQueryConcept = conceptsimilarityCollection;
	}

	public Collection<ConceptSimilarity> getSimilaritiesAsDatabaseConcept() {
		return similaritiesAsDatabaseConcept;
	}

	public void setConceptsimilarityCollection1(
			Collection<ConceptSimilarity> setSimilaritiesAsDatabaseConcept) {
		this.similaritiesAsDatabaseConcept = setSimilaritiesAsDatabaseConcept;
	}

	public void setSemanticExpertises(
			Collection<SemanticExpertise> semanticExpertise) {
		this.semanticExpertises = semanticExpertise;

	}

	public Collection<SemanticExpertise> getSemanticExpertises() {
		return this.semanticExpertises;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OntologyConcept) {
			OntologyConcept other = (OntologyConcept) obj;
			return this.id.equals(other.id);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.concept.hashCode();
	}
	
	public String getSimilarityConceptsAsString() {
		String result = "(";
		Iterator<ConceptSimilarity> it = this.getSimilaritiesAsQueryConcept()
				.iterator();
		while (it.hasNext()) {
			ConceptSimilarity currentSimilarity = it.next();
			result += "'" + currentSimilarity.getDatabaseConcept().getConcept()
					+ "'";
			if (it.hasNext())
				result += ",";
		}
		result += ")";
		return result;
	}

	public String getConceptName() {
		return conceptName;
	}

	public void setConceptName(String conceptName) {
		this.conceptName = conceptName;
	}

	public String getNormalizedName() {
		return normalizedName;
	}

	public void setNormalizedName(String normalizedName) {
		this.normalizedName = normalizedName;
	}

	/**
	 * @return the semanticAnnotations
	 */
	public Collection<SemanticAnnotation> getSemanticAnnotations() {
		return semanticAnnotations;
	}

	/**
	 * @param semanticAnnotations
	 *            the semanticAnnotations to set
	 */
	public void setSemanticAnnotations(
			Collection<SemanticAnnotation> semanticAnnotations) {
		this.semanticAnnotations = semanticAnnotations;
	}

	/**
	 * @return the relevance
	 */
	public float getRelevance() {
		return relevance;
	}

	/**
	 * @param relevance
	 *            the relevance to set
	 */
	public void setRelevance(float relevance) {
		this.relevance = relevance;
	}

	/**
	 * @return the vector
	 */
	public String getVector() {
		return vector;
	}

	/**
	 * @param vector
	 *            the vector to set
	 */
	public void setVector(String vector) {
		this.vector = vector;
	}

}
