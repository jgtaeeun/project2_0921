package edu.pnu.domain;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@Table(name = "board_re")

public class BoardRe {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="boardre_id" )
	private Long boardReId;
	
	private String content;
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="create_date" )
	private Date createDate;

	@ManyToOne
	@JoinColumn(name="fullnumber" , referencedColumnName = "fullnumber", nullable = false) // Foreign key column in Board table
	private Member member;
	
	@ManyToOne
	@JoinColumn(name="board_id" , referencedColumnName = "board_id",  nullable=false)
	private Board board;

	

	public void setCreateDate(Date date) {
		// TODO Auto-generated method stub
		this.createDate= date;
	}

	public void setBoard(Board board2) {
		// TODO Auto-generated method stub
		this.board=  board2;
	}

	public void setMember(Member member2) {
		// TODO Auto-generated method stub
		this.member=member2;
	}
	
	public void setContent(String content) {
		// TODO Auto-generated method stub
		this.content=content;
	}
	
	public String getContent() {
		// TODO Auto-generated method stub
		return content;
	}
	public Date getCreateDate() {
		// TODO Auto-generated method stub
		return createDate;
	}
	public  Long getboardReId() {
		// TODO Auto-generated method stub
		return boardReId;
	}
	public  Member getMember() {
		// TODO Auto-generated method stub
		return member;
	}
	public  Board getBoard() {
		// TODO Auto-generated method stub
		return board;
	}
}






