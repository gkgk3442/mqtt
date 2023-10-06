package com.naonworks.common.config.jooq

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.jooq.Record
import org.jooq.ResultQuery
import org.jooq.RowCountQuery
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

object JooqQuery {

    suspend fun <T : ResultQuery<out R>, R : Record> findAllFlow(query: T): Flow<R> {
        JooqUtils.queryLog(query)

        @Suppress("UNCHECKED_CAST")
        return Flux.from(query as Publisher<out R>).asFlow()
    }

    suspend fun <T : ResultQuery<out R>, R : Record> findAll(query: T): List<R> = findAllFlow(query).toList()

    suspend fun <T : ResultQuery<out R>, R : Record> findOne(query: T): R? {
        JooqUtils.queryLog(query)

        @Suppress("UNCHECKED_CAST")
        return Flux.from(query as Publisher<out R>).asFlow().firstOrNull()
    }

    suspend fun <T : RowCountQuery> execute(query: T): List<Int> {
        JooqUtils.queryLog(query)

        @Suppress("UNCHECKED_CAST")
        return Flux.from(query as Publisher<Int>).asFlow().toList()
    }
}