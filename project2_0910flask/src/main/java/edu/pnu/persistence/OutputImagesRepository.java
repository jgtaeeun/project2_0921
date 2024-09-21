package edu.pnu.persistence;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import edu.pnu.domain.InputImages;
import edu.pnu.domain.OutputImages;

public interface OutputImagesRepository extends JpaRepository<OutputImages, Long> {
	
	  List<OutputImages> findByNameStartingWith(String datePrefix);
	  
	  @Query("select o from OutputImages o where o.id =?1")
	  OutputImages findByIdForUpdate(Long id);
	  
	  List<OutputImages > findByStatusFalse();
	  
	  @Query("SELECT o FROM OutputImages o WHERE o.name BETWEEN :startDate AND :endDate")
	    List<OutputImages> findByDateRange(@Param("startDate") String startDate, @Param("endDate")String  endDate);
	  
	  @Query("SELECT i FROM OutputImages i WHERE SUBSTRING(i.name, 1, 13) = :dateTime")
	    List<OutputImages> findByDateTime(@Param("dateTime") String dateTime);
	  
	  @Query("SELECT i FROM OutputImages i WHERE i.status=true")
		 List<OutputImages> findAllFinishFlask();
}
