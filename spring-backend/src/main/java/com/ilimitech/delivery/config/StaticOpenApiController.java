package com.ilimitech.delivery.config;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class StaticOpenApiController {

    @GetMapping(value = "/v3/api-docs.yaml", produces = "text/yaml")
    public ResponseEntity<String> openApiYaml() throws IOException {
        ClassPathResource res = new ClassPathResource("static/openapi.yml");
        if (!res.exists()) {
            return ResponseEntity.notFound().build();
        }
        String yaml = StreamUtils.copyToString(res.getInputStream(), StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/yaml; charset=UTF-8"));
        return ResponseEntity.ok().headers(headers).body(yaml);
    }
}

