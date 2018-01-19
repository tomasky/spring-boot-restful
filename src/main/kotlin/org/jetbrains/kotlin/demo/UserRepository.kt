package org.jetbrains.kotlin.demo


import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop

import org.jetbrains.exposed.dao.*

object Users : Table() {
   val id = long("id").autoIncrement().primaryKey() // Column<Long>
   val name = varchar("name", length = 50) // Column<String>
}

object UserRepository{

   fun handlerData(handMethod: () -> Long):Long{
      var ret:Long = 0
      Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
      transaction {
         create (Users)
         ret =   handMethod() 
      }
      return ret
   }

   fun insert(msg:Map<String,Any>):Long{
      val ret =  handlerData{ ->
               Users.insert {
                  it[name] = msg.get("name") as String
               }get Users.id
       }

      return ret
   }

   fun delById(id:Long):Long{
      return handlerData{ -> 
         (Users.deleteWhere{Users.id eq id}).toLong()
      }
   } 

   fun update(id:Long,msg:UserRequestJson):Long{
      val ret =  handlerData{ ->
         (Users.update({Users.id eq id}) {
            it[name] = msg.name
         }).toLong()
      }
      return ret
   }

   fun findById(id:Long):User?{
      Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false;", driver = "org.h2.Driver")
      var ret:User? = null
      transaction {
         create (Users)
         ret = Users.select {
            Users.id.eq(id.toString()) 
         }.map {
            User(it[Users.id],it[Users.name])
         }.getOrNull(0)
      }
      return ret?:throw DataNOTFOUNDException("not datas")
   }


}
