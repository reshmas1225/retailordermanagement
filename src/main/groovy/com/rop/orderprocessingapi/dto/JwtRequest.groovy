package com.rop.orderprocessingapi.dto

import groovy.transform.Canonical

@Canonical
class JwtRequest implements Serializable {

    private String username
    private String password

    //need default constructor for JSON Parsing
    JwtRequest() {

    }

    JwtRequest(String username, String password) {
        this.setUsername(username)
        this.setPassword(password)
    }

    String getUsername() {
        return this.username
    }

    void setUsername(String username) {
        this.username = username
    }

    String getPassword() {
        return this.password
    }

    void setPassword(String password) {
        this.password = password
    }
}