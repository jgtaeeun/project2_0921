package edu.pnu.service;



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
public class FlaskResponse {
    private String image; // Base64로 인코딩된 이미지 데이터
    private String text;  // 일반 텍스트 데이터
    private String message;  // 일반 텍스트 데이터
    private String score;
	public String getImage() {
		// TODO Auto-generated method stub
		return image; 
	}
	public String getText() {
		// TODO Auto-generated method stub
		return  text;
	}
	public String getScore() {
		// TODO Auto-generated method stub
		return score;
	}
}