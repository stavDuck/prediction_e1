package servlets.user;

import com.sun.org.apache.bcel.internal.Const;
import constants.Constants;
import engine.request.Request;
import engine.request.RequestManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

public class UpdateRequestAfterExecution extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       try {
           Integer requestId = Integer.parseInt(request.getParameter(Constants.REQUEST_ID));
           RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
           Request currRequest = requestManager.getRequestById(requestId);

           // update the information of the request after user execute simulation
           currRequest.setSimulationCurrentRunning(currRequest.getSimulationCurrentRunning() + 1);
           currRequest.setSimulationLeftoverRuns(currRequest.getSimulationLeftoverRuns() - 1);

           System.out.println("Updated the Request after executing simulation successful");
           response.setStatus(HttpServletResponse.SC_OK);
       }
       catch (NumberFormatException e){
           response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           response.getOutputStream().print("Error: Request Id is not a valid number");
       }
    }
}
