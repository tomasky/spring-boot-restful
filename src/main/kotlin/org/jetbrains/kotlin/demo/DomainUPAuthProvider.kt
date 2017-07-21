package org.jetbrains.kotlin.demo

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.authority.AuthorityUtils
import org.slf4j.LoggerFactory
class DomainUPAuthProvider:AuthenticationProvider {

    var tokenService:TokenService 

    constructor(tokenService:TokenService) {
        this.tokenService = tokenService
    }

   override public  fun authenticate(authentication:Authentication ):Authentication? {
        val  username =  authentication.getPrincipal() as String 
        val  password =  authentication.getCredentials()

        if (!"user".equals(username) || !"password".equals(password )) {
            throw BadCredentialsException("Invalid username or password") 
        }
        val  resultOfAuthentication =  AuthenticationWithToken(username, null,AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"))
        val newToken = tokenService.generateNewToken()
        resultOfAuthentication.setToken(newToken)
        tokenService.store(username,newToken, resultOfAuthentication)

        return resultOfAuthentication
    }

    override public fun supports(authentication:Class<*>):Boolean  {
        return authentication.equals(UsernamePasswordAuthenticationToken::class.java)
    }
}
