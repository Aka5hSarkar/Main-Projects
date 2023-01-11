package com.bmm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="booking_list")
public class BookingList {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int bookingEntry;
	private String customerEmail;
	private String movieName;
	private String multiplex;
	private String screen;
	private String time;
	private int ticketCount;
	private int price;
}
