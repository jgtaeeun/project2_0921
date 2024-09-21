package edu.pnu.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.gson.Gson;

import edu.pnu.domain.ImageDataDTO;
import edu.pnu.domain.InputImages;
import edu.pnu.domain.Member;
import edu.pnu.domain.OutputImages;
import edu.pnu.domain.TimeDataset;
import edu.pnu.persistence.InputImagesRepository;
import edu.pnu.persistence.OutputImagesRepository;
import edu.pnu.service.InformationService;
import edu.pnu.service.MemberService;
import edu.pnu.service.WebSocketService;

@RestController
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	MemberService memberService;
	
	 @Autowired
	    private WebSocketService webSocketService;
	
	@Autowired
	private InformationService informationService;
	
    @Autowired
    private InputImagesRepository inputImagesRepository;
    @Autowired
    private OutputImagesRepository outputImagesRepository;
    
	
	  @GetMapping("/members")
	    public ResponseEntity<List<Member>> getMembers() {
	        // 멤버 목록 조회 (모든 차량 조회)
	        List<Member> members = memberService.getAllMembers();
	        // DTO를 사용하여 비밀번호를 마스킹 처리
	        
	        // DTO를 사용하여 비밀번호를 마스킹 처리
	        // DTO로 변환 및 비밀번호 마스킹 처리
	      
	        return ResponseEntity.ok(members );
	    }


	  
//	  @GetMapping("/member")
//	    public ResponseEntity<Member> getMember( @RequestParam String fullnumber) {
//	        // 멤버 목록 조회 
//	        Member member = memberService. getMember(fullnumber);
//	        return ResponseEntity.ok(member);
//	    }
	  @PostMapping("/member")
	    public ResponseEntity<List<String>> getMember( @RequestParam String fullnumber) {
	        // 멤버 목록 조회 
		  	List<String> member = memberService. getSimilarMember(fullnumber);
	        return ResponseEntity.ok(member);
	    }
	  
	  
	  
	  @GetMapping("/members/register")             //member객체 중 register이 false인 거
	    public ResponseEntity<List<Member>> getMembersForRegister() {
	        // 멤버 목록 조회
	        List<Member> members = memberService.getMembersForRegister();
	        return ResponseEntity.ok(members);
	    }
	  
	
	  
	  
	  @GetMapping("/members/registerFinish")             //member객체 중 register이 true인 거
	    public ResponseEntity<List<Member>> getMembersForRegisterFinish() {
	        // 멤버 목록 조회
	        List<Member> members = memberService.getMembersForRegisterFinish();
	        return ResponseEntity.ok(members);
	    }
	  
	
	  
	  @PostMapping("/members/register")
	    public ResponseEntity<String> updateMemberRegister(
	    		@RequestParam String username) {
	        // 멤버의 register 값을 업데이트
	        boolean updated = memberService.updateMemberRegister(username);
	        if (updated) {
	            return ResponseEntity.ok("등록 상태가 성공적으로 업데이트되었습니다.");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
	        }
	    }
	  
		//모든차량의 입출차
		 @GetMapping("/inout")
		 public ResponseEntity< ImageDataDTO>  getCarinoutAll(){
	
			 return ResponseEntity.ok(informationService.getCarinoutAll());
		 }
		 
		 
		 
		 //입출차 기록에서 수정사항(고철장)
		 @PostMapping("/inout/outputImage")
		    public OutputImages updateCarList(
		    		@RequestParam Long id,
		    		@RequestParam String fullnumber ) {
		        // 멤버의 register 값을 업데이트
		        boolean updated = informationService.updateCarList(id, fullnumber);
		       
		        if (updated) {
		        	
		            return  outputImagesRepository.findByIdForUpdate(id);
		        	
		        } 
		        
		        else {
		        	return null;
		        }
		        
		     }
		 
		 //입출차 기록에서 수정사항(계근대)
		 @PostMapping("/inout/inputImage")
		    public InputImages updateCarList2(
		    		@RequestParam Long id,
		    		@RequestParam String fullnumber ) {
		        // 멤버의 register 값을 업데이트
			 
		        boolean updated = informationService.updateCarList2(id, fullnumber);
		       
		        if (updated) {
		        	
		            return  inputImagesRepository.findByIdForUpdate(id);
		        	
		        } 
		        
		        else {
		        	return null;
		        }
		        
		     }
		 
		 
		    
}
	