package org.jetbrains.kotlin.demo

class DataVALUENotMATCHException(msg:String) :RuntimeException(msg)
class DataCanNotNullException(msg:String) :RuntimeException(msg)
data class DataNOTFOUNDException(override val message:String):RuntimeException(message)


open class ResultInterface
data class User(val id: Long, val name: String):ResultInterface()
data class ErrorResponse(val status: Int, val message: String)
data class DataResponse(val status: Int =200, val message:String="done",val result: Any)


