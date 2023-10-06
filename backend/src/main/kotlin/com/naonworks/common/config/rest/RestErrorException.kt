package com.naonworks.common.config.rest

data class RestErrorException(val restError: RestError) : RuntimeException()