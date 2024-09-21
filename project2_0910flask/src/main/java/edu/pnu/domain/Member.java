package edu.pnu.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "member")

public class Member {
	@Id
	@Column(name="fullnumber" )
	private String username;
	
	private String password;
	private boolean register;
	@Enumerated(EnumType.STRING)
	private Role role;
	private String phonenumber;


	
	
	public String getUsername() {
		// TODO Auto-generated method stub
		return  username;
	}

	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	public boolean isRegister() {
		// TODO Auto-generated method stub
		return register;
	}

	public Role getRole() {
		// TODO Auto-generated method stub
		return role;
	}
	public String getPhonenumber() {
		// TODO Auto-generated method stub
		return  phonenumber;
	}

	public void setRegister(boolean b) {
		// TODO Auto-generated method stub
		this.register=b;
	}

	public void setPassword(String encodedPassword) {
		// TODO Auto-generated method stub
		this.password=encodedPassword;
	}

	public void setRole(Role roleMember) {
		// TODO Auto-generated method stub
		this.role=roleMember;
	}
	
	public void setPhonenumber(String  phonenumber) {
		// TODO Auto-generated method stub
		this.phonenumber=phonenumber;
	}
	public void setUsername(String  username) {
		// TODO Auto-generated method stub
		this. username= username;
	}

}
