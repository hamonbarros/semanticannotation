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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 
 * @author Fabio
 * @author Hamon
 * 
 */
@Entity
@Table(name = "spatialexpertise")
public class SpatialExpertise implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SPATIAL_EXPERTISE", sequenceName = "spatialexpertise_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SPATIAL_EXPERTISE")
	private Integer id;

	@Column(name = "frequency")
	private Integer frequency;

	@Column(name = "degree")
	private Double degree;

	@JoinColumn(name = "service", referencedColumnName = "id")
	@ManyToOne
	private Service service;

	public SpatialExpertise() {
	}

	public SpatialExpertise(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getDegree() {
		return degree;
	}

	public void setDegree(Double degree) {
		this.degree = degree;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public void setFreqency(int frequency) {
		this.frequency = frequency;
	}

}
