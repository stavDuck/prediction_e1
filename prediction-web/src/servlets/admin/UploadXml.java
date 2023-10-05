package servlets.admin;

import constants.Constants;
import engine.simulation.Simulation;
import engine.simulation.SimulationMultipleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import java.io.IOException;
import java.io.InputStream;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadXml extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        Part filePart = request.getPart("file1");
        InputStream fileContent = filePart.getInputStream();

        // check if simulationMultipleManager exist in servletContext - if not create one
        SimulationMultipleManager simulationMultipleManager = ServletUtils.getSimulationMultipleManager(getServletContext());

        // create simulation from InputStream
        Simulation simulation = new Simulation();
        try {
           int simulationID = simulation.createSimulation(fileContent);
            //if simulation name already exist!!!!
            // String simulationName = simulation.getsimulationName()
            if(simulationMultipleManager.isNameExistInMap("smoker")){
                String errorMessage = "File name " + "smoker" + " already exists. Please enter a different xml.";

                // stands for unauthorized as there is already such user with this name
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print(errorMessage);
            }
            else {
                // add new xml (simulation) to the list with uniq number
                simulationMultipleManager.addSimulationToSimulationMultipleManager("smoker", simulation);

                System.out.println("Added new xml named : smoker");
                response.setStatus(HttpServletResponse.SC_OK);
                //return the simulation ID if sucessfully added
                response.getWriter().println(simulationID);
            }
        }
        catch (RuntimeException e){
            // return status 500 - with the exception during creating the simulation from xml
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
        }
    }

}
