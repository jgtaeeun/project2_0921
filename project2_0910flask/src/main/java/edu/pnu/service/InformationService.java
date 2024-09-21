package edu.pnu.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.pnu.domain.ImageDataDTO;
import edu.pnu.domain.InputImages;
import edu.pnu.domain.OutputImages;
import edu.pnu.domain.TimeDataset;
import edu.pnu.persistence.InputImagesRepository;
import edu.pnu.persistence.OutputImagesRepository;
import edu.pnu.persistence.TimeDatasetRepository;

@Service
public class InformationService {

	
	
	 @Autowired
	 private  TimeDatasetRepository  timeDatasetRepo;
	 
	
	 @Autowired
	 private InputImagesRepository inputImageRepository;

	 @Autowired
	 private OutputImagesRepository outputImageRepository;

	 
	 private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 
	 
	//관리자 모든차량 입출차
	public ImageDataDTO	getCarinoutAll(){
		  List<InputImages> inputImages = inputImageRepository.findAllFinishFlask();
	        List<OutputImages> outputImages = outputImageRepository.findAllFinishFlask();
	        
	        // Create DTO object
	        ImageDataDTO imageDataDTO = new ImageDataDTO(inputImages, outputImages);
	        return  imageDataDTO;
	}
	
	

	   //해당차주 입출차
	public List<TimeDataset> 	getCarinout(String fullnumber){
			return  timeDatasetRepo.getCarinout(fullnumber);
		}
		
		
		
	 
	//년
	public int countImagesForYear(int year) {
		        String datePrefix = year + "-";
		        return countImagesByDatePrefix(datePrefix);
	 }

	//년, 월
	public int countImagesForMonth(int year, int month) {
		        String datePrefix = String.format("%d-%02d-", year, month);
		        return countImagesByDatePrefix(datePrefix);
	}

	
	
	 private int countImagesByDatePrefix(String datePrefix) {
		        List<InputImages> inputImages = inputImageRepository.findByNameStartingWith(datePrefix);
		        List<OutputImages> outputImages = outputImageRepository.findByNameStartingWith(datePrefix);
		        return inputImages.size() + outputImages.size();
	 }
	
//---------------------
	 //주

	  private int countImagesInDateRange(LocalDateTime startDateTime, LocalDateTime endDateTime) {
	        String startPrefix = startDateTime.format(DATE_FORMATTER);
	        String endPrefix = endDateTime.format(DATE_FORMATTER);

	        // Find images that have timestamps within the date range
	        List<InputImages> inputImages = inputImageRepository.findByDateRange(startPrefix, endPrefix);
	        List<OutputImages> outputImages = outputImageRepository.findByDateRange(startPrefix, endPrefix);

	        return inputImages.size() + outputImages.size();
	    }

	    public Map<String, String> countImagesForYearAndMonth(int year, int month) {
	        Map<String, String> rangeCounts = new HashMap<>();

	        // Determine the start and end of the month
	        LocalDate startOfMonth = LocalDate.of(year, month, 1);
	        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

	        // Define date ranges with LocalDateTime
	        LocalDateTime range1Start = startOfMonth.atStartOfDay();
	        LocalDateTime range1End = startOfMonth.plusDays(9).atTime(23, 59, 59);

	        LocalDateTime range2Start = startOfMonth.plusDays(10).atStartOfDay();
	        LocalDateTime range2End = startOfMonth.plusDays(19).atTime(23, 59, 59);

	        LocalDateTime range3Start = startOfMonth.plusDays(20).atStartOfDay();
	        LocalDateTime range3End = endOfMonth.atTime(23, 59, 59);

	        // Count images in each range
	        rangeCounts.put("1-10", Integer.toString(countImagesInDateRange(range1Start, range1End)));
	        rangeCounts.put("11-20", Integer.toString(countImagesInDateRange(range2Start, range2End)));
	        rangeCounts.put("21-31", Integer.toString(countImagesInDateRange(range3Start, range3End)));

	        return rangeCounts;
	    }
	    

	   
	    
	   



		public boolean updateCarList(Long id,String fullnumber ) {
			// TODO Auto-generated method stub
			OutputImages o= outputImageRepository.findByIdForUpdate(id);
			if (o !=null) {
				o.setFullnumber(fullnumber);
				o.setRecognize(o.getRecognize()+"A");
				outputImageRepository.save(o);
				return true;
			}
			else {
				return false;
			}
		}
		public boolean updateCarList2(Long id,String fullnumber ) {
			// TODO Auto-generated method stub
			InputImages o= inputImageRepository.findByIdForUpdate(id);
			if (o !=null) {
				o.setFullnumber(fullnumber);
				o.setRecognize(o.getRecognize()+"A");
				inputImageRepository.save(o);
				return true;
			}
			else {
				return false;
			}
		}
}