package ca.six43tech.tools.simpletemplates.config

import ca.six43tech.tools.simpletemplates.env.BuildEnv
import io.undertow.Undertow
import io.undertow.server.handlers.resource.FileResourceManager
import io.undertow.server.handlers.resource.ResourceHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class UndertowConfig {

    @Autowired private final Environment env
    @Autowired private final BuildEnv buildEnv

    @Bean
    Undertow undertow() {
        def port = env.getProperty('server-port', '8880').toInteger()
        def bind = env.getProperty('server-bind', 'localhost')
        def server = Undertow.builder()
            .addHttpListener(port, bind)
            .setHandler(new ResourceHandler(new FileResourceManager(buildEnv.targetRoot)))
        server.build()
    }
}
