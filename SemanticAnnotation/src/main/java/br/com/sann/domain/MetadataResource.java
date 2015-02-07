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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 */

@Entity
@Table(name = "metadataresource")
public class MetadataResource implements Serializable {

	public static final int DISCOVERED = 0;
	public static final int PERSISTED = 1;

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_METADATA", sequenceName = "metadataresource_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_METADATA")
	private Integer id;

	@Column(name = "metadataidentifier")
	private String metadataIdentifier;

	@Column(name = "title")
	private String title;

	@Column(name = "publisher")
	private String publisher;

	@Column(name = "textdescription")
	private String textDescription;

	@Column(name = "url")
	private String url;

	@Column(name = "type")
	private int type;

	@Column(name = "status")
	private int status;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
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

	/**
	 * @return the textDescription
	 */
	public String getTextDescription() {
		return textDescription;
	}

	/**
	 * @param textDescription
	 *            the textDescription to set
	 */
	public void setTextDescription(String textDescription) {
		this.textDescription = textDescription;
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
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

}
