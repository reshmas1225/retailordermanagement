package com.rop.orderprocessingapi.config

import com.rop.orderprocessingapi.service.JwtUserDetailsService
import groovy.util.logging.Slf4j
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Slf4j
class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService

    @Autowired
    private JwtTokenUtil jwtTokenUtil

    /**
     * To check if the request has a valid JWT token
     * @param request
     * @param response
     * @param chain
     * @throws ServletException* @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization")

        String username = null
        String jwtToken = null
        try {
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7)
                try {
                    username = jwtTokenUtil.getUsernameFromToken(jwtToken)
                } catch (IllegalArgumentException e) {
                    log.error("Unable to get JWT Token", e.message)
                } catch (ExpiredJwtException e) {
                    log.error("JWT Token has expired", e.message)
                }
            }
        } catch (Exception ex) {
            logger.error("Login Failed - could not process the login request due to the error - ", ex)
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username)

            // If token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities())
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request))
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken)
            }
        }
        chain.doFilter(request, response)
    }

}