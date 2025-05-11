/*
 * Copyright 2025 Jiaqi Liu. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.qubitpi.ws.email;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

/**
 * The entity webservice integration tests.
 */
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationIT {

    @Container
    private static final GenericContainer SMTP_SERVER = new GenericContainer(
            DockerImageName.parse("jack20191124/mailhog:latest")
    )
            .withExposedPorts(8025)
            .withExposedPorts(1025);

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private Controller controller;

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * Dynamically set ArangoDB container connection info.
     *
     * @param registry  {@code application.properties} mutator at runtime
     */
    @DynamicPropertySource
    static void registerPgProperties(final DynamicPropertyRegistry registry) {
        registry.add(
                "spring.mail.port",
                () -> SMTP_SERVER.getMappedPort(1025)
        );
    }

    /**
     * Make sure the context is creating controller.
     */
    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    /**
     * Make sure actuator is working properly.
     */
    @Test
    void testHealthcheck() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + this.port + "/actuator/health", String.class))
                .isEqualTo("{\"status\":\"UP\"}");
    }

    /**
     * Making sure email can be sent.
     */
    @Test
    void send() {
        final Email email = new Email();
        email.title = "my title";
        email.body = "my body";

        assertThat(
                this.restTemplate.postForObject(
                        "http://localhost:" + port + "/email/send",
                        new HttpEntity<>(email),
                        String.class
                )).isEqualTo("Success");
    }
}
