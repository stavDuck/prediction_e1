package utils;
import dto.manager.DtoManager;
import engine.request.RequestManager;
import engine.simulation.SimulationMultipleManager;
import engine.users.UserManager;
import jakarta.servlet.ServletContext;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ServletUtils {

	private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
	private static final String SIMULATION_MULTIPLE_MANAGER_ATTRIBUTE_NAME = "simulationMultipleManagerLock";
	private static final String XML_INPUT_STREAM_FILE_MAP = "xmlInputStreamFileMap";

	// this object will be for the details view for admin and user
	private static final String DTO_XML_MANAGER = "dtoXmlManager";
	// this object will be for requests flows
	private static final String REQUEST_MANAGER = "requestManager";

	/*
	Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
	the actual fetch of them is remained un-synchronized for performance POV
	 */
	private static final Object userManagerLock = new Object();
	private static final Object simulationMultipleManagerLock = new Object();
	private static final Object xmlFileMapLock = new Object();
	private static final Object dtoXmlManagerLock = new Object();
	private static final Object requestManagerLock = new Object();

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

	// this map will save key = name of xml, value = strint contect of inputStream
	public static Map<String, String> getXmlInputStremFileMap (ServletContext servletContext){
		synchronized (xmlFileMapLock) {
			if (servletContext.getAttribute(XML_INPUT_STREAM_FILE_MAP) == null) {
				servletContext.setAttribute(XML_INPUT_STREAM_FILE_MAP, new HashMap<String,InputStream>());
			}
		}
		return (Map<String,String>) servletContext.getAttribute(XML_INPUT_STREAM_FILE_MAP);
	}

	public static DtoManager getDtoXmlManager(ServletContext servletContext){
		synchronized (dtoXmlManagerLock) {
			if (servletContext.getAttribute(DTO_XML_MANAGER) == null) {
				servletContext.setAttribute(DTO_XML_MANAGER, new DtoManager());
			}
		}
		return (DtoManager) servletContext.getAttribute(DTO_XML_MANAGER);
	}

	public static RequestManager getRequestManager(ServletContext servletContext){
		synchronized (requestManagerLock) {
			if (servletContext.getAttribute(REQUEST_MANAGER) == null) {
				servletContext.setAttribute(REQUEST_MANAGER, new RequestManager());
			}
		}
		return (RequestManager) servletContext.getAttribute(REQUEST_MANAGER);
	}
}