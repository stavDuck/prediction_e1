package servlets.user;

import com.google.gson.Gson;
import constants.Constants;
import engine.request.RequestManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;

public class GetUserRequestsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
        //Gson gson = new Gson();
        Gson gson = Constants.GSON_INSTANCE;
        String dtoUserRequestsMap = gson.toJson(requestManager.getAllRequestsForAUser(usernameFromParameter));
        response.getWriter().println(dtoUserRequestsMap);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
