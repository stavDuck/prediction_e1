package servlets.admin;

import engine.simulation.Simulation;
import engine.simulation.SimulationMultipleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletUtils;
import java.io.IOException;
import java.io.InputStream;


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
        simulation.createSimulation(fileContent);
        // add new xml (simulation) to the list with uniq number
        simulationMultipleManager.addSimulationToSimulationMultipleManager("smoker", simulation);
        System.out.println("Added new xml named : smoker");

        // NEED TO ADD SUPPORT IF NAME ALREADY EXIST
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
