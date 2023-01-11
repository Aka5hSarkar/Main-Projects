package com.bmm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="customer_details")
public class Customer {
	@Id
	@Column(length =50) private String customerEmail;
	@Column(length = 20) private String customerFName;
	@Column(length = 20) private String customerLName;
	@Column(length = 10) private String customerPhone;
	@Column(length = 6) private String customerPassword;
	@Column (length = 10)private boolean subscription;
	@Override
	public String toString() {
		return "[Email=" + customerEmail + ", Name=" + customerFName + " "
				+ customerLName + ", Phone=" + customerPhone + ", Subscription="+subscription+"]";
	}
	
}
