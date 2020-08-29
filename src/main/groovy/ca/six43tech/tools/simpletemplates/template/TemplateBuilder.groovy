package ca.six43tech.tools.simpletemplates.template


import org.apache.commons.configuration2.CompositeConfiguration
import org.apache.commons.configuration2.ConfigurationConverter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class TemplateBuilder {

    @Autowired private final freemarker.template.Configuration tmplConfig
    @Autowired private final ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv

    void build(File input, CompositeConfiguration config) {
        def relativePath = buildEnv.sourceRoot.relativePath(input)
        def tmpl = tmplConfig.getTemplate(relativePath)
        def target = new File(buildEnv.targetRoot, relativePath.substring(0, relativePath.lastIndexOf('.')))
        target.parentFile.mkdirs()
        target.withWriter {
            tmpl.process(ConfigurationConverter.getMap(config), it)
        }
    }
}
