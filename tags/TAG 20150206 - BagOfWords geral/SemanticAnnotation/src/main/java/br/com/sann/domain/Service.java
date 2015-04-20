/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import java.sql.Timestamp;
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
 * 
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "service")
public class Service implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SERVICO", sequenceName = "service_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SERVICO")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "textdescription")
	private String textDescription;

	@Column(name = "url")
	private String url;

	@Column(name = "updatefrequency")
	private String updateFrequency;

	@Column(name = "beginTime")
	private Timestamp beginTime;

	@Column(name = "endTime")
	private Timestamp endTime;

	@Column(name = "publisher")
	private String publisher;

	@Column(name = "keywords")
	private String keywords;

	@JoinColumn(name = "serviceprovider", referencedColumnName = "id")
	@ManyToOne
	private ServiceProvider serviceprovider;

	@OneToMany(mappedBy = "service", cascade = { CascadeType.ALL })
	private Collection<SpatialData> spatialData;

	@OneToMany(mappedBy = "service", cascade = { CascadeType.ALL })
	private Collection<SpatialExpertise> spatialExpertises;

	@OneToMany(mappedBy = "service", cascade = { CascadeType.ALL })
	private Collection<SemanticExpertise> semanticExpertises;

	@OneToMany(mappedBy = "service", cascade = { CascadeType.ALL })
	private Collection<TemporalExpertise> temporaExpertises;

	@Column(name = "metadataIdentifier")
	protected String metadataIdentifier;
	
	@Column(name = "processed")
	private Boolean processed;

	@Transient
	private boolean isFilled = false;

	public Service() {
	}

	public Service(Integer id) {
		this.id = id;
	}

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

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
	}

	public String getURL() {
		return url;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public ServiceProvider getServiceProvider() {
		return serviceprovider;
	}

	public void setServiceProvider(ServiceProvider serviceprovider) {
		this.serviceprovider = serviceprovider;
	}

	public void setSpatialData(Collection<SpatialData> data) {
		this.spatialData = data;
	}

	public void setSpatialExpertise(
			Collection<SpatialExpertise> spatialExpertise) {
		this.spatialExpertises = spatialExpertise;
		isFilled = false;

	}

	public void setSemanticExpertises(
			Collection<SemanticExpertise> semanticExpertise) {
		this.semanticExpertises = semanticExpertise;
		isFilled = false;

	}

	public Collection<SemanticExpertise> getSemanticExpertises() {
		return this.semanticExpertises;

	}

	public double getExpertise(String theme) {
		Iterator<SemanticExpertise> it = this.getSemanticExpertises()
				.iterator();
		while (it.hasNext()) {
			SemanticExpertise expertise = it.next();
			if (expertise.getConcept().getConcept().equals(theme))
				return expertise.getDegree();
		}
		return 0;
	}

	public boolean hasAnyExpertise(String theme) {
		Iterator<SemanticExpertise> it = this.getSemanticExpertises()
				.iterator();
		while (it.hasNext()) {
			SemanticExpertise currentExpertise = it.next();
			if (currentExpertise.getConcept().getConcept().equals(theme))
				return true;
		}
		return false;
	}

	public boolean hasAnyExpertise(OntologyConcept theme) {
		Iterator<SemanticExpertise> it = this.getSemanticExpertises()
				.iterator();
		while (it.hasNext()) {
			SemanticExpertise currentExpertise = it.next();
			if (currentExpertise.getConcept().equals(theme))
				return true;
		}
		return false;
	}

	public String getMetadataIdentifier() {
		return metadataIdentifier;
	}

	public void setMetadataIdentifier(String metadataIdentifier) {
		this.metadataIdentifier = metadataIdentifier;
	}

	public String getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(String updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Collection<TemporalExpertise> getTemporaExpertises() {
		return temporaExpertises;
	}

	public void setTemporaExpertises(
			Collection<TemporalExpertise> temporaExpertises) {
		this.temporaExpertises = temporaExpertises;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public Boolean getProcessed() {
		return processed;
	}

	public void setProcessed(Boolean processed) {
		this.processed = processed;
	}

}