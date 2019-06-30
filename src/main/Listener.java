package main;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import managers.AccountData;
import managers.AvatarManager;
import managers.IDGenerator;

/**
 * Application Lifecycle Listener implementation class Listener
 *
 */
@WebListener
public class Listener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public Listener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
    	AccountData accountData = null;
    	AvatarManager avatarManager = null;
    	IDGenerator generator = null;
    	
		try {
			accountData = AccountData.getInstance();
			avatarManager = AvatarManager.getInstance();
			generator = IDGenerator.getInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arg0.getServletContext().setAttribute("accountData", accountData);
		arg0.getServletContext().setAttribute("avatarManager", avatarManager);
		arg0.getServletContext().setAttribute("idGenerator", generator);
    	
    }
	
}
