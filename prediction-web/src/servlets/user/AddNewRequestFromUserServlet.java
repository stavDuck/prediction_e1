package servlets.user;

import com.google.gson.Gson;
import constants.Constants;
import dto.termination.DtoTermination;
import engine.request.Request;
import engine.request.RequestManager;
import engine.termination.Termination;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import request.DtoRequest;
import utils.ServletUtils;
import utils.SessionUtils;
import java.io.BufferedReader;
import java.io.IOException;

public class AddNewRequestFromUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        //Gson gson = new Gson();
        Gson gson = Constants.GSON_INSTANCE;
        try (BufferedReader reader = request.getReader()) {
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            // Now, the string from the request body is available in the requestBody StringBuilder.
            String requestBodyString = requestBody.toString();

            System.out.println("Received string from request body: " + requestBodyString);
            DtoRequest dtoRequest = gson.fromJson(requestBodyString, DtoRequest.class);

            // Create request from information and add to request manager
            RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
            Request requestFromUser = createRequestFromUser(dtoRequest);

            // test if the username in the request is the same as in the session
            String usernameFromSession = SessionUtils.getUsername(request);
            if(usernameFromSession.equals(dtoRequest.getUserName())) {
                requestManager.addNewRequestForUser(dtoRequest.getUserName(), requestFromUser);

                // set information on successful response
                response.setStatus(HttpServletResponse.SC_OK);
                String dtoRequestResponse = gson.toJson(createDtoRequestFromRequest(requestFromUser));
                response.getWriter().println(dtoRequestResponse);
            }
            else {
                // stands for unauthorized as there is already such user with this name
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getOutputStream().print("Username in session is not the same as the username in request");
            }

        } catch (IOException e) {
            e.printStackTrace();
            // Handle any exceptions appropriately
        }
    }

    private Request createRequestFromUser(DtoRequest dtoRequest) {
        String xmlName = dtoRequest.getSimulationXmlName();
        Integer simulationRequstedRuns = dtoRequest.getSimulationRequestedRuns();
        Termination termination = new Termination(dtoRequest.getTerminationConditions().getBySeconds(), dtoRequest.getTerminationConditions().getByTick());
        if (dtoRequest.getTerminationConditions().getBySeconds() == null &&
                dtoRequest.getTerminationConditions().getByTick() == null) {
            termination.setStoppedByUser(true);
        }

        Request newRequest = new Request(xmlName, simulationRequstedRuns, termination, dtoRequest.getUserName());

        return newRequest;
    }

    private DtoRequest createDtoRequestFromRequest(Request request){
        DtoTermination dtoTermination = new DtoTermination(request.getTerminationConditions().getByTick(),request.getTerminationConditions().getBySec());
        DtoRequest dtoRequest = new DtoRequest(request.getRequestId(), request.getSimulationXmlName(), request.getSimulationRequestedRuns(),
                request.getSimulationCurrentRunning(), request.getSimulationLeftoverRuns(),request.getSimulationFinishedRuns(), dtoTermination, request.getStatus(), request.getUserName());

        return dtoRequest;
    }
}
