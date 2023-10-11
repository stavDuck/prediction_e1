package utils;
import dto.manager.DtoManager;
import engine.simulation.SimulationMultipleManager;
import engine.users.UserManager;
import jakarta.servlet.ServletContext;

import java.util.HashMap;
import java.util.Map;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String SIMULATION_MULTIPLE_MANAGER_ATTRIBUTE_NAME = "simulationMultipleManagerLock";
	private static final String XML_FILE_MAP = "xmlFileMap";

	// this object will be for the details view for admin and user
	private static final String DTO_XML_MANAGER = "dtoXmlManager";
	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object simulationMultipleManagerLock = new Object();
	private static final Object xmlFileMapLock = new Object();
	private static final Object dtoXmlManagerLock = new Object();

	public static UserManager getUserManager(ServletContext servletContext) {

		synchronized (userManagerLock) {
			if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new UserManager());
			}
		}
		return (UserManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
	}

	public static SimulationMultipleManager getSimulationMultipleManager(ServletContext servletContext){
		synchronized (simulationMultipleManagerLock) {
			if (servletContext.getAttribute(SIMULATION_MULTIPLE_MANAGER_ATTRIBUTE_NAME) == null) {
				servletContext.setAttribute(SIMULATION_MULTIPLE_MANAGER_ATTRIBUTE_NAME, new SimulationMultipleManager());
			}
		}
		return (SimulationMultipleManager) servletContext.getAttribute(SIMULATION_MULTIPLE_MANAGER_ATTRIBUTE_NAME);
	}

	public static Map<String,String> getXmlFileMap (ServletContext servletContext){
		synchronized (xmlFileMapLock) {
			if (servletContext.getAttribute(XML_FILE_MAP) == null) {
				servletContext.setAttribute(XML_FILE_MAP, new HashMap<String,String>());
			}
		}
		return (Map<String,String>) servletContext.getAttribute(XML_FILE_MAP);
	}

	public static DtoManager getDtoXmlManager(ServletContext servletContext){
		synchronized (dtoXmlManagerLock) {
			if (servletContext.getAttribute(DTO_XML_MANAGER) == null) {
				servletContext.setAttribute(DTO_XML_MANAGER, new DtoManager());
			}
		}
		return (DtoManager) servletContext.getAttribute(DTO_XML_MANAGER);
	}
}
