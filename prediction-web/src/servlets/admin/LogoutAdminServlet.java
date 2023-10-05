package servlets.admin;

import engine.users.UserManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

public class LogoutAdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String nameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (nameFromSession != null) {
            response.getWriter().println("Clearing session for " + nameFromSession);
            System.out.println("Clearing session for " + nameFromSession);
            userManager.removeAdmin();
            SessionUtils.clearSession(request);
        }
    }
}
