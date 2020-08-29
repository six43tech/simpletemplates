package ca.six43tech.tools.simpletemplates.env

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
class BuildEnv {

    final File buildRoot
    final File sourceRoot
    final File targetRoot
    final boolean enableHttp

    @Autowired
    BuildEnv(Environment env) {
        buildRoot = new File(env.getRequiredProperty('root'))
        sourceRoot = new File(buildRoot, 'src')
        targetRoot = new File(buildRoot, 'target')
        enableHttp = Boolean.parseBoolean(env.getProperty('http'))
        targetRoot.deleteDir()
        targetRoot.mkdirs()
    }
}
