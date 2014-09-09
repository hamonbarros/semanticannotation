package br.com.sann.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 *
 */
@Entity
@Table(name = "ontology")
public class Ontology implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_ONTOLOGIA", sequenceName = "ontology_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_ONTOLOGIA")
	private Integer id;

	@Column(name = "domain")
	private String domain;

	@Column(name = "url")
	private String url;

	@Column(name = "path")
	private String path;

	@Column(name = "vector")
	private String vector;

	@OneToMany(mappedBy = "internalontology")
	private Collection<OntologyMapping> ontologymappingCollection;

	public Ontology() {
	}

	public Ontology(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getURL() {
		return getUrl();
	}

	public void setURL(String url) {
		this.setUrl(url);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Collection<OntologyMapping> getOntologyMapping() {
		return ontologymappingCollection;
	}

	public void setOntologyMapping(
			Collection<OntologyMapping> ontologymappingCollection) {
		this.ontologymappingCollection = ontologymappingCollection;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
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