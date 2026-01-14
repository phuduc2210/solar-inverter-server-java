package solar.server;

import java.io.FileNotFoundException;
import java.io.IOException;

import solar.server.Main;
import solar.server.configs.SpringConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.util.Log4jConfigurer;

/**
 * Hello world!
 *
 */
public class Main 
{
	public static void main(String[] args) throws FileNotFoundException, IOException {
		initLog4jConfig();
		Logger LOG = LoggerFactory.getLogger(Main.class);
		LOG.debug("Starting application context");
		
		@SuppressWarnings("resource")
		AbstractApplicationContext ctx = new AnnotationConfigApplicationContext(
				SpringConfig.class);
		System.out.println("main running.....");
		
		ctx.registerShutdownHook();
	}
	
	public static void initLog4jConfig() throws FileNotFoundException{
		//Log4jConfigurer.initLogging("classpath:log4j.properties");
		String profileActive = System.getProperty("spring.profiles.active");
		if(profileActive != null){
			Log4jConfigurer.initLogging("classpath:log4j-"+profileActive+".properties");
		}
	}
}