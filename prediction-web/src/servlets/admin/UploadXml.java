package servlets.admin;

import com.google.gson.Gson;
import constants.Constants;
import dto.Dto;
import dto.manager.DtoManager;
import engine.simulation.Simulation;
import engine.simulation.SimulationMultipleManager;
import engine.simulation.execution.SimulationExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import java.io.IOException;
import java.io.InputStream;

// if response is successful response body will have the xml name
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class UploadXml extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        Part filePart = request.getPart("file1");
        InputStream fileContent = filePart.getInputStream();

        // check if dtoXmlManager exist in servletContext- if not create one
        DtoManager dtoManager = ServletUtils.getDtoXmlManager(getServletContext());
        // check if simulationMultipleManager exist in servletContext - if not create one
        SimulationMultipleManager simulationMultipleManager = ServletUtils.getSimulationMultipleManager(getServletContext());

        // create simulation from InputStream to get the xml name
        Simulation simulation = new Simulation();
        try {
            simulation.createSimulation(fileContent, simulationMultipleManager.getIdGenerator());

            //if simulation name already exist!!!!
            SimulationExecution simulationExecution = simulation.getSimulationById(simulationMultipleManager.getIdGenerator());
            synchronized (this) {
                if (simulationMultipleManager.isNameExistInMap(simulationExecution.getXmlName())) {
                    String errorMessage = "File name " + simulationExecution.getXmlName() + " already exists. Please enter a different xml.";

                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                } else {
                    Dto dtoUploadXml = simulation.getSimulationById(simulationMultipleManager.getIdGenerator()).createWorldDto();
                    // save new simulation dto
                    dtoManager.addNewDtoToManager(dtoUploadXml.getXmlName(), dtoUploadXml);
                    // add NEW xml (simulation) to the list with uniq number
                    simulationMultipleManager.addSimulationToSimulationMultipleManager(simulationExecution.getXmlName(), new Simulation());

                    System.out.println("Added new xml named : " + simulationExecution.getUserName());
                    response.setStatus(HttpServletResponse.SC_OK);

                    // return the DTO in string format
                    //Gson gson = new Gson();
                    Gson gson = Constants.GSON_INSTANCE;
                    response.getOutputStream().print(gson.toJson(dtoUploadXml));
                }
            }
        }
        catch (RuntimeException e){
            // return status 500 - with the exception during creating the simulation from xml
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(e.getMessage());
        }
    }

}
