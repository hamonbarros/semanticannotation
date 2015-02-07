/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.sann.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * @author Fabio
 * @author Hamon
 */
@Entity
@Table(name = "servicetemporalreport")
public class ServiceTemporalReport implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@SequenceGenerator(name = "GERADOR_SERVICE_TEMPORAL_REPORT", sequenceName = "servicetemporalreport_id_seq", allocationSize = 0, initialValue = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GERADOR_SERVICE_TEMPORAL_REPORT")
	private Integer id;

	@Column(name = "frequency")
	private Integer frequency;

	@Column(name = "begintime")
	private Timestamp beginTime;

	@Column(name = "endtime")
	private Timestamp endTime;

	@JoinColumn(name = "serviceid", referencedColumnName = "id")
	@ManyToOne
	private Service service;

	public ServiceTemporalReport() {
	}

	public ServiceTemporalReport(Integer id) {
		this.id = id;
	}

	public ServiceTemporalReport(Service service, Timestamp begin,
			Timestamp end, int frequency) {
		this.service = service;
		this.beginTime = begin;
		this.endTime = end;
		this.frequency = frequency;
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

	public Timestamp getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Timestamp begintime) {
		this.beginTime = begintime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endtime) {
		this.endTime = endtime;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service serviceid) {
		this.service = serviceid;
	}

}
