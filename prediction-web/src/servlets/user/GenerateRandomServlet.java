package servlets.user;

import constants.Constants;
import dto.range.DtoRange;
import engine.value.generator.ValueGeneratorFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GenerateRandomServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain;charset=UTF-8");
        String envName = req.getParameter(Constants.ENV_VARIABLE_NAME);
        String type = req.getParameter(Constants.ENV_VARIABLE_TYPE);
        Object random;
        switch (type) {
            case Constants.TYPE_FLOAT:
                String from = req.getParameter(Constants.FROM_RANGE);
                String to = req.getParameter(Constants.TO_RANGE);
                if(from != null && to != null) {
                    random = ValueGeneratorFactory.createRandomFloat(Float.parseFloat(from), Float.parseFloat(to));
                }
                else {
                    random = ValueGeneratorFactory.createRandomFloat(1.0f, 100.0f);
                }
                break;
            case Constants.TYPE_STRING:
                random = ValueGeneratorFactory.createRandomString().generateValue();
                break;
            case Constants.TYPE_BOOLEAN:
                random = ValueGeneratorFactory.createRandomBoolean().generateValue();
                break;
            default:
                random = null;
        }
        resp.getWriter().println(random);
    }
}
