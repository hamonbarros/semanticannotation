/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Vector;

import javax.persistence.CascadeType;
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
 */
@Entity
@Table(name = "serviceprovider")
public class ServiceProvider implements Serializable {

	public static final int DESCRIPTION_MAX_LENGTH = 3000;

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_PROVEDOR", sequenceName = "serviceprovider_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_PROVEDOR")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "url")
	private String url;

	@Column(name = "textdescription")
	private String textDescription;

	@Column(name = "metadataIdentifier")
	private String metadataIdentifier;

	@Column(name = "keywords")
	private String keyWords;

	@Column(name = "publisher")
	private String publisher;

	@OneToMany(mappedBy = "serviceprovider", cascade = { CascadeType.ALL })
	private Collection<Service> services;

	public ServiceProvider() {
		services = new Vector();
	}

	public ServiceProvider(Integer id) {
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

	public String getURL() {
		return getUrl();
	}

	public void setURL(String url) {
		this.setUrl(url);
	}

	public String getTextDescription() {
		return textDescription;
	}

	public void setTextDescription(String textdescription) {
		this.textDescription = textdescription;
	}

	public Collection<Service> getServices() {
		return services;
	}

	public void setServices(Collection<Service> serviceCollection) {
		this.services = serviceCollection;
	}

	public void addService(Service service) {
		service.setServiceProvider(this);
		services.add(service);
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
	 * @return the keyWords
	 */
	public String getKeyWords() {
		return keyWords;
	}

	/**
	 * @param keyWords
	 *            the keyWords to set
	 */
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
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
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *            the publisher to set
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

}
