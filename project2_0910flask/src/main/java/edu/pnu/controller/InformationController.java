package edu.pnu.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.pnu.domain.InputImages;
import edu.pnu.domain.Member;
import edu.pnu.domain.OutputImages;
import edu.pnu.domain.TimeDataset;
import edu.pnu.persistence.InputImagesRepository;
import edu.pnu.persistence.OutputImagesRepository;
import edu.pnu.service.InformationService;
import edu.pnu.service.MemberService;

@RestController
@RequestMapping("/member")
public class InformationController {
	@Autowired
	private InformationService informationService;

	
	 @Autowired
	 private InputImagesRepository inputImageRepository;

	 @Autowired
	 private OutputImagesRepository outputImageRepository;
	 
	 
	 private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	 
	 
	 @GetMapping("/inout/car")
	    public ResponseEntity<List<TimeDataset>> getCarinout(@RequestParam String fullnumber ) {
	        // 현재 로그인한 사용자 정보 가져오기
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String currentUsername = null;
	        
	        // 인증된 사용자 정보가 UserDetails 인스턴스일 경우
	        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            currentUsername = userDetails.getUsername();
	        }

	        // 로그인한 사용자와 차량 소유주 확인 (가정: 차량 소유주는 username으로 식별됨)
	        boolean isAuthorized = currentUsername != null && currentUsername.equals(fullnumber);
	        
	        if (!isAuthorized) {
	            return ResponseEntity.status(403).build(); // 권한이 없으면 403 Forbidden 상태 반환
	        }
	        
	        // 차량 입출차 기록 조회
	        List<TimeDataset> carInout = informationService.getCarinout(fullnumber);
	        
	      
	        
	        return ResponseEntity.ok(carInout);
	    }
	
	
	 //통계자료(년, 월, 주, 시간)
	 @GetMapping("/graph")
	 public ResponseEntity<List<Map<String, String>>> getDateForGraph( 
			 @RequestParam  String  year , 
			 @RequestParam (required = false)  String month){
		
		 
		 	List<Map<String, String>> regionsMapList = new ArrayList<>();
		 	
		 	if (year != null && month == null ) {
		 	    int endYear = Integer.parseInt(year);

		 	    // Process images by year
		 	    for (int i = 2018; i <= endYear; i++) {
		 	    	
		 	        Map<String, String> yearMap = new HashMap<>();
		 	        yearMap.put("year_" + i, Integer.toString(informationService.countImagesForYear(i)));
		 	        regionsMapList.add(yearMap);
		 	    }

		 	    // Process images by month for the specified year
		 	    for (int i = 1; i <= 12; i++) {
		 	     
		 	    	int j = Integer.parseInt(year);
		 	        // Create a map for the month with the combined list size
		 	        Map<String, String> monthMap = new HashMap<>();
		 	        monthMap.put(year + "_" +i, Integer.toString( informationService.countImagesForMonth(j, i)));
		 	        regionsMapList.add(monthMap);
		 	    }
		 	}
		 	

	 		
	 		else if (year != null && month != null ) {                         //2018 07 첫째주
	 			int i = Integer.parseInt(year);
	 			int j = Integer.parseInt(month);
	 			regionsMapList.add(informationService.countImagesForYearAndMonth(i, j));
	 			
	 			 		
	 		}
		 	 
	 		
	 		else {
	 			 return null;
	 		}
	 		
	 		return ResponseEntity.ok(regionsMapList);
   }
	 
	 
//	 @GetMapping("/graph/hours")
//	    public ResponseEntity<Map<String, Long>> getHourForGraph(
//	            @RequestParam String year,
//	            @RequestParam String month,
//	            @RequestParam String day) {
//
//	        // Ensure month and day are zero-padded to always be two digits
//	        String formattedMonth = String.format("%02d", Integer.parseInt(month));
//	        String formattedDay = String.format("%02d", Integer.parseInt(day));
//
//	        // Construct the date string and parse it
//	        String dateString = String.format("%s-%s-%s", year, formattedMonth, formattedDay);
//	        LocalDate parsedDate = LocalDate.parse(dateString, DATE_FORMATTER);
//
//	        // Prepare map to hold counts for each hour
//	        Map<String, Long> hourCounts = new HashMap<>();
//
//	        // Initialize hour counts for each hour of the day
//	        for (int i = 0; i < 24; i++) {
//	            hourCounts.put(String.format("%02d:00 - %02d:59", i, i), 0L);
//	        }
//
//	        // Count images for each hour
//	        countImagesForHour(parsedDate, hourCounts);
//
//	        return ResponseEntity.ok(hourCounts);
//	    }
	 @GetMapping("/graph/hours")
	 public ResponseEntity<Map<String, Long>> getHourForGraph(
	         @RequestParam String year,
	         @RequestParam String month,
	         @RequestParam String day) {

	     // Ensure month and day are zero-padded to always be two digits
	     String formattedMonth = String.format("%02d", Integer.parseInt(month));
	     String formattedDay = String.format("%02d", Integer.parseInt(day));

	     // Construct the date string and parse it
	     String dateString = String.format("%s-%s-%s", year, formattedMonth, formattedDay);
	     LocalDate parsedDate = LocalDate.parse(dateString, DATE_FORMATTER);

	     // Prepare a sorted list of hour ranges
	     List<String> hourRanges = new ArrayList<>();
	     for (int i = 0; i < 24; i++) {
	         hourRanges.add(String.format("%02d:00 - %02d:59", i, i));
	     }

	     // Initialize the map with zeros
	     Map<String, Long> hourCounts = new LinkedHashMap<>();
	     for (String hourRange : hourRanges) {
	         hourCounts.put(hourRange, 0L);
	     }

	     // Count images for each hour
	     countImagesForHour(parsedDate, hourCounts);

	     return ResponseEntity.ok(hourCounts);
	 }


	    private void countImagesForHour(LocalDate date, Map<String, Long> hourCounts) {
	        for (int i = 0; i < 24; i++) {
	            // Build date-time strings to match filenames
	            String dateTimePrefix = String.format("%s-%s-%s_%02d", date.getYear(), String.format("%02d", date.getMonthValue()), String.format("%02d", date.getDayOfMonth()), i);

	            // Query for images with this date-time prefix
	            List<InputImages> inputImages = inputImageRepository.findByDateTime(dateTimePrefix);
	            List<OutputImages> outputImages = outputImageRepository.findByDateTime(dateTimePrefix);

	            long count = inputImages.size() + outputImages.size();
	            hourCounts.put(String.format("%02d:00 - %02d:59", i, i), count);
	        }
	    }


	
}