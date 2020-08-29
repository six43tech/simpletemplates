package ca.six43tech.tools.simpletemplates.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean

@Configuration
class FreeMarkerConfig {

    @Autowired private final ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv

    @Bean
    FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
        def cfg = new FreeMarkerConfigurationFactoryBean()
        cfg.setTemplateLoaderPath(buildEnv.sourceRoot.toURI().toString())
        cfg
    }
}
