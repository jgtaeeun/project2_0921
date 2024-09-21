package edu.pnu.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.nimbusds.jose.shaded.gson.Gson;

import edu.pnu.domain.ImageDataDTO;
import edu.pnu.domain.InputImages;
import edu.pnu.domain.OutputImages;
import edu.pnu.persistence.InputImagesRepository;
import edu.pnu.persistence.OutputImagesRepository;



@Service
public class DataScheduler {

    @Autowired
    private InputImagesRepository inputImagesRepository;
    @Autowired
    private OutputImagesRepository outputImagesRepository;
    
    @Autowired
    private WebSocketService webSocketService;
    
    private static final String FLASK_URL = "http://Tkflsns001.iptime.org:8099/upload";
    private final RestTemplate restTemplate = new RestTemplate();

    
 //   @Scheduled(fixedRate = 30000) 
    public void sendDataToFlask() {
        // DB에서 데이터 조회
       
        try {
        	
            List<InputImages>outputImages =inputImagesRepository.findByStatusFalse();
  	
            if (outputImages.isEmpty() ) {
          	   System.out.println("No images found.");
  	            return;
            }
            
        
                InputImages image = outputImages.get(0);
                File file = new File(image.getPath());

                if (!file.exists()) {
              	  System.out.println("File not found: " + image.getPath());
              	  return;
                }
            
              	byte[] fileContent = readFileToByteArray(file);
        	        ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent) {
                        @Override
                        public String getFilename() {
                            return file.getName();
                        }
                    };
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                	
                    
                    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                    body.add("file", byteArrayResource);

                    
                    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                    // Send request
                    ResponseEntity<FlaskResponse> response = restTemplate.exchange(
                        FLASK_URL,
                        HttpMethod.POST,
                        requestEntity,
                        FlaskResponse.class
                    );

                    
                    if (response.getStatusCode().is2xxSuccessful()) {
                    	  FlaskResponse flaskResponse =response.getBody();
                    	if (flaskResponse.getImage() != null) {
                            String base64Image = flaskResponse.getImage();
                            String textData = flaskResponse.getText();
                            String accuracy = flaskResponse.getScore();
                            // Save base64 image and update ImagePath object
                            saveBase64Image(base64Image, "C:\\uploads\\number\\" + removeFileExtension(image.getName()) + "_Analysis.jpg");
                            image.setFullnumber(textData);
                            image.setName2(removeFileExtension(image.getName()) + "_Analysis.jpg");
                            image.setPath2("C:\\uploads\\number\\" + image.getName2());
                            
                            
                           
                            // Handle failure case if Flask server does not process successfully
                            image.setRecognize(accuracy); // Set a failure status
                            image.setCartype(getRandomCarType());
                            
                            image.setStatus(true);
                            inputImagesRepository.save(image);
                         
                        }
                    } else {
        	            System.out.println("Failed to communicate with Flask server. Status code: " + response.getStatusCode());
        	        }

                    
                
            

  	  } catch (IOException e) {
            e.printStackTrace();
        }
      
      
        //  sendImagesToFrontEnd() ;
      }
            	
    
 //@Scheduled(fixedRate = 30000) // 30초마다 실행
  public void sendDataToFlask2() {
      // DB에서 데이터 조회
     
      try {
      	
          List<OutputImages>outputImages =outputImagesRepository.findByStatusFalse();
	
          if (outputImages.isEmpty() ) {
        	   System.out.println("No images found.");
	            return;
          }
          
      
              OutputImages image = outputImages.get(0);
              File file = new File(image.getPath());

              if (!file.exists()) {
            	  System.out.println("File not found: " + image.getPath());
            	  return;
              }
          
            	byte[] fileContent = readFileToByteArray(file);
      	        ByteArrayResource byteArrayResource = new ByteArrayResource(fileContent) {
                      @Override
                      public String getFilename() {
                          return file.getName();
                      }
                  };
                  
                  HttpHeaders headers = new HttpHeaders();
                  headers.setContentType(MediaType.MULTIPART_FORM_DATA);
              	
                  
                  MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                  body.add("file", byteArrayResource);

                  
                  HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

                  // Send request
                  ResponseEntity<FlaskResponse> response = restTemplate.exchange(
                      FLASK_URL,
                      HttpMethod.POST,
                      requestEntity,
                      FlaskResponse.class
                  );

                  
                  if (response.getStatusCode().is2xxSuccessful()) {
                  	  FlaskResponse flaskResponse =response.getBody();
                  	if (flaskResponse.getImage() != null) {
                          String base64Image = flaskResponse.getImage();
                          String textData = flaskResponse.getText();
                          String accuracy = flaskResponse.getScore();
                          // Save base64 image and update ImagePath object
                          saveBase64Image(base64Image, "C:\\uploads\\number\\" + removeFileExtension(image.getName()) + "_Analysis.jpg");
                          image.setFullnumber(textData);
                          image.setName2(removeFileExtension(image.getName()) + "_Analysis.jpg");
                          image.setPath2("C:\\uploads\\number\\" + image.getName2());
                          
                          
                         
                          // Handle failure case if Flask server does not process successfully
                          image.setRecognize(accuracy); // Set a failure status
                          image.setCartype(getRandomCarType());
                          
                          image.setStatus(true);
                          outputImagesRepository.save(image);
                       
                      }
                  } else {
      	            System.out.println("Failed to communicate with Flask server. Status code: " + response.getStatusCode());
      	        }

                  
              
          

	  } catch (IOException e) {
          e.printStackTrace();
      }
    
    
      //  sendImagesToFrontEnd() ;
    }
          	
