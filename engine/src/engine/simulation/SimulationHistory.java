package engine.simulation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimulationHistory {
    private Simulation startSimulation;
    private Simulation endSimulation;
    private int uniqID;
    private String date;
    private String time;

    public SimulationHistory(Simulation startSimulation, int uniqID, String date, String time){
        this.startSimulation = startSimulation;
        this.endSimulation = startSimulation;
        this.uniqID = uniqID;
        this.date = date;
        this.time = time;
    }

    public Simulation getStartSimulation(){return startSimulation;}
    public Simulation getEndSimulation(){return endSimulation;}
    public String getDate() {
        return date;
    }
    public String getTime() {
        return time;
    }
    public int getUniqID(){return uniqID;}

    public void setStartSimulation(Simulation startSimulation) {
        this.startSimulation = startSimulation;
    }
    public void setEndSimulation(Simulation endSimulation) {
        this.endSimulation = endSimulation;
    }
    public void setUniqID(int uniqID) {
        this.uniqID = uniqID;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
