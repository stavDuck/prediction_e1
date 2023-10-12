package servlets.admin;

import com.google.gson.Gson;
import dto.manager.DtoManager;
import engine.simulation.SimulationMultipleManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

public class GetThreadPoolInfoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       SimulationMultipleManager simulationMultipleManager = ServletUtils.getSimulationMultipleManager(getServletContext());
        Gson gson = new Gson();
        String dtoThreadPool = gson.toJson(simulationMultipleManager.createDtoThreadPool());
        System.out.println("GetThreadPoolInfoServlet sending current state thread pool");
        response.getWriter().println(dtoThreadPool);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
