package com.rop.orderprocessingapi.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Contains business operations associated with the user details endpoints resource.
 */

@Service
class JwtUserDetailsService implements UserDetailsService {

    @Value('${jwt.secret.username}')
    String secretKeyUser

    @Value('${jwt.secret.password}')
    String secretKeyPassword

    /**
     * Get User details
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (secretKeyUser == username) {
            return new User(username, secretKeyPassword, new ArrayList<>())
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username)
        }
    }
}
