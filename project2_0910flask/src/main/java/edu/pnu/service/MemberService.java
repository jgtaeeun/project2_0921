package edu.pnu.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pnu.domain.Member;
import edu.pnu.persistence.MemberRepository;



@Service
public class MemberService {
    @Autowired
    private  MemberRepository  memberRepo;
   
   
    @Transactional
    public Member addMember(Member member) {
    	return memberRepo.save(member);
    	
    }
    // 모든 멤버를 조회
    public List<Member> getAllMembers() {
       
        return  memberRepo.WithOUtAdmin();
    }
    
    // 회원승인된 멤버 조회
    public Member getMember(String username) {
       
        return  memberRepo. findFullnumber(username);
    }
    
    // 회원승인된 멤버 중에서 유사 번호 추출

   public List<String> getSimilarMember(String username) {
    	    // username에서 뒤에서 5자리 추출
    	    String lastFive = username.substring(username.length() - 5);
    	    
    	    // 추출한 5자리로 유사한 회원 조회
    	    return memberRepo.findSimilarFullnumber(lastFive);
    }

    
    
 // 승인 필요한 멤버 리스트 조회
    public List<Member> getMembersForRegister() {
        
        return  memberRepo.findMembersForRegister();
    }
    
    
 // 승인 완료한 멤버 리스트 조회
   public List<Member> getMembersForRegisterFinish() {
        
        return  memberRepo.findMembersForRegisterFinish();
    }
   
    
    
    public boolean updateMemberRegister(String username) {
        // 특정 멤버의 register 값을 업데이트
        Member member = memberRepo.findByUsername(username);
        if (member != null && member.isRegister() ==false) {
            member.setRegister(true);
            memberRepo.save(member);
            return true;
        }
        else if (member != null && member.isRegister() ==true) {
        	member.setRegister(false);
            memberRepo.save(member);
            return true;
        }
        return false;
    }
}
