package ca.six43tech.tools.simpletemplates.model


import org.apache.commons.configuration2.CompositeConfiguration
import org.apache.commons.configuration2.Configuration
import org.apache.commons.configuration2.PropertiesConfiguration
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder
import org.apache.commons.configuration2.builder.fluent.Parameters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ModelBuilder {

    @Autowired
    private final ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv

    def build(File root, CompositeConfiguration config = null) {
        if(root == buildEnv.buildRoot) return config
        if(!config)
            config = new CompositeConfiguration()

        def modelSource = new File(root, 'model.stm')
        if(modelSource.canRead()) {
            def params = new Parameters()
            def builder = new FileBasedConfigurationBuilder<Configuration>(PropertiesConfiguration)
                            .configure(params.properties().setFile(modelSource))
            config.addConfiguration(builder.configuration)
        }
        build(root.parentFile, config)
    }
}
