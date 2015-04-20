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
 * Classe de domínio para representar as anotações semânticas dos atributos dos serviços WFS.
 * 
 * @author Hamon
 */
@Entity
@Table(name = "semanticannotationattr")
public class SemanticAnnotationAttribute implements Serializable {

	private static final long serialVersionUID = 606368917964344981L;

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_ATRIBUTO", sequenceName = "semanticannotationattr_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_ATRIBUTO")
	private Integer id;

	@JoinColumn(name = "attributeid", referencedColumnName = "id")
	@ManyToOne
	private AttributeSpatialData attributeService;
	
	@JoinColumn(name = "conceptid", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept ontologyConcept;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AttributeSpatialData getAttributeService() {
		return attributeService;
	}

	public void setAttributeService(AttributeSpatialData attributeService) {
		this.attributeService = attributeService;
	}

	public OntologyConcept getOntologyConcept() {
		return ontologyConcept;
	}

	public void setOntologyConcept(OntologyConcept ontologyConcept) {
		this.ontologyConcept = ontologyConcept;
	}

}
