package edu.pnu.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import edu.pnu.domain.OutputImages;
import edu.pnu.domain.TimeDataset;



public interface TimeDatasetRepository  extends JpaRepository<TimeDataset, Long> {
	//해당차주 입출차
	@Query("SELECT m FROM TimeDataset m WHERE m.member.username = ?1")
    List<TimeDataset> getCarinout(String fullnumber);
    
	 
}