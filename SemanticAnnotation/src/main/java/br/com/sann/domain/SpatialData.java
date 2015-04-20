package br.com.sann.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "spatialdata")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dataType", discriminatorType = DiscriminatorType.STRING)
public class SpatialData implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_DADO_ESPACIAL", sequenceName = "spatialdata_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_DADO_ESPACIAL")
	private Integer id;

	@Column(name = "name")
	protected String name;

	@Column(name = "title")
	protected String title;

	@Column(name = "textdescription")
	protected String textDescription;

	@Column(name = "theme")
	protected String theme;

	@Column(name = "uri")
	protected String uri;

	@Column(name = "metadataIdentifier")
	private String metadataIdentifier;

	@Column(name = "beginTime")
	private Timestamp beginTime;

	@Column(name = "endTime")
	private Timestamp endTime;

	@Column(name = "updatefrequency")
	private String updateFrequency;

	@Column(name = "keywords")
	private String keywords;
	
	@Column(name = "annotated")
	private Boolean annotated;
	
	@OneToMany(mappedBy = "spatialData", cascade = { CascadeType.ALL })
	private Collection<AttributeSpatialData> attributesService;	

	@OneToMany(mappedBy = "featureType")
	private Collection<SemanticAnnotation> semanticAnnotations;

	@Transient
	private Collection<String> crs;

	@JoinColumn(name = "serviceid", referencedColumnName = "id")
	@ManyToOne
	private Service service;

	public SpatialData() {
		crs = new Vector();
	}

	public SpatialData(Integer id) {
		this.id = id;
		crs = new Vector();
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textdescription) {
		this.textDescription = textdescription;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getURI() {
		return uri;
	}

	public void setURI(String uri) {
		this.uri = uri;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public void addCRS(String srs) {
		crs.add(srs);
	}

	public Collection<String> getCRS() {
		return crs;
	}

	/**
	 * @return the metadataIdentifier
	 */
	public String getMetadataIdentifier() {
		return metadataIdentifier;
	}

	/**
	 * @param metadataIdentifier
	 *            the metadataIdentifier to set
	 */
	public void setMetadataIdentifier(String metadataIdentifier) {
		this.metadataIdentifier = metadataIdentifier;
	}

	/**
	 * @return the beginTime
	 */
	public Timestamp getBeginTime() {
		return beginTime;
	}

	/**
	 * @param beginTime
	 *            the beginTime to set
	 */
	public void setBeginTime(Timestamp beginTime) {
		this.beginTime = beginTime;
	}

	/**
	 * @return the endTime
	 */
	public Timestamp getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime
	 *            the endTime to set
	 */
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getUpdateFrequency() {
		return updateFrequency;
	}

	public void setUpdateFrequency(String updateFrequency) {
		this.updateFrequency = updateFrequency;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	public Boolean getAnnotated() {
		return annotated;
	}

	public void setAnnotated(Boolean annotated) {
		this.annotated = annotated;
	}
	
	public Collection<AttributeSpatialData> getAttributesService() {
		return attributesService;
	}

	public void setAttributesService(Collection<AttributeSpatialData> attributesService) {
		this.attributesService = attributesService;
	}

	/**
	 * @param keywords
	 *            the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getTokenDescription() {
		String description = "";
		if (title != null)
			description += title;
		if (textDescription != null)
			description += " " + textDescription;
		if (getKeywords() != null)
			description += " " + getKeywords();
		if (this.getService().getName() != null) {
			description += " " + getService().getName();
		}
		if (this.getService().getTextDescription() != null) {
			// description+="  "+getService().getTextDescription();
		}

		return description;
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

	public boolean containsAnnotation(String concept) {
		if (semanticAnnotations == null)
			return false;

		Iterator<SemanticAnnotation> it = semanticAnnotations.iterator();

		while (it.hasNext()) {
			SemanticAnnotation sa = it.next();
			if (sa.getConceptid().getConcept().equals(concept)) {
				return true;
			}

		}

		return false;
	}

	public boolean containsAnnotation(OntologyConcept concept) {
		return containsAnnotation(concept.getConcept());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SpatialData) {
			SpatialData other = (SpatialData) obj;
			return this.id.equals(other.id);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.title.hashCode();
	}
}
