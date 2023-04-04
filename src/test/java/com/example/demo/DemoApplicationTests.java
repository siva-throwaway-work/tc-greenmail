package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Container
    private static GenericContainer greenMailContainer = new GenericContainer<>(
            DockerImageName.parse("greenmail/standalone:2.0.0"))
            .withEnv("GREENMAIL_OPTS",
                    "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=name:password")
            .withExposedPorts(3025);

    @DynamicPropertySource
    public static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", greenMailContainer::getHost);
        registry.add("spring.mail.port", () -> greenMailContainer.getMappedPort(3025));
        registry.add("spring.mail.username", () -> "name");
        registry.add("spring.mail.password", () -> "password");
    }
    
    @Test
    void contextLoads() {
    }

}
