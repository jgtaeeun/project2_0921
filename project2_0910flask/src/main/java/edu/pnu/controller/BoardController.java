package edu.pnu.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import edu.pnu.domain.Board;
import edu.pnu.domain.BoardRe;
//import edu.pnu.domain.BoardRe;
import edu.pnu.domain.Member;
import edu.pnu.persistence.BoardRepository;
//import edu.pnu.service.BoardReService;
import edu.pnu.service.BoardService;
import edu.pnu.service.FileService;
import edu.pnu.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RestController
@RequestMapping("/member")
public class BoardController {

	@Autowired
	private BoardService boardService;
	@Autowired
	private MemberService memberService;
	  
    @Autowired
    private BoardRepository  boardRepo;
    @Autowired
    private FileService fileService;
	
	@GetMapping("/community/search")
	public ResponseEntity<List<Board>> getBoardListBySomeone(@RequestParam String fullnumber) {
		
		try {
					List<Board> boards = boardService.getBoardListBySomeone(fullnumber);
					if (boards.isEmpty()) {
						return ResponseEntity.noContent().build();
					} else {
						return ResponseEntity.ok(boards);
					}
		}catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	

	@GetMapping("/community")
	public ResponseEntity<List<Board>> getBoardList() {
		try {
			List<Board> boards = boardService.getAllBoardList();
			List <Board> notice =new ArrayList <>() ;
			List<Board > community=new ArrayList <>() ;
			//관리자가 쓴 공지사항을 가장 위에 위치하도록
			for (Board b : boards) {
				if (b.getMember().getRole().name().equals("ROLE_ADMIN")) {
					notice.add(b);
				}
				else {
					community.add(b);
				}
			}
			
			notice.addAll(community);
			
			if (notice.isEmpty()) {
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.ok(notice);
			}
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/community/{id}")
	public ResponseEntity<Board> getBoard(@PathVariable Long id) {
		try {
			
			  Board board = boardService.getBoard(id);
			
			  
			  if (board.getMember().getRole().name().equals("ROLE_ADMIN")) { //관리자가 쓴 글은 접근제한 없이 member인 경우, 다 볼 수 있다.
				  board.setCount(board.getCount()+1);
				  boardRepo.save(board);
				  return ResponseEntity.ok(board);
				  
			  }else {
				  
				  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			      String currentUsername = null;
			        
			        // 인증된 사용자 정보가 UserDetails 인스턴스일 경우
			       if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			            currentUsername = userDetails.getUsername();
			            
			        }

			        // 로그인한 사용자와 차량 소유주 확인 (가정: 차량 소유주는 username으로 식별됨)
			        boolean isAuthorized = currentUsername != null && (currentUsername.equals( board.getMember().getUsername())||currentUsername.contains("admin"));
			        
			        if (!isAuthorized) {
			            return ResponseEntity.status(403).build(); // 권한이 없으면 403 Forbidden 상태 반환
			        }
			        else {
			        	  board.setCount(board.getCount()+1);
						  boardRepo.save(board);
			        	 return ResponseEntity.ok(board);
			        }
				  
			  }
			
		       
		
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
	}
	@PostMapping("/community")
	public ResponseEntity<Void> insertBoard(
	        @RequestParam("title") String title,
	        @RequestParam("content") String content,
	        @RequestParam(value = "files", required = false) MultipartFile[] files) { // Change 'file' to 'files'
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String currentUsername = userDetails.getUsername();

	        // Create and set up Board object
	        Board board = new Board();
	        board.setTitle(title);
	        board.setContent(content);
	        board.setCreateDate(new Date());
	        board.setCount(0L);

	        // File processing logic
	        if (files != null && files.length > 0) {
	            List<String> fileNames = new ArrayList<>();
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    String fileName = file.getOriginalFilename();
	                    fileNames.add(fileName);
	                    // File saving logic
	                    fileService.saveFile(file); // Ensure this method is properly implemented
	                }
	            }
	            // Save multiple file names (CSV format)
	            board.setFileName(String.join(",", fileNames));
	        }

	        // Set the Member associated with the current user
	        board.setMember(memberService.getMember(currentUsername));

	        // Save Board to the database
	        boardService.insertBoard(board);
	        return ResponseEntity.status(HttpStatus.CREATED).build();
	    } catch (Exception e) {
	        e.printStackTrace(); // Log the error details
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	@PutMapping("/community/{id}")
	public ResponseEntity<Void> updateBoard(
	        @PathVariable Long id,
	        @RequestParam("title") String title,
	        @RequestParam("content") String content,
	        @RequestParam(value = "files", required = false) MultipartFile[] files,
	        @RequestParam(value = "existingFiles", required = false) String[] existingFiles) throws IOException {
	    try {
	        // 게시물 존재 여부 확인
	        Board existingBoard = boardRepo.findById(id).orElse(null);
	        if (existingBoard == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        // 게시물 업데이트
	        existingBoard.setTitle(title);
	        existingBoard.setContent(content);
	        
	        // 기존 파일 처리
	        List<String> fileNames = new ArrayList<>();
	        if (existingFiles != null) {
	            fileNames.addAll(Arrays.asList(existingFiles)); // 기존 파일 이름 추가
	        }

	        // 새 파일 처리
	        if (files != null && files.length > 0) {
	            for (MultipartFile file : files) {
	                if (!file.isEmpty()) {
	                    String fileName = file.getOriginalFilename();
	                    fileNames.add(fileName);
	                    fileService.saveFile(file); // 파일 저장 로직
	                }
	            }
	        }

	        // 파일 이름 업데이트
	        existingBoard.setFileName(String.join(",", fileNames));

	        // 서비스 호출
	        boardService.updateBoard(existingBoard);
	        return ResponseEntity.ok().build();
	    } catch (SQLException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	@DeleteMapping("/community/{id}")
	public ResponseEntity<Void> deleteBoard( @PathVariable Long id) {
		 try {
	            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	            }

	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            String currentUsername = userDetails.getUsername();
	            
	            
	            Board board = boardService.getBoard(id);
	            
	            if (board.getMember().getUsername().equals(currentUsername)) {
	            	 boardService.deleteBoard(id);
	            	 return ResponseEntity.noContent().build();
	            }
	            else {
	            	return ResponseEntity.status(403).build(); 
	            }
	           
	            
	           
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	//-------댓글
	@GetMapping("/reply/{id}")
	public ResponseEntity <List<BoardRe>> getBoardRe(@PathVariable Long id) {
		try {
			
	            List<BoardRe> boardRe = boardService.getBoardRe(id);        //댓글목록
	            if ( boardRe !=null) {
				
	            	return ResponseEntity.ok( boardRe);
	            } else {
	            	return ResponseEntity.notFound().build();
	            }
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/reply/write/{id}")
	public ResponseEntity<Void> insertBoardRe(@PathVariable Long id,  @RequestParam("content") String content) {
	
		try {
			
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	            if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	            }

	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            String currentUsername = userDetails.getUsername();
	            
	            
	            BoardRe boardRe =new BoardRe();
	            
	            boardRe.setMember( memberService. getMember(currentUsername)); // Member 객체를 생성하여 설정 (Member 클래스를 적절히 구현)
	            boardRe.setCreateDate(new Date());
	            boardRe.setBoard(boardService.getBoard(id));
	            boardRe.setContent(content);
	            boardService.insertBoardRe(boardRe);
	            return ResponseEntity.status(HttpStatus.CREATED).build();
	            
	      
		} catch (SQLException e) {
			e.printStackTrace(); // 로그에 상세 오류를 기록합니다.
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	

}