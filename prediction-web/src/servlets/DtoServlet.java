package servlets;

import com.google.gson.*;
import dto.Dto;
import dto.grid.DtoGrid;
import dto.rule.Action.DtoAbstractAction;
import engine.action.AbstractAction;
import engine.action.type.*;
import engine.action.type.calculation.Calculation;
import engine.action.type.condition.Condition;
import engine.action.type.condition.ConditionMultiple;
import engine.action.type.condition.ConditionSingle;
import engine.simulation.SimulationMultipleManager;
import engine.simulation.execution.SimulationExecution;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class DtoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //getParameter xml name+ simulation ID
        //getSevlerContex.getSimulation
        //String fileName = getServletContext().getAttribute("name").toString();
       // Integer id = Integer.parseInt(getServletContext().getAttribute("id").toString());
        //should be "createDto" func
        String dtoJson = createDummy();
        response.getWriter().println(dtoJson);
        Gson gson = new Gson();
        Dto dto = gson.fromJson(dtoJson, Dto.class);
        System.out.println("finished");
    }

    public String createDummy() {
        Gson gson = new Gson();
            Dto dto = new Dto();
            dto.addEntity("entity", 100, null);
            dto.addPropertyToEntity("entity", "property", "prop type", null, false);
            dto.addRule("rule", 3, 3.3, 1);
            dto.addIncreaseAction("rule", "property", "3", "increase", "entity", false, null);
            dto.addEnv("env type", "env", 3.0f, 4.0f);
            dto.addTermination(3, 3);
            dto.setGrid(new DtoGrid(3, 3));
            dto.setCurrTicks(2);
        dto.setErrorStopSimulation("stop");
        return gson.toJson(dto);
    }

    public String createDto(String simulationName, int simulationID) {
        SimulationMultipleManager manager = ServletUtils.getSimulationMultipleManager(getServletContext());
        SimulationExecution reqSimulation = manager.getSimulationByNameAndID(simulationName, simulationID);
        Dto dto = reqSimulation.createWorldDto();
        Gson gson = new Gson();
        return gson.toJson(dto);
    }


}
