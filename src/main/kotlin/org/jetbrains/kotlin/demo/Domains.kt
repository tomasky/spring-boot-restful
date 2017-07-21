package org.jetbrains.kotlin.demo


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop

import org.jetbrains.exposed.dao.*

open class ResultInterface
data class User(val id: Long, val content: String):ResultInterface()
data class ErrorResponse(val status: Int, val message: String)
data class DataResponse(val status: Int =200, val message: Any)
data class DataListResponse(val status: Int=200, val message: List<ResultInterface>)

object Users : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
}

object Domains{


   fun findById(id:Long):User?{
      Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
      var ret:User? = null
      transaction {
        Users.select {
            Users.id.eq(id.toString()) 
        }.forEach{

         ret = User(it[Users.id].length.toLong(),it[Users.name])
      }

     }
    return ret
   }
   fun insert(msg:String){

      Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
      transaction {
        create (Users)
        Users.insert {
            it[id] = msg.length.toString() 
            it[name] = msg 
        }
     }
   }
}
