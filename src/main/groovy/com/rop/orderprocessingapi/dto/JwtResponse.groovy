package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical

@Canonical
class JwtResponse implements Serializable {
    private final String jwttoken

    JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken
    }

    String getToken() {
        return this.jwttoken
    }
}