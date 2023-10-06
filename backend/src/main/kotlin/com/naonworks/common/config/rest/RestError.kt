package com.naonworks.common.config.rest

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.validation.BindException
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors


class RestError {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var exception = mutableListOf<RestErrorDetail>()

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var global = mutableListOf<RestErrorDetail>()

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var field = mutableListOf<RestErrorDetail>()

    constructor(e: BindingResult) {
        e.globalErrors.map(::RestErrorDetail).forEach(global::add)
        e.fieldErrors.map(::RestErrorDetail).forEach(field::add)
    }

    constructor(e: BindException) : this(e.bindingResult)

    constructor(e: Errors) {
        e.globalErrors.map(::RestErrorDetail).forEach(global::add)
        e.fieldErrors.map(::RestErrorDetail).forEach(field::add)
    }

    constructor(e: Exception) {
        exception.add(RestErrorDetail(e))
    }
}
