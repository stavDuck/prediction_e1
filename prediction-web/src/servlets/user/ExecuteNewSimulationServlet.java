package servlets.user;

import engine.simulation.Simulation;
import engine.simulation.SimulationMultipleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

// The servlet expected to get in request newEecutionDTO with -
// xml fileName + map for population + map for env values
// create new SimulationExecution - add it to the simulation
// return in the response the format "simulationId,xmlName" -> need to parse into (int,String)
public class ExecuteNewSimulationServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        // need to add
        String xmlName = request.getXmlNameFromRequest;
        String fileFullPath = ServletUtils.getXmlFileMap().get(xmlName);

        // If file name exist - the simulation in multipleManager have to be set correctly
        if(fileFullPath != null) {
            InputStream fileContent = new FileInputStream(new File(fileFullPath));


            // check if simulationMultipleManager exist in servletContext - if not create one
            SimulationMultipleManager simulationMultipleManager = ServletUtils.getSimulationMultipleManager(getServletContext());
            int idForCurrSimulation = simulationMultipleManager.getIdGenerator();

            // get simulation from multiple manager by xmlName
            Simulation simulation = simulationMultipleManager.getSimulationsManager().get(xmlName);
            try {
                // NEED TO ADD ALL PROPERTIES AND ENV VALS
                simulation.createSimulation(fileContent, idForCurrSimulation);
                simulation.getSimulationById(idForCurrSimulation).setPopulationValues;
                simulation.getSimulationById(idForCurrSimulation).setEnvValues;

                response.setStatus(HttpServletResponse.SC_OK);
                System.out.println("Added new simulationExecution id: " + idForCurrSimulation + "in Xml: "+ xmlName);

                //return the pattern "simulationId,xmlName" in response body
                response.getWriter().println(idForCurrSimulation + "," + xmlName);

                // if loaded successfuly - advance the uniq id to the next id
                simulationMultipleManager.advanceIdGenerator();

            } catch (RuntimeException e) {
                // return status 500 - with the exception during creating the simulation from xml
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(e.getMessage());
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("No file with the name: " + xmlName + " was found");
        }
    }
}
