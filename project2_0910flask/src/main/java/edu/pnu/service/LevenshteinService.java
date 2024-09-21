package edu.pnu.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import edu.pnu.domain.InputImages;
import edu.pnu.domain.Member;
import edu.pnu.domain.OutputImages;
import edu.pnu.domain.TimeDataset;
import edu.pnu.persistence.InputImagesRepository;
import edu.pnu.persistence.MemberRepository;
import edu.pnu.persistence.OutputImagesRepository;
import edu.pnu.persistence.TimeDatasetRepository;

@Service
public class LevenshteinService {

		@Autowired
	    private InputImagesRepository inputImagesRepository;
		@Autowired
	    private  OutputImagesRepository outputImagesRepository;
		@Autowired
		private TimeDatasetRepository timeDatasetRepository;
	 	@Autowired
	    private MemberRepository memberRepository;
	 	
	    public int calculateLevenshteinDistance(String s1, String s2) {
	        int m = s1.length();
	        int n = s2.length();
	        int[][] d = new int[m + 1][n + 1];

	        for (int i = 0; i <= m; i++) {
	            d[i][0] = i;
	        }
	        for (int j = 0; j <= n; j++) {
	            d[0][j] = j;
	        }

	        for (int i = 1; i <= m; i++) {
	            for (int j = 1; j <= n; j++) {
	                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
	                d[i][j] = Math.min(
	                        Math.min(d[i - 1][j] + 1, d[i][j - 1] + 1),
	                        d[i - 1][j - 1] + cost
	                );
	            }
	        }

	        return d[m][n];
	    }
	   // 계근대 이미지 유사도로 번호판 수정
	// @Scheduled(fixedRate = 60000)
	    public void findMostSimilarPlate() {
	        List<InputImages> inputImages = inputImagesRepository.findAll();
	        List<Member> plates = memberRepository.WithOUtAdmin();

	        for (InputImages i : inputImages) {
	            // Check if recognition is successful
	            if ("인식성공100".equals(i.getRecognize())) {
	                // Handle the case when recognition is successful (no similarity calculation needed)
	                handleSuccessfulRecognition(i);
	            } else if ("인식성공50".equals(i.getRecognize())){
	                // Handle the case when recognition is not successful (perform similarity calculation)
	                handleUnsuccessfulRecognition(i, plates);
	            }
	        }
	    }

	    private void handleSuccessfulRecognition(InputImages i) {
	        // If recognition is successful, perform any specific operations
	        // For demonstration, just saving the entity (modify as needed)
	        inputImagesRepository.save(i);
	    }

	    private void handleUnsuccessfulRecognition(InputImages i, List<Member> plates) {
	        String mostSimilar = null;
	        int minDistance = Integer.MAX_VALUE;

	        for (Member plate : plates) {
	            int distance = calculateLevenshteinDistance(i.getFullnumber(), plate.getUsername());
	            if (distance < minDistance) {
	                minDistance = distance;
	                mostSimilar = plate.getUsername();
	            }
	        }

	        // Ensure mostSimilar is not null
	        if (mostSimilar != null) {
	            i.setFullnumber(mostSimilar);
	   
	        }
	        inputImagesRepository.save(i);
	    }

//	  //계근대 이미지 유사도로 번호판 수정
	 //   @Scheduled(fixedRate = 60000)
	
	    public void findMostSimilarPlate2() {
	        List<OutputImages> outputImages = outputImagesRepository.findAll();
	        List<InputImages> inputImages = inputImagesRepository.findAll();

	        for (OutputImages i : outputImages) {
	            // Check if recognition is successful
	            if ("인식성공100".equals(i.getRecognize())) {
	                // Process the image without similarity calculation
	                processImageWithoutSimilarity(i, inputImages);
	            } else if ("인식성공50".equals(i.getRecognize())){
	                // Perform similarity calculation and process
	                processImageWithSimilarity(i, inputImages);
	            }
	        }
	    }

	    private void processImageWithoutSimilarity(OutputImages i, List<InputImages> inputImages) {
	        // Implement the logic for processing when recognition is successful
	        // Example: Directly use the image and update `InputImages` or `TimeDataset`
	        // You can adjust this logic as needed based on your requirements

	        // For demonstration, assume we set the first `InputImages` as the matched one
	        if (!inputImages.isEmpty()) {
	            InputImages final_i = inputImages.get(0); // Simplified example
	            TimeDataset t = new TimeDataset();
	            int dotIndex = final_i.getName().lastIndexOf('.');
	            int dotIndex2 = i.getName().lastIndexOf('.');

	            t.setTimein(final_i.getName().substring(0, dotIndex));
	            t.setTimeout(i.getName().substring(0, dotIndex2));
	            t.setRecognize(i.getRecognize());

	            // Ensure member is not null
	            Member member = memberRepository.findByUsername(final_i.getFullnumber());
	            if (member == null) {
	                throw new IllegalStateException("Member cannot be null for username: " + final_i.getFullnumber());
	            }
	            t.setMember(member);

	            // Ensure fullnumber is not null
	            if (i.getFullnumber() == null) {
	                throw new IllegalStateException("Fullnumber cannot be null");
	            }
	            t.setNumber(i.getFullnumber());

	            timeDatasetRepository.save(t);

	            final_i.setPair(true);
	            inputImagesRepository.save(final_i);
	        }
	    }

	    private void processImageWithSimilarity(OutputImages i, List<InputImages> inputImages) {
	        String mostSimilar = null;
	        int minDistance = Integer.MAX_VALUE;

	        for (InputImages plate : inputImages) {
	            int distance = calculateLevenshteinDistance(i.getFullnumber(), plate.getFullnumber());
	            if (distance < minDistance) {
	                minDistance = distance;
	                mostSimilar = plate.getFullnumber();
	            }
	        }

	        // Check if mostSimilar is valid
	        if (mostSimilar != null) {
	            InputImages final_i = inputImagesRepository.findByFullnumber2(mostSimilar).stream()
	                    .findFirst()
	                    .orElse(null);

	            if (final_i == null) {
	                // Log and skip if no matching InputImages found
	                System.out.println("No matching InputImages found for fullnumber: " + mostSimilar);
	                return;
	            }

	            TimeDataset t = new TimeDataset();
	            int dotIndex = final_i.getName().lastIndexOf('.');
	            int dotIndex2 = i.getName().lastIndexOf('.');

	            t.setTimein(final_i.getName().substring(0, dotIndex));
	            t.setTimeout(i.getName().substring(0, dotIndex2));
	            t.setRecognize(i.getRecognize());

	            // Ensure member is not null
	            Member member = memberRepository.findByUsername(mostSimilar);
	            if (member == null) {
	                // Log and skip if no matching Member found
	                System.out.println("Member cannot be null for username: " + mostSimilar);
	                return;
	            }
	            t.setMember(member);

	            // Ensure fullnumber is not null
	            if (i.getFullnumber() == null) {
	                throw new IllegalStateException("Fullnumber cannot be null");
	            }
	            t.setNumber(i.getFullnumber());

	            timeDatasetRepository.save(t);

	            final_i.setPair(true);
	            inputImagesRepository.save(final_i);
	        } else {
	            // Log if mostSimilar is null
	            System.out.println("No similar plate found for OutputImages with fullnumber: " + i.getFullnumber());
	        }
	    }



	    
	    //요소가 중복된지 확인
	    public static boolean checkForDuplicates(List<OutputImages> list) {
	        Set<OutputImages> seen = new HashSet<>();
	        for (OutputImages num : list) {
	            if (!seen.add(num)) {
	                // If add() returns false, it means the element was already present in the set
	                return true;
	            }
	        }
	        return false;
	    }
	    
	}