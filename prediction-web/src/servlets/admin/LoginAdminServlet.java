package servlets.admin;

import constants.Constants;
import engine.users.UserManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import utils.SessionUtils;

import java.io.IOException;

public class LoginAdminServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        UserManager userManager = ServletUtils.getUserManager(getServletContext());

        if (usernameFromSession == null) { //current user is not logged in yet

            String adminName = Constants.ADMIN;
                /*
                One can ask why not enclose all the synchronizations inside the userManager object ?
                Well, the atomic action we need to perform here includes both the question (isUserExists) and (potentially) the insertion
                of a new user (addUser). These two actions needs to be considered atomic, and synchronizing only each one of them, solely, is not enough.
                (of course there are other more sophisticated and performable means for that (atomic objects etc) but these are not in our scope)

                The synchronized is on this instance (the servlet).
                As the servlet is singleton - it is promised that all threads will be synchronized on the very same instance (crucial here)

                A better code would be to perform only as little and as necessary things we need here inside the synchronized block and avoid
                do here other not related actions (such as response setup. this is shown here in that manner just to stress this issue
                 */
            synchronized (this) {
                // if Admin is already logged in to server, cannot login again
                if (userManager.isAdminLoggedIn()) {
                    String errorMessage = "Admin is already exists. Cannot allow more than one Admin at once";

                    // stands for unauthorized as there is already such user with this name
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print(errorMessage);
                } else {
                    //set Admin is logged in
                    userManager.setAdminLoggedIn();
                    //set the username in a session so it will be available on each request
                    //the true parameter means that if a session object does not exists yet
                    //create a new one
                    request.getSession(true).setAttribute(Constants.USERNAME, adminName);

                    //redirect the request to the chat room - in order to actually change the URL
                    System.out.println("On login, request URI is: " + request.getRequestURI());
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            }

        } else {
            //user is already logged in
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
