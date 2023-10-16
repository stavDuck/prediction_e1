package servlets.admin;

import constants.Constants;
import engine.request.Request;
import engine.request.RequestManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

public class UpdateRequestStatusServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        String requestidFromParameter = request.getParameter(Constants.REQUEST_ID);
        String newstatusFromParameter = request.getParameter(Constants.NEW_STATUS);

        RequestManager requestManager = ServletUtils.getRequestManager(getServletContext());
       // Request request1 = requestManager.getRequestByIdAndUserName()
    }
}
