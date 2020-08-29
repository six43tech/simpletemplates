package ca.six43tech.tools.simpletemplates.resource


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class StaticResouceBuilder {

    private final ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv

    @Autowired
    StaticResouceBuilder(ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv) {
        this.buildEnv = buildEnv
    }

    void build(File input) {
        def relativePath = buildEnv.sourceRoot.relativePath(input)
        def dest = new File(buildEnv.targetRoot, relativePath)
        dest.parentFile.mkdirs()
        input.withInputStream { dest << it }
    }
}
