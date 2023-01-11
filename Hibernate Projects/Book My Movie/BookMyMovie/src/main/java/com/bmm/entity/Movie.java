package com.bmm.entity;

import javax.persistence.Column;
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
@NoArgsConstructor
@ToString
@Entity
@Table(name="movies")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int entryNo;
	@Column(length = 50) private String movieName;
	@Column(length = 50) private String multiplex;
	@Column(length = 50) private String time;
	@Column(length = 50) private String screen;
	@Column(length = 20) private String genre;
	@Column(length = 10) private String language;
	@Column(length = 3) private int seatCount;
	@Column(length = 3) private int price;
}
