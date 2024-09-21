package edu.pnu.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "timedataset")
public class TimeDataset {
	@Id
	@Column(name="idx" )
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Long idx;


	@ManyToOne
	@JoinColumn(name="fullnumber" , referencedColumnName = "fullnumber", nullable = false) // Foreign key column in Board table
	private Member member;
	
	
	
	@Column(name="timein" )
	private String timein;
	@Column(name="timeout" )
	private String timeout;
	@Column(name="recognize" )
	public String recognize;
	@Column(name="number" )
	public String number;
	
	public Member getMember() {
		return member;
	}
	
	public String getTimein() {
		return timein;
	}
	public String getTimeout() {
		return timeout;
	}
	public String getRecognize() {
		return recognize;
	}
	public String getNumber() {
		return number;
	}
	public void setMember(Member member) {
		this.member=member;
	}
	public void setTimein(String timein) {
		this.timein=timein;
	}
	public void setTimeout(String timein) {
		this.timeout=timein;
	}
	public void setRecognize(String timein) {
		this.recognize=timein;
	}
	public void setNumber(String timein) {
		this.number=timein;
	}
}

