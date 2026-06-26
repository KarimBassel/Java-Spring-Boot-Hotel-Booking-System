package com.hotel.booking.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class VersionController {

    @Value("${RENDER_GIT_COMMIT:dev}")
    private String gitSha;

    @GetMapping("/version")
    public String version() {
        return gitSha;
    }
}
