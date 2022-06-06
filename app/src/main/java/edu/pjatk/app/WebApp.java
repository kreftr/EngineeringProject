package edu.pjatk.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
public class WebApp {

    private static final Logger LOGGER = LogManager.getLogger(WebApp.class);

    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);

        LOGGER.info("Info level log message");
        LOGGER.error("Error level log message");
    }
}
