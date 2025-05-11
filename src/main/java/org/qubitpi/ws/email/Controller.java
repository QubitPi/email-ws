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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link Controller} is responsible for all email operations, such as sending emails.
 */
@RestController
@RequestMapping("/email")
public class Controller {

    @Value("${receiver.email}")
    private String receiverEmailAddress;

    @Value("${spring.mail.username}")
    private String senderEmailAddress;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * Endpoint for sending emails.
     *
     * @param email  The model that includes all information of an email.
     *
     * @return the result of sending the email
     */
    @PostMapping(value = "/send", produces = "application/json")
    ResponseEntity<String> send(@RequestBody final Email email) {
        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmailAddress);
        message.setTo(receiverEmailAddress);
        message.setSubject(email.title);
        message.setText(email.body);
        javaMailSender.send(message);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
