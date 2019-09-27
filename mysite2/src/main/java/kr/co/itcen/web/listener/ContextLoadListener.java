package kr.co.itcen.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextLoadListener implements ServletContextListener {

	// web.xml에 등록을 다 마치고, 읽어냈을 때 실행
	public void contextInitialized(ServletContextEvent servletContextEvent)  {
		String contextConfigLocation = 
				servletContextEvent.getServletContext().getInitParameter("contextConfigLocation");
		System.out.println("MySite2 Application Starts with " + contextConfigLocation);
    }
	
	public void contextDestroyed(ServletContextEvent arg0)  { 
		System.out.println("MySite2 Application destroyed.....");
    }
	
}
