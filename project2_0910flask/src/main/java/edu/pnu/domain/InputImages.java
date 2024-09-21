package edu.pnu.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "input_images")

public class InputImages {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="idx" )
    private Long id;

	
	@Column(name = "name", nullable = false)
    private String name;
	@Column(name = "path", nullable = false)
    private String path;
	
	@Column(name = "status", nullable = false)
    private boolean status;

	
	@Column(name = "name2", nullable = false)
    private String name2;
	
	
	@Column(name = "path2", nullable = false)
    private String path2;
	
	@Column(name = "recognize", nullable = false)
    private String recognize;
	
	@Column(name = "fullnumber", nullable = false)
    private String fullnumber;

	
	@Column(name = "pair", nullable = false)
    private boolean pair;
	
	
	@Column(name = "cartype", nullable = false)
    private String cartype;
	
	@Column(name = "place", nullable = false)
    private String place;
	
	public String  getPlace() {
		return  place;
	}
	
	public void setCartype( String cartype) {
		this.cartype=cartype;
	}
	
	public String  getCartype() {
		return cartype;
	}
	
	public void setPair(boolean pair) {
		this.pair=pair;
	}
	
	public boolean getPair() {
		return pair;
	}
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public String getName2() {
		// TODO Auto-generated method stub
		return name2;
	}
	
	public String getFullnumber() {
		// TODO Auto-generated method stub
		return fullnumber;
	}

	public String getRecognize() {
		// TODO Auto-generated method stub
		return recognize;
	}
	
	public void setName(String newFilename) {
		// TODO Auto-generated method stub
		this.name=newFilename;
	}

	public void setPath(String path) {
		// TODO Auto-generated method stub
		this.path=path;
	}

	public void setFullnumber(String fullnumber) {
		// TODO Auto-generated method stub
		this. fullnumber= fullnumber;
	}

	public void setRecognize(String recognize) {
		// TODO Auto-generated method stub
		this. recognize= recognize;
		}
	

public void setName2(String newFilename) {
	// TODO Auto-generated method stub
	this.name2=newFilename;
}

public void setPath2(String path) {
	// TODO Auto-generated method stub
	this.path2=path;
}

public String getPath() {
	// TODO Auto-generated method stub
	return path;
}

public void setStatus(boolean b) {
	// TODO Auto-generated method stub
	this.status=b;
}
}
