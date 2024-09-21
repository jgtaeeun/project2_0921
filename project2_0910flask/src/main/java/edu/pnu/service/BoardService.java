package edu.pnu.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.pnu.domain.Board;
import edu.pnu.domain.BoardRe;
import edu.pnu.domain.Member;
import edu.pnu.persistence.BoardReRepository;
import edu.pnu.persistence.BoardRepository;




//
@Service
public class BoardService {

  
    @Autowired
    private BoardRepository  boardRepo;
    
    @Autowired
    private BoardReRepository boardReRepo;
    
    
    
    
    //게시판 리스트
    public List<Board> getAllBoardList() throws SQLException {
       
    	return boardRepo.findAll();
    }
    //게시판 글
    public Board getBoard(Long id) throws SQLException {
       
    	return boardRepo.findById(id).get();
    }
    
    //게시판 리스트 (특정 작성자)
    public List<Board> getBoardListBySomeone(String fullnumber) throws SQLException {
        
    	return boardRepo.findBoardListBySomeone( fullnumber);
    }
    
    
    
    
    //글 업로드
    @Transactional
    public void insertBoard(Board board) throws SQLException {
      
    	boardRepo.save(board);
    }
    
    
    //글 수정
    @Transactional
    public void updateBoard(Board board) throws SQLException {
    
    	  boardRepo.save(board);
        }
    
    //글 삭제
    @Transactional
    public void deleteBoard( Long id) throws SQLException {
      
    	Board board= boardRepo.findById(id).get();

        if (board.getBoardId()!=null) {

        
                boardRepo.delete(board);
               
            } 
        }
    
    //===================댓글
    public List<BoardRe> getBoardRe(Long id) throws SQLException {
        
    	return boardReRepo.findAllById(id);
    }
   
    
    
    @Transactional
    public void  insertBoardRe(BoardRe boardRe) throws SQLException {
      
    	boardReRepo.save( boardRe);
    }
    
    
    
}