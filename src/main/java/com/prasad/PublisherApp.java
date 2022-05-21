package com.prasad;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.UUID;

@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class PublisherApp {

    private Logger logger = LoggerFactory.getLogger(PublisherApp.class);

    @Autowired
    private MessageChannel output;

    @PostMapping("/publish")
    public String publishEvent(@RequestBody EnrichmentJob enrichmentJob) {
        logger.info("Generation correlationID");
        enrichmentJob.setCorrelationID(UUID.randomUUID().toString());
        output.send(MessageBuilder.withPayload(enrichmentJob).build());
        logger.info("Batch job published : " + enrichmentJob.toString());
        return ("Batch job published");
    }

    @RequestMapping(value="/healthcheck",method = RequestMethod.GET, produces={MediaType.TEXT_PLAIN_VALUE})
    @ResponseBody
    public ResponseEntity<String> healthcheck() {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String responseText = "Publisher Healthcheck @ "+ timeStamp+" - All OK";
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.TEXT_PLAIN).body(responseText.toString());
    }

    public static void main(String[] args) {
        SpringApplication.run(PublisherApp.class, args);
    }
}
