package ca.six43tech.tools.simpletemplates


import ca.six43tech.tools.simpletemplates.model.ModelBuilder
import ca.six43tech.tools.simpletemplates.template.TemplateBuilder
import groovy.io.FileType
import groovy.util.logging.Slf4j
import io.undertow.Undertow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration

@Slf4j
@SpringBootApplication
@EnableAutoConfiguration(exclude= EmbeddedWebServerFactoryCustomizerAutoConfiguration)
class SimpleTemplatesApplication implements CommandLineRunner {
	static void main(String[] args) {
		SpringApplication.run(SimpleTemplatesApplication, args)
	}

	static private final boolean ignore(File f) {
		def ignore = f.name.startsWith('.') || f.parent.matches(/.*\/?\..+\/?/)
		if(ignore)
			log.debug "Ignored: $f"
		ignore
	}

	private final ModelBuilder modelBuilder
	private final ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv
	private final ca.six43tech.tools.simpletemplates.resource.StaticResouceBuilder staticResourceBuilder
	private final TemplateBuilder templateBuilder
	private final Undertow undertow

	@Autowired
	SimpleTemplatesApplication(ModelBuilder modelBuilder, ca.six43tech.tools.simpletemplates.env.BuildEnv buildEnv, ca.six43tech.tools.simpletemplates.resource.StaticResouceBuilder staticResourceBuilder,
							   TemplateBuilder templateBuilder, Undertow undertow) {
		this.modelBuilder = modelBuilder
		this.buildEnv = buildEnv
		this.staticResourceBuilder = staticResourceBuilder
		this.templateBuilder = templateBuilder
		this.undertow = undertow
	}

	@Override
	void run(String... args) throws Exception {
		build()
		if(buildEnv.enableHttp)
			serve()
	}

	private final void build() {
		def modelBuilder = this.modelBuilder
		def staticResourceBuilder = this.staticResourceBuilder
		def templateBuilder = this.templateBuilder
		buildEnv.sourceRoot.eachFileRecurse(FileType.FILES) {
			if(ignore(it)) return
			def model = modelBuilder.build(it.parentFile)
			switch(it.name) {
				case 'model.stm': // ignore models
				case ~/.+\.sti$/: // ignore includes
					break
				case ~/.+\.stt$/: templateBuilder.build(it, model); break
				default: staticResourceBuilder.build(it)
			}
		}
	}

	private final void serve() {
		undertow.start()
		Runtime.addShutdownHook {
			undertow.stop()
		}
	}
}
