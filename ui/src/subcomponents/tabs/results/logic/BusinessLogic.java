package subcomponents.tabs.results.logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import subcomponents.tabs.results.ResultsComponentController;

import java.util.function.Consumer;

public class BusinessLogic {
    private long currTick;
    private long runningTime;
    private Task<Boolean> currentRunningTask;
    private ResultsComponentController controller;

    public BusinessLogic(ResultsComponentController controller) {
        currTick = 0;
        this.controller = controller;
    }

    public void collectSimulationMetadata(Consumer<Long> currTickDelegate, Consumer<Long> runningTimeDelegate, Runnable onFinish) {


        /*Consumer<Long> currTickConsumer = ct -> {
            this.currTick = ct;
            currTickDelegate.accept(ct);
        };

        currentRunningTask = new CollectMetadataTask(fileName.get(), totalWordsConsumer, totalLinesDelegate);

        controller.bindTaskToUIComponents(currentRunningTask, onFinish);

        new Thread(currentRunningTask).start();*/
    }

}
