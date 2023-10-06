package com.naonworks.common.config.jooq

import org.jooq.Query
import org.jooq.conf.ParamType

object JooqUtils {
    private val jooqLog = org.slf4j.LoggerFactory.getLogger(this::class.java)

    fun queryLog(clazz: Class<*>, query: Query) {
        if (jooqLog.isTraceEnabled) {
            jooqLog.trace("\nCLASS : {}\nQUERY : {}", clazz.canonicalName, query.getSQL(ParamType.NAMED_OR_INLINED))
        } else if (jooqLog.isDebugEnabled) {
            jooqLog.debug("{}", query.sql)
        }
    }

    fun queryLog(query: Query) {
        if (jooqLog.isTraceEnabled) {
            jooqLog.trace("\nQUERY : {}", query.getSQL(ParamType.NAMED_OR_INLINED))
        } else if (jooqLog.isDebugEnabled) {
            jooqLog.debug("{}", query.sql)
        }
    }
}