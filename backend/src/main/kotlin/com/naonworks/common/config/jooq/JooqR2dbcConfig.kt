package com.naonworks.common.config.jooq

import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.runBlocking
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@Configuration(proxyBeanMethods = false)
class JooqR2dbcConfig(
    private val props: JooqProperties,
) {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    @Bean
    fun dslContext(connectionFactory: ConnectionFactory): DSLContext {
        val ctx = DSL.using(connectionFactory)

        if (props.enabled) {
            props.schemaLocations.forEach {
                runBlocking {
                    val resource = PathMatchingResourcePatternResolver().getResource(it)
                    val sql = String(resource.inputStream.readAllBytes())
                    sql.split(";").forEach {item ->
                        val query = item.trim()

                        if(query.isNotBlank())
                            JooqQuery.execute(ctx.query(query))
                    }
                }
            }

            props.dataLocations.forEach {
                runBlocking {
                    val resource = PathMatchingResourcePatternResolver().getResource(it)
                    val sql = String(resource.inputStream.readAllBytes())
                    sql.split(";").forEach {item ->
                        val query = item.trim()

                        if(query.isNotBlank())
                            JooqQuery.execute(ctx.query(query))
                    }
                }
            }
        }

        return ctx
    }
}