package com.bmm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="admin_details")
public class Admin {
	@Id
	@Column(length = 5) private String adminId;
	@Column(length = 20) private String adminFName;
	@Column(length = 20) private String adminLName;
	@Column(length = 50) private String adminAddress;
	@Column(length = 10) private String adminPhn;
	@Column(length = 6) private String adminPwd;
	@Override
	public String toString() {
		return "[Id=" + adminId + ", Name=" + adminFName + " " + adminLName
				+ ", Address=" + adminAddress + ", Phone=" + adminPhn + "]";
	}
	
}
