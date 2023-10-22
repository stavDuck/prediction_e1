package servlets.user;

import com.google.gson.Gson;
import constants.Constants;
import engine.request.Request;
import engine.request.RequestManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import request.DtoRequest;
import utils.ServletUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetUserRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
        //Gson gson = new Gson();
        Gson gson = Constants.GSON_INSTANCE;
        Map<Integer, Request> tempMap = requestManager.getAllRequestsForAUser(usernameFromParameter);
        Map<Integer, DtoRequest> resMap = createDtoRequestMapFromRequestMap(tempMap);
        String dtoUserRequestsMap = gson.toJson(resMap);
        System.out.println("GetUserRequestsServlet sending the current full requests list of USER : " + usernameFromParameter);
        response.getWriter().println(dtoUserRequestsMap);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<Integer, DtoRequest> createDtoRequestMapFromRequestMap(Map<Integer, Request> tempMap) {
        Map<Integer, DtoRequest> resMap = new HashMap<>();
        for(Request currRequest : tempMap.values()){
            resMap.put(currRequest.getRequestId(), currRequest.createDtoRequest());
        }
        return resMap;
    }

}
