package logging;

import java.net.URL;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * This class gets triggered on tomcat startup. It is used to configure log4j.
 */
public class LoggingConfigurer implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		
		try {
			URL log4jConfig = LoggingConfigurer.class.getClassLoader().getResource("logging/log4j.properties");
			PropertyConfigurator.configure(log4jConfig);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
