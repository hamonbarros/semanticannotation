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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Classe de domínio para representar os atributos dos serviços WFS.
 * 
 * @author Hamon
 */
@Entity
@Table(name = "attributespatialdata")
public class AttributeSpatialData implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_ATRIBUTO", sequenceName = "attributeservice_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_ATRIBUTO")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;

	@JoinColumn(name = "spatialdataid", referencedColumnName = "id")
	@ManyToOne
	private SpatialData spatialData;
	
	@Column(name = "annotated")
	private Boolean annotated;
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public SpatialData getSpatialData() {
		return spatialData;
	}

	public void setSpatialData(SpatialData spatialData) {
		this.spatialData = spatialData;
	}

	public Boolean getAnnotated() {
		return annotated;
	}

	public void setAnnotated(Boolean annotated) {
		this.annotated = annotated;
	}

	@Override
	public String toString() {
		return "Nome: " + name + " | Tipo: " + type;
	}

}
