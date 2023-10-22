package admin.subcomponents.model;

import admin.util.http.HttpAdminUtil;
import com.google.gson.Gson;
import dto.Dto;
import dto.manager.DtoManager;
import javafx.application.Platform;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import admin.subcomponents.common.ResourcesConstants;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Model {
    private boolean isFileLoaded = false;
    private int currSimulationId;
    private String currSimulationName;
    private DtoManager dtoXmlManager;

    public Model() {
        dtoXmlManager = new DtoManager();
    }

    public DtoManager getDtoXmlManager(){
        return dtoXmlManager;
    }
    public void addNewDtoToManager(Dto dto){
        dtoXmlManager.addNewDtoToManager(dto.getXmlName(), dto);
    }
    public void setDtoXmlManager(DtoManager dtoManager){
        this.dtoXmlManager = dtoManager;
    }
    public Dto getDtoByXmlName(String xmlName){
        return dtoXmlManager.getDtoXmlManager().get(xmlName);
    }

    // function gets file name, try to load new simulation, if successful return "" else - return the error information
    /*public String loadXmlFile(String fileName){
        Simulation tempSimulation = null;
        int tempSimulationID;

        LocalDate currentDate;
        LocalTime currentTime;
        // list of simulations history
        // simulationHistory = null;
        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

        try {
            tempSimulation = new Simulation();
            // reset uniq id to 1 every time we laod new xml
            tempSimulation.resetUniqID();
            tempSimulationID = tempSimulation.createSimulation(fileName);
            isFileLoaded = true;

            //tempSimulation = createSimulationFromFile(fileName);
            // tempSimlate.setSimulationID(idGenerator);
            //idGenerator++;

            // if all went good
            simulation = tempSimulation;
            currSimulationId = tempSimulationID;
            currSimulationName = fileName;
            //simulationHistory = new ArrayList<>(); // every time we load new XML the history is deleted
            //isSimulateHistoryNotEmpty = false;
        } catch (RuntimeException e) {
            return (e.getMessage() +
                    "\nplease try to fix the issue and reload the xml again.\n");
        }

        // loaded sucessfuly
        return "File loaded successfully";
    }*/

    /*public Simulation createSimulationFromFile(String fileName) throws RuntimeException {
        Simulation simulation= new Simulation(fileName);
        return simulation;
    }*/

   /* public Dto getDtoWorld(){
        String simulationName;
        final Dto[] dto = new Dto[1];
        String finalUrl = HttpUrl
                .parse(ResourcesConstants.GET_DTO)
                .newBuilder()
                .addQueryParameter("name", currSimulationName)
                .addQueryParameter("id", String.valueOf(currSimulationId))
                .build()
                .toString();

        HttpAdminUtil.runAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("dto retrival failed");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                if (response.code() != 200) {
                    System.out.println("dto retrival failed");
                } else {
                    Platform.runLater(() -> {
                        // chatAppMainController.updateUserName(userName);
                        dto[0] = gson.fromJson(responseBody, Dto.class);
                    });
                }
            }
        });


        return dto[0];
        // return simulation.getWorld().createDto();

    }*/





    public Dto getDtoWorld() {
        Dto dto = new Dto();

        String finalUrl = HttpUrl
                .parse(ResourcesConstants.GET_DTO)
                .newBuilder()
                .addQueryParameter("name", currSimulationName)
                .addQueryParameter("id", String.valueOf(currSimulationId))
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(finalUrl).build();
        Call call = HttpAdminUtil.HTTP_CLIENT.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Gson gson = new Gson();
                if (response.code() != 200) {
                    System.out.println("dto retrieval failed");
                } else {
                    dto = gson.fromJson(responseBody, Dto.class);

                }
            } else {
                System.out.println("Response is NOT valid");
            }
            response.close();

        } catch (IOException e) {
            System.out.println("call to dtoResponse servlet failed");
        }

        return dto;
    }



    /*public Simulation getSimulation() {
        return simulation;
    }*/

    public void runSimulation() {
        try {
            LocalDate currentDate;
            LocalTime currentTime;
            // simulation already ran need to create a new one
            /*if(simulation.getWorld().getTermination().isStop() == true){
                simulation = loadFileXML(fileName);
                simulation.setSimulationID(idGenerator);
                idGenerator++;
            }*/

            // set env values
            //setSimulationEnvValues(simulation);
            // Print all env names + values
            //printEnvLivsNamesAndValues(simulation);
            currentDate = LocalDate.now();
            currentTime = LocalTime.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

            // set startSimulation in simulation history
            //simulationHistory.add(new SimulationHistory(simulation, simulation.getSimulationID(),
                    //currentDate.format(dateFormatter), currentTime.format(timeFormatter)));
           // isSimulateHistoryNotEmpty = true;

            // run simulation
           // simulation.run();
            //simulation.execute(currSimulationId);

           // isSimulationDone = true;
            // set the end simulation after run is done
            //simulationHistory.get(simulationHistory.size() - 1).setEndSimulation(simulation);
        }
        catch (RuntimeException e) {
            //System.out.println("Simulation failed with an error: " + e.getMessage());
        }
    }

   // public boolean getSimulationDone() {
    //    return isSimulationDone;
   // }

   /* public SimulationExecution getSimulationById(int id) {
        return simulation.getSimulationById(id);
        //return simulationHistory.get(id).getStartSimulation();
    }*/

    /*public SimulationExecution getCurrSimulation(){
        return simulation.getSimulationById(currSimulationId);
    }*/

    public int getCurrSimulationId() {
        return currSimulationId;
    }

    public void setCurrSimulationId(int currSimulationId) {
        this.currSimulationId = currSimulationId;
    }
}

