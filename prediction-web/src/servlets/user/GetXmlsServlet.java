package servlets.user;

import com.google.gson.Gson;
import dto.manager.DtoManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;
import java.io.IOException;

public class GetXmlsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DtoManager dtoManager = ServletUtils.getDtoXmlManager(getServletContext());
        Gson gson = new Gson();
        String dtoXmlMap = gson.toJson(dtoManager.getDtoXmlManager());
        System.out.println("GetXmlsServlet sending current state of dto xml map");
        response.getWriter().println(dtoXmlMap);
        // response.getOutputStream().print(dtoXmlMap);
        response.setStatus(HttpServletResponse.SC_OK);


    }
}
