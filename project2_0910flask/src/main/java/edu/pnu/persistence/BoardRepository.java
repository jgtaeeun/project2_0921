package edu.pnu.persistence;

import java.sql.SQLException;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.Board;


public interface  BoardRepository extends JpaRepository<Board, Long> {
	
	@Query("select b from Board b where b.member.username =?1")
	List<Board> findBoardListBySomeone(String fullnumber);
}
