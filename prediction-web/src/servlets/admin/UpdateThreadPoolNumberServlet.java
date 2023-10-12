package servlets.admin;

import constants.Constants;
import engine.simulation.SimulationMultipleManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

public class UpdateThreadPoolNumberServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
       try{
           int newNumber = Integer.parseInt(request.getParameter(Constants.NEW_NUMBER));
           SimulationMultipleManager simulationMultipleManager = ServletUtils.getSimulationMultipleManager(getServletContext());
           simulationMultipleManager.updateThreadsNumber(newNumber);
           System.out.println("new number of Threads : " + newNumber + " in thread pool is updated");
           response.setStatus(HttpServletResponse.SC_OK);
       }
       catch (Exception e){
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           response.getOutputStream().print(e.getMessage());
       }


    }
}
