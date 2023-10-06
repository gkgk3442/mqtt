package com.naonworks.common.config.rest

import com.fasterxml.jackson.annotation.JsonInclude
import org.jooq.exception.DataAccessException
import org.springframework.context.support.DefaultMessageSourceResolvable
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import java.sql.SQLException
import java.util.*

class RestErrorDetail {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    var exception: String? = null
    var objectName: String? = null
    var field: String? = null
    var code: String? = null

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var arguments: LinkedList<Any> = LinkedList()
    var message: String? = null
    var rejectValue: Any? = null

    constructor(e: Exception) {
//        exception = e.javaClass.simpleName
        exception = e::class.simpleName

        message = if (e is SQLException || e is DataAccessException || e is DataAccessException)
            "sql error"
        else
            e.message

        if (e is RestException) {
            val err = e as RestException

            val args = err.arguments
            if (!args.isNullOrEmpty())
                arguments.addAll(args.toList())
        }
    }

    constructor(error: ObjectError) {
        objectName = error.objectName
        code = error.code

        val args = error.arguments

        if (!args.isNullOrEmpty()) {
            for (element in args) {
                if (element is DefaultMessageSourceResolvable)
                    continue
                else if (element is Number || element is String)
                    arguments.add(element)
                else {
                    log.error("obj err : {}", element)
                    arguments.add(element.toString())
                }
            }
        }

        message = error.defaultMessage
    }

    constructor(error: FieldError) {
        objectName = error.objectName
        field = error.field
        code = error.code

        val args = error.arguments

        if (!args.isNullOrEmpty()) {
            for (element in args) {
                if (element is DefaultMessageSourceResolvable)
                    continue
                else if (element is Number || element is String)
                    arguments.add(element)
                else {
                    log.error("field err : {}", element)
                    arguments.add(element.toString())
                }
            }
        }

        message = error.defaultMessage
        rejectValue = error.rejectedValue
    }
}