package edu.pnu.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.InputImages;

public interface InputImagesRepository extends JpaRepository<InputImages, Long> {
	
	@Query("select i from InputImages i where i.fullnumber=?1")
	public InputImages findByFullnumber(String i);
	
	@Query("select i from InputImages i where i.fullnumber=?1 and i.pair=false")
	public List<InputImages> findByFullnumber2(String i);
	
	
	List<InputImages> findByNameStartingWith(String datePrefix);
	
	
	@Query("select i from InputImages i where i.id=?1")
	InputImages findByIdForUpdate(Long id);
	

	
	 List<InputImages> findByStatusFalse();
	 
	 
	 @Query("SELECT i FROM InputImages i WHERE i.name BETWEEN :startDate AND :endDate")
	    List<InputImages> findByDateRange(@Param("startDate") String  startDate, @Param("endDate")String endDate);
	 
	 @Query("SELECT i FROM InputImages i WHERE SUBSTRING(i.name, 1, 13) = :dateTime")
	    List<InputImages> findByDateTime(@Param("dateTime") String dateTime);
	 
	 @Query("SELECT i FROM InputImages i WHERE i.status=true")
	 List<InputImages> findAllFinishFlask();
}