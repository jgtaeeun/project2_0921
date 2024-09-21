package edu.pnu.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	@Query("select m from Member m where m.username =?1")
	Member findByUsername(String username);

	@Query("select m from Member m where m.register = false and m.role = 'ROLE_MEMBER'")
	List<Member> findMembersForRegister();

	@Query("select m from Member m where m.register =true and  m.role='ROLE_MEMBER'")
	List<Member> findMembersForRegisterFinish();
	@Query("select m from Member m where m.username =?1 and m.register=true")
	Member findFullnumber(String textData);

	@Query("select m from Member m where m.role = 'ROLE_MEMBER'")
	List<Member> WithOUtAdmin();
	
	@Query("select m.username from Member m where  m.register=true and m.username like %?1%")
	List<String> findSimilarFullnumber(String username);
}
