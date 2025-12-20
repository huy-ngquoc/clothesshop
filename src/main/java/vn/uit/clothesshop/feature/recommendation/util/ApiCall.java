package vn.uit.clothesshop.feature.recommendation.util;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import vn.uit.clothesshop.feature.recommendation.domain.Model;

public class ApiCall {
    private static final Dotenv dotenv = Dotenv.load();

    public static Model callApi(MultipartFile multipartFile) throws IOException {
    // 1. Cấu hình Factory để ÉP BUỘC tính Content-Length
    SimpleClientHttpRequestFactory simpleFactory = new SimpleClientHttpRequestFactory();
    simpleFactory.setBufferRequestBody(true); // Buffer body để tính độ dài
    
    // Bọc trong BufferingClientHttpRequestFactory để chắc chắn không dùng chunked
    BufferingClientHttpRequestFactory bufferingFactory = new BufferingClientHttpRequestFactory(simpleFactory);
    
    RestTemplate restTemplate = new RestTemplate(bufferingFactory);

    // 2. Setup Headers
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA); // Spring sẽ tự thêm boundary vào sau

    // 3. Setup Body (Phần này bạn làm đúng rồi, nhưng nhớ set ContentType cho file)
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    
    ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
        @Override
        public String getFilename() {
            return multipartFile.getOriginalFilename();
        }
    };

    // Tạo Header riêng cho phần File để Python hiểu đây là ảnh
    HttpHeaders fileHeaders = new HttpHeaders();
    fileHeaders.setContentType(MediaType.parseMediaType(multipartFile.getContentType())); // Ví dụ: image/jpeg
    HttpEntity<ByteArrayResource> fileEntity = new HttpEntity<>(fileResource, fileHeaders);

    body.add("file", fileEntity);

    // 4. Gửi Request
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    String pythonURL = dotenv.get("RECOMMENDATION_SERVER");
    
    ResponseEntity<Model> result = restTemplate.postForEntity(pythonURL, requestEntity, Model.class);
    return result.getBody();
}
}
