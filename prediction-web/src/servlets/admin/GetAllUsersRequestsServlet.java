package servlets.admin;

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

public class GetAllUsersRequestsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
        Gson gson = Constants.GSON_INSTANCE;
        Map<String, Map<Integer, Request>> tempMap = requestManager.getAllRequestsInManager();
        Map<Integer, DtoRequest> resDtoMapOfAllRequests = createDtoRequestMap(tempMap);
        String dtoAllRequests = gson.toJson(resDtoMapOfAllRequests);
        System.out.println("GetAllUsersRequestsServlet sending the current full list of requests");
        response.getWriter().println(dtoAllRequests);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private Map<Integer, DtoRequest> createDtoRequestMap(Map<String, Map<Integer, Request>> tempMap) {
        Map<Integer, DtoRequest> resMap = new HashMap<>();

        for(Map<Integer, Request> currMap : tempMap.values()){
            for(Request currRequest : currMap.values()){
                resMap.put(currRequest.getRequestId(), currRequest.createDtoRequest());
            }
        }

        return resMap;
    }
}
