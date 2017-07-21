package org.jetbrains.kotlin.demo

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.concurrent.CompletableFuture
import org.springframework.stereotype.Component
import org.springframework.security.access.prepost.PreAuthorize 

@Service
class LookService {

   val  logger:Logger = LoggerFactory.getLogger("LookService")

   @Async("taskExecutor")
   fun  findUsers():CompletableFuture<User> {
      val results:User =User(0,"all users") 
      Thread.sleep(5000L)
      logger.info("Looking up " + results)
      return CompletableFuture.completedFuture(results)
   }

   /*@Async*/
   fun  findUserById(id:Long):DataResponse {
      var ret:Any = DataResponse(4100,"no data")
       val user = Domains.findById(id)
      ret = DataResponse(message= if(user == null)ret else user) 
      return ret 
   }
   fun  insertUser(msg:String ):DataResponse{
      Domains.insert(msg)
      return DataResponse(message= "add, one")
   }
   fun  updateUser(id:Long,msg:String):DataResponse{
      return DataResponse(message= "update, one by $id")
   }
   fun  delUser(id:Long):DataResponse {
      return DataResponse(message= "delete, one by $id")
   }

}
