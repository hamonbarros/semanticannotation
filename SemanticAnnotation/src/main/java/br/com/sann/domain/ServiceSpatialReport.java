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
 */
@Entity
@Table(name = "servicespatialreport")
public class ServiceSpatialReport implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SERVICE_SPATIAL_REPORT", sequenceName = "servicespatialreport_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SERVICE_SPATIAL_REPORT")
	private Integer id;

	@Column(name = "frequency")
	private Integer frequency;

	@JoinColumn(name = "serviceid", referencedColumnName = "id")
	@ManyToOne
	private Service service;

	public ServiceSpatialReport() {
	}

	public ServiceSpatialReport(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service serviceid) {
		this.service = serviceid;
	}

}
