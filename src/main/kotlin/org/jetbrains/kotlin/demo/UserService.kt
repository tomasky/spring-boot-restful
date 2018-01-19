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
import org.springframework.boot.json.*
import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.module.kotlin.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class UserRequestJson(
      @JsonProperty("id") val id: Int,
      @JsonProperty("name") val name: String   
)


@Service
class UserService {

   val  logger:Logger = LoggerFactory.getLogger("UserService")

   fun checkJsonNull(json:Map<String,Any>){
      for(it in json){
         if("null".equals(it.value))
         throw DataCanNotNullException("input data value can not null")
         else if(it.value is Map<*,*>) {
            checkJsonNull(it.value as Map<String,Any>)
         }
      }
   }


   fun parseJson(jsonStr:String):Map<String,Any>{
      val json = BasicJsonParser().parseMap(jsonStr)
      checkJsonNull(json) 
      return json
   }

   @Async("taskExecutor")
   fun  findUsers():CompletableFuture<User> {
      val results:User =User(0,"all users") 
      Thread.sleep(5000L)
      logger.info("Looking up " + results)
      return CompletableFuture.completedFuture(results)
   }

   /*@Async*/
   fun  findUserById(id:Long):DataResponse {
      val user = UserRepository.findById(id)
      val ret = DataResponse(result= user?:"no datas") 
      return ret 
   }
   fun  insertUser(msg:String ):DataResponse{

      val retId = UserRepository.insert(parseJson(msg))
      return DataResponse(result= "add, one:$retId")
   }
   fun  updateUser(id:Long,msg:UserRequestJson):DataResponse{
      val ret = UserRepository.update(id,msg)
      return DataResponse(result= "update, one by $id,ret:$ret")
   }
   fun  delUser(id:Long):DataResponse {
      val ret = UserRepository.delById(id)
      return DataResponse(result= "delete, one by $id,ret:$ret")
   }

}
