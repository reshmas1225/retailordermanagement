package com.rop.orderprocessingapi.controller

import com.rop.orderprocessingapi.config.JwtTokenUtil
import com.rop.orderprocessingapi.dto.JwtRequest
import com.rop.orderprocessingapi.dto.JwtResponse
import com.rop.orderprocessingapi.service.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager

    @Autowired
    private JwtTokenUtil jwtTokenUtil

    @Autowired
    private JwtUserDetailsService userDetailsService

    /**
     * Create and Fetches the token
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword())

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername())

        final String token = jwtTokenUtil.generateToken(userDetails)

        return ResponseEntity.ok(new JwtResponse(token))
    }

    /**
     * Gets username and password from body using Spring Authentication Manager and authenticates
     * @param username
     * @param password
     * @throws Exception
     */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e)
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e)
        }
    }
}