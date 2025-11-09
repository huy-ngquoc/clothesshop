package vn.uit.clothesshop.recommendation.util;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import io.github.cdimascio.dotenv.Dotenv;
import vn.uit.clothesshop.recommendation.domain.Model;

public class ApiCall {
    private static final Dotenv dotenv = Dotenv.load();
    public static Model callApi(MultipartFile multipartFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
         ByteArrayResource fileResource = new ByteArrayResource(multipartFile.getBytes()) {
        @Override
        public String getFilename() {
            return multipartFile.getOriginalFilename();
        }
    };
    body.add("file",fileResource);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(factory);
        String pythonURL = dotenv.get("RECOMMENDATION_SERVER");
        ResponseEntity<Model> result = restTemplate.postForEntity(pythonURL,requestEntity,Model.class);
        Model data= result.getBody();
        return data;
    }
}
