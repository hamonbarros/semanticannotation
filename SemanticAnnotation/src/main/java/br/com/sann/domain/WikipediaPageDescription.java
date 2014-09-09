/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "wikipediapagedescription")
public class WikipediaPageDescription implements Serializable {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "title")
	private String title;

	@Column(name = "categories")
	private String categories;

	@Column(name = "links")
	private String links;

	@Column(name = "tokens")
	private String tokens;

	@Column(name = "processed")
	private boolean processed;

	@OneToMany(mappedBy = "page")
	private Collection<WikipediaPageAnnotation> annotations;

	public WikipediaPageDescription() {
		processed = false;
	}

	public WikipediaPageDescription(Integer id) {
		this.id = id;
		processed = false;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<WikipediaPageAnnotation> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(
			Collection<WikipediaPageAnnotation> wikipediapageannotationCollection) {
		this.annotations = wikipediapageannotationCollection;
	}

	/**
	 * @return the categories
	 */
	public String getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(String categories) {
		this.categories = categories;
	}

	/**
	 * @return the links
	 */
	public String getLinks() {
		return links;
	}

	/**
	 * @param links
	 *            the links to set
	 */
	public void setLinks(String links) {
		this.links = links;
	}

	/**
	 * @return the tokens
	 */
	public String getTokens() {
		return tokens;
	}

	/**
	 * @param tokens
	 *            the tokens to set
	 */
	public void setTokens(String tokens) {
		this.tokens = tokens;
	}

	/**
	 * @return the processed
	 */
	public boolean isProcessed() {
		return processed;
	}

	/**
	 * @param processed
	 *            the processed to set
	 */
	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

}
