/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "wikipediapageannotation")
public class WikipediaPageAnnotation implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_WIKI", sequenceName = "wikipediapageannotation_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_WIKI")
	private Integer id;

	@JoinColumn(name = "conceptid", referencedColumnName = "id")
	@ManyToOne
	private OntologyConcept concept;

	@JoinColumn(name = "pageid", referencedColumnName = "id")
	@ManyToOne
	private WikipediaPageDescription page;

	public WikipediaPageAnnotation() {
	}

	public WikipediaPageAnnotation(Integer id) {
		this.id = id;
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

	public WikipediaPageDescription getPage() {
		return page;
	}

	public void setPage(WikipediaPageDescription pageid) {
		this.page = pageid;
	}

}
