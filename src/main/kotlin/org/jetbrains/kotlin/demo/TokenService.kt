package org.jetbrains.kotlin.demo
import org.springframework.security.core.Authentication

import java.util.UUID
import java.util.Random
import sun.misc.BASE64Encoder

class TokenService {

@JvmField var tokens = hashMapOf<String,Authentication>()
@JvmField var users = hashMapOf<String,String>()
    fun generateNewToken():String {
        val genStr = ""+System.currentTimeMillis()+UUID.randomUUID().toString().replace("-","")
        return BASE64Encoder().encodeBuffer(genStr.toByteArray()).replace("\\s+".toRegex(),"")
    }

    public fun store(user:String,token:String, authentication:Authentication) {
          val oldToken = users.get(user)
          if(oldToken != null){
             tokens.remove(oldToken)
          }
          users.put(user,token)

          tokens.put(token,authentication)
    }
    public fun contains(token:String):Boolean  {
       return tokens.contains(token) 
    }
    fun retrieve(token:String):Authentication ?  {
       return tokens.get(token) 
    }

}
