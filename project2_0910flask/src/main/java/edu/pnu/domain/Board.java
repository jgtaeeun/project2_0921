package edu.pnu.domain;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "board")

public class Board {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="board_id" )
	private Long boardId;
	
	private String title;
	private String content;
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="create_date" )
	private Date createDate;

	@Column(name="count" )
	private Long count;
	
	@Column(name="file_name" )
	private String fileName; // Store the file name or URL
	
	
	@ManyToOne
	@JoinColumn(name="fullnumber" , referencedColumnName = "fullnumber", nullable = false) // Foreign key column in Board table
	private Member member;
	
	public void setCreateDate(Date date) {
		// TODO Auto-generated method stub
		this.createDate= date;
		
	}
	
	public Long getBoardId() {
		// TODO Auto-generated method stub
		return boardId;
	}
	public String getTitle() {
		// TODO Auto-generated method stub
		return title;
	}
	public String  getContent() {
		// TODO Auto-generated method stub
		return content;
	}
	public void setContent( String content2) {
		// TODO Auto-generated method stub
		this.content=content2;
		
	}
	public void setTitle( String title2) {
		// TODO Auto-generated method stub
		this.title=title2;
	}

	public Member getMember() {
		// TODO Auto-generated method stub
		return member;
	}

	public void setMember(Member member2) {
		// TODO Auto-generated method stub
		this.member=member2;
	}

	public void setCount(Long i) {
		// TODO Auto-generated method stub
		this.count=i;
	}
	
	public Long getCount() {
		return count;
	}

	public Date getCreateDate() {
		return createDate;
	}
	
	  public String getFileName() {
	        return fileName;
	    }

	public void setFileName(String file) {
		// TODO Auto-generated method stub
		this.fileName=file;
	}

	 
	
	
}