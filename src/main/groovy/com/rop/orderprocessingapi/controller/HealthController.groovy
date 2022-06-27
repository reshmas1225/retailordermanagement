package com.rop.orderprocessingapi.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HealthController {

    @GetMapping("/health")
    ResponseEntity health() {
        ResponseEntity.status(HttpStatus.OK).body("orderprocessingapi is up and running")
    }
}
