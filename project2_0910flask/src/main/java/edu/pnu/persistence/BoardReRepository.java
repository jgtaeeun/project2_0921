package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.BoardRe;

public interface  BoardReRepository extends JpaRepository<BoardRe, Long>{
	
	@Query("select b from BoardRe b where b.board.boardId=?1")
	public List<BoardRe> findAllById(Long id);
	
}
