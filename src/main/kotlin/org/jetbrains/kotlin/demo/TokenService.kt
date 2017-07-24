package org.jetbrains.kotlin.demo
import org.springframework.security.core.Authentication

import java.util.UUID
import java.util.Random
import sun.misc.BASE64Encoder
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicLong
import org.springframework.scheduling.annotation.Scheduled

class TokenService {
   companion object{
      const val Expiration = 60*60*12L
   }

 @JvmField val expiryQueue = DelayQueue<TokenExpiry>()
  

@JvmField var tokens = hashMapOf<String,Authentication>()
@JvmField var users = hashMapOf<String,String>()
    fun generateNewToken():String {
        val genStr = ""+System.currentTimeMillis()+UUID.randomUUID().toString().replace("-","")
        return BASE64Encoder().encodeBuffer(genStr.toByteArray()).replace("\\s+".toRegex(),"")
    }

    public fun store(user:String,token:String, authentication:Authentication) {
          flush()
       
          val oldToken = users.get(user)
          if(oldToken != null){
             tokens.remove(oldToken)
             storeExpiry(oldToken,token)
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

    @Scheduled(fixedDelay = Expiration)
    fun flush(){
       var expiry = expiryQueue.poll()
        while (expiry != null) {
           tokens.remove(expiry.value)
           expiry = expiryQueue.poll()
        }
    }

    fun storeExpiry(oldtoken:String,token:String){
       var oldexpiry = TokenExpiry(oldtoken, Expiration)
       var expiry = TokenExpiry(token, Expiration)
       expiryQueue.remove(oldexpiry)
       expiryQueue.remove(expiry)
       expiryQueue.put(expiry)
    }
}
