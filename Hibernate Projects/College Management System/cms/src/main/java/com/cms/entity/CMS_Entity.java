package com.cms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
@Table(name="new_cms_table")
public class CMS_Entity {
	@Id
	private int id;
	@Column(name="fName",length=20,nullable=false)
	private String fName;
	@Column(name="lName",length=20,nullable=false)
	private String lName;
	@Column(name="fees")
	private double fees;
	@Column(name="phone",length=11,unique=true)
	private long ph;
}
