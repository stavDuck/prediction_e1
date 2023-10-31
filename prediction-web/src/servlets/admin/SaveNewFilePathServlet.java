package servlets.admin;

import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class SaveNewFilePathServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain");
        String xmlName = request.getParameter(Constants.XML_NAME);

        try (BufferedReader reader = request.getReader()) {
            StringBuilder requestBody = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                requestBody.append(line);
            }

            // Now, the string from the request body is available in the requestBody StringBuilder.
            String requestBodyString = requestBody.toString();

            // save the input stream of the xml to create simulation execution later on !!!!!
            Map<String, String> Map = ServletUtils.getXmlInputStremFileMap(getServletContext());
            // save the key = xml name, value = fulle path to local file
            Map.put(xmlName,requestBodyString);
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (IOException e) {
                e.printStackTrace();
                // Handle any exceptions appropriately
            }
    }
}
