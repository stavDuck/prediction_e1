package servlets.user;

import engine.users.UserManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

public class LogoutUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession != null) {
            response.getWriter().println("Clearing session for " + usernameFromSession);
            System.out.println("Clearing session for " + usernameFromSession);
            userManager.removeUser(usernameFromSession);
            SessionUtils.clearSession(request);
        }
    }
}
