package com.prasad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class PublisherApp {
    @Autowired
    private MessageChannel output;

    @PostMapping("/publish")
    public String publishEvent(@RequestBody EnrichmentJob enrichmentJob) {
        output.send(MessageBuilder.withPayload(enrichmentJob).build());
        return ("Batch job published : " + enrichmentJob.toString());
    }

    @RequestMapping(value="/healthcheck",method = RequestMethod.GET, produces={MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public ResponseEntity<String> healthcheck() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String responseText = "Healthcheck @ "+ timeStamp+" - All OK";
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body(responseText.toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherApp.class, args);
    }
}
