package com.naonworks

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Profile("prod")
@Component
class WebVersionPrint : ApplicationRunner {
    private val gitPropsFileName = "git.properties"

    @Value("\${spring.application.name:web}")
    private lateinit var appName: String

    override fun run(args: ApplicationArguments?) {
        val classPathResource = ClassPathResource(gitPropsFileName)

        if (classPathResource.exists()) {
            val outputFileName = appName + "_version.txt"

            Files.copy(
                classPathResource.inputStream,
                Path.of(outputFileName),
                StandardCopyOption.REPLACE_EXISTING
            )
        }
    }
}