//        //파일을 바이트로 변환
        private byte[] readFileToByteArray(File file) throws IOException {
            try (FileInputStream fis = new FileInputStream(file)) {
                return fis.readAllBytes();
            }
        }
        
        //확장자제거하고 name2만들기
        public static String removeFileExtension(String fileName) {
            int lastDotIndex = fileName.lastIndexOf(".");
            if (lastDotIndex > 0) {
                return fileName.substring(0, lastDotIndex);
            }
            return fileName; // 확장자가 없으면 원래 파일 이름을 반환
        }
        
        
        
        //base64인코딩
        private static void saveBase64Image(String base64Image, String outputFilePath) throws FileNotFoundException, IOException {
            // Base64 디코딩
        	   byte[] imageBytes = Base64.getDecoder().decode(base64Image);
               File outputFile = new File(outputFilePath);
               outputFile.getParentFile().mkdirs(); // Ensure directory exists

               try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                   fos.write(imageBytes);
               }
        }
        
        
        
        
    
        
       

       
        
  @Scheduled(fixedRate = 30000) 
    public void sendImagesToFrontEnd() {
        // Retrieve data from repositories
        List<InputImages> inputImages = inputImagesRepository.findAllFinishFlask();
        List<OutputImages> outputImages = outputImagesRepository.findAllFinishFlask();
        
        // Create DTO object
        ImageDataDTO imageDataDTO = new ImageDataDTO(inputImages, outputImages);
        
        // Convert DTO object to JSON string
        String json = new Gson().toJson(imageDataDTO);
        
        // Send JSON string to the front end
        webSocketService.sendMessage(json);
    }
    
    

    
    //차량 종류 랜덤 설정
    public String getRandomCarType() {
    	List<String> carTypes = new ArrayList<>();
    	carTypes.add("1톤트럭");
        carTypes.add("5톤트럭");
        carTypes.add("10톤트럭");
        carTypes.add("승용차");
        carTypes.add("기타");
        
        Random random = new Random();
        int randomIndex = random.nextInt(carTypes.size());
        return carTypes.get(randomIndex);
    }
    
        //프론트-백엔드 스케줄링 test용
//        @Scheduled(fixedRate = 30000) // 30초마다 실행
//        public void sendDataTest() {
//        	ImagePath p=new ImagePath();
//        	p.setFullnumber("test2");
//        	p.setName("name2");
//        	p.setName2("name22");
//        	p.setPath("path2");
//        	p.setPath2("path22");
//        	p.setRecognize("인식성공100");
//        	p.setStatus(true);
//        	
//        	fileRepository.save(p);
//        	 List<ImagePath> dataList = fileRepository.findAll();
//        	 
//             webSocketService.sendMessage(new Gson().toJson(dataList));
//        }
}