package menu;
import dto.Dto;
import engine.action.FunctionHelper;
import engine.entity.EntityStructure;
import engine.property.PropertyInstance;
import engine.property.PropertyStructure;
import engine.property.type.Type;
import engine.simulation.Simulation;
import engine.simulation.SimulationHistory;
import engine.validation.exceptions.InvalidInputFromUser;
import engine.value.generator.ValueGeneratorFactory;
import sun.awt.windows.WPrinterJob;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class Menu {
    final static char OPTION_ONE = '1';
    final static char OPTION_TWO = '2';
    final static char OPTION_THREE = '3';
    final static char OPTION_FOUR = '4';
    final static char OPTION_FIVE = '5';

    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        boolean isFileHasValue = false;
        boolean isSimulationDone = false;
        boolean isSimulateHistoryNotEmpty = false;
        String fileName, tempFile;
        char choice;
        Simulation simulation = null, tempSimlate = null;

        // list of simulations history
        List<SimulationHistory> simulationHistory = null;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");

        System.out.println("Welcome to Prediction !");
        // main menu
        while (!isStop) {
            printMainMenu();
            choice = scanner.next().charAt(0);

            switch (choice) {
                // Option one = load file + copy info from xml
                case (OPTION_ONE):
                    // load the file
                    tempFile = getFileNameFromUser();
                    if (tempFile != null) {
                        try {
                            tempSimlate = loadFileXML(tempFile);

                            // if all went good - file name and file load
                            fileName = tempFile;
                            isFileHasValue = true;
                            simulation = tempSimlate;

                            System.out.println("File has loaded successfully, all information exported, ready to run simulation !!! \n");
                            simulationHistory = new ArrayList<>(); // every time we load new XML the history is deleted
                            isSimulateHistoryNotEmpty = false;
                        } catch (RuntimeException e) {
                            System.out.println(e.getMessage() +
                                    "\n please try to fix the issue and reload the xml again.");
                        }
                    } else
                        System.out.println("File has not loaded because the file path is not valid, file needs to be an XML type,  " +
                                "the last valid file has been loaded");
                    break;

                // Option two = over view of entities, rule and termination flow
                case (OPTION_TWO):
                    if (isFileHasValue) {
                        overViewMenu(simulation);
                    } else
                        System.out.println("File has not been loaded yet. please use option one first and load a new file.");
                    break;

                // Option three = run simulation
                case (OPTION_THREE):
                    if (isFileHasValue) {
                        // set env values
                        setSimulationEnvValues(simulation);
                        // Print all env names + values
                        printEnvLivsNamesAndValues(simulation);

                        // set startSimulation in simulation history
                        simulationHistory.add(new SimulationHistory(simulation, simulation.getSimulationID(),
                                currentDate.format(dateFormatter), currentTime.format(timeFormatter)));
                        isSimulateHistoryNotEmpty = true;

                        // run simulation
                        simulation.run();
                        System.out.println("Simulation id: " + simulation.getSimulationID() + "!!!!");

                        isSimulationDone = true;
                        // set the end simulation after run is done
                        simulationHistory.get(simulationHistory.size() - 1).setEndSimulation(simulation);
                    } else
                        System.out.println("File has not been loaded yet. please use option one first and load a new file.");

                    break;
                // Option four = show histograma
                case (OPTION_FOUR):
                    if (!isFileHasValue)
                        System.out.println("File has not been loaded yet. please use option one first and load a new file.");

                    else if (!isSimulationDone)
                        System.out.println("No Simulation on file has been run yet. please use option two first to run a simulation.");
                    else if (!isSimulateHistoryNotEmpty) {
                        System.out.println("No Simulation History found. please use option three first to run at lest one simulation.");
                    }
                    // if file is loaded and simulation finished to run - can get information
                    else {
                        // get information
                        simulationHistogram(simulationHistory);
                    }
                    break;
                case (OPTION_FIVE):
                    // stopint the application
                    isStop = true;
                    break;
                default:
                    System.out.println("Invalid option number, please try again with an option from the menu only");
            }


        }
        scanner.close();
    }


    public void printMainMenu() {
        System.out.println("MENU:");
        System.out.println("*********************");
        System.out.println("Please select the desire action");
        System.out.println("1) Load new XML file by file path");
        System.out.println("2) Present simulation information");
        System.out.println("3) Start simulation");
        System.out.println("4) Present previous simulation's information");
        System.out.println("5) Exit");
    }

    public void printOverViewMenu() {
        System.out.println("OVER - VIEW - MENU:");
        System.out.println("*********************");
        System.out.println("Please select the desire action");
        System.out.println("1) Present entities");
        System.out.println("2) Present rules");
        System.out.println("3) Present termination conditions");
        System.out.println("4) Return to main-menu");
    }

    public void printHistogramMenu() {
        System.out.println("VIEW OPTION FOR HISTOGRAM:");
        System.out.println("*********************");
        System.out.println("Please select the view type of the simulation histogram:");
        System.out.println("1. Entities population summary");
        System.out.println("2. Property Histogram");
        System.out.println("3. Return to main-menu");
    }


    public void printEnvLivsNamesAndValues(Simulation simulation) {
        int counter = 1;
        System.out.println("List of all environment names and values:");
        System.out.println("*********************");

        for (PropertyInstance prop : simulation.getWorld().getEnvironment().getPropertyInstancesMap().values()) {
            System.out.println(counter + ". Name: " + prop.getName() + ", Value: " + prop.getType().convert(prop.getVal()));
            counter++;
        }


    }

    public String getFileNameFromUser() {
        Scanner scanner = new Scanner(System.in);
        String inputString;

        System.out.println("Please enter the full path of your XML file");
        inputString = scanner.nextLine();
        if (validateFilePath(inputString))
            return inputString;
        // if file is not XML return null
        return null;
    }

    public boolean validateFilePath(String path) {
        if (!path.contains(".xml"))
            return false;

        return true;
    }

    public Simulation loadFileXML(String fileName) throws RuntimeException {

        // Simulation simulation= new Simulation("C:\\Users\\USER\\IdeaProjects\\prediction_e1\\engine\\src\\resources\\ex1-cigarets.xml");
         Simulation simulation = new Simulation("C:/study/java/prediction/engine/src/resources/ex1-cigarets.xml");
       // Simulation simulation = new Simulation("C:/study/java/prediction/engine/src/resources/master-ex1.xml");

        return simulation;
    }

    public void overViewMenu(Simulation simulation) {
        //
        // how to do copy data from world to dto ?
        // how we save the start and end valuses if dto die
        Dto dto = simulation.getWorld().createDto();
        Scanner scanner = new Scanner(System.in);
        char choice;
        boolean isReturnToMainMenu = false;

        while (!isReturnToMainMenu) {
            printOverViewMenu();
            // get choice from user
            //choice = scanner.nextInt();
            choice = scanner.next().charAt(0);

            switch (choice) {
                // Option one = show entitis
                case OPTION_ONE:
                    dto.printEntitiesStructure();
                    break;
                // Option two = show rules
                case OPTION_TWO:
                    dto.printRules();
                    break;
                // Option three = show termination
                case OPTION_THREE:
                    dto.printTermination();
                    break;
                // Option four = go back to main menu
                case OPTION_FOUR:
                    isReturnToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid option number, please try again with an option from the menu only");
            }
        }
    }

    private void setSimulationEnvValues(Simulation simulation) {
        System.out.println("Please enter value for the following environment properies:");
        System.out.println("For every property you can press 'Y' to set a valid value, or press 'N' to get default values");
        System.out.println("*********************");

        // going over all env properties and set values from user/ random
        for (PropertyInstance currProp : simulation.getWorld().getEnvironment().getPropertyInstancesMap().values()) {
            System.out.println("Env Property name: " + currProp.getName());
            System.out.println("Env Property type: " + currProp.getType().name().toLowerCase());
            setEnvValueByType(currProp);
        }

        System.out.println("All environment values are set successfully");
    }

    public void setEnvValueByType(PropertyInstance currProp) {
        Type type = currProp.getType();

        switch (type) {
            case DECIMAL:
                setDecimalValue(currProp);
                break;
            case FLOAT:
                setFloatValue(currProp);
                break;
            case BOOLEAN:
                setBooleanValue(currProp);
                break;
            case STRING:
                setStringValue(currProp);
                break;
        }
    }

    public void setDecimalValue(PropertyInstance currProp) {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        String c;
        Integer choice = null;

        // print for range if needed
        if (currProp.getRange() != null) {
            System.out.println("Env Property range: from: " + (int) currProp.getRange().getFrom() +
                    ", to: " + (int) currProp.getRange().getTo());
        }

        // if true need to get from user
        if (isValueInputOrGenerated()) {
            while (!isStop) {
                try {
                    System.out.println("Please enter Integer valid value");
                    choice = scanner.nextInt();
                    if (choice == null) {
                        System.out.println("Value is not valid Integer");
                    }
                    if ((currProp.getRange() != null) &&
                            ((int) (currProp.getRange().getFrom()) > choice || (int) (currProp.getRange().getTo()) < choice)) {
                        throw new InvalidInputFromUser("Value for this property out of rage, please try again and provide a valid value in between the range values");
                    }

                    currProp.setVal(choice);
                    isStop = true;
                } catch (InputMismatchException e) {
                    System.out.println("value is invalid, value need to be integer, please try again");
                    scanner.next(); // to get any issues with the values that not int

                } catch (InvalidInputFromUser e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        // generate value
        else {
            //Random random = new Random();
            if (currProp.getRange() != null) {
                currProp.setVal(ValueGeneratorFactory.createRandomInteger((int) currProp.getRange().getFrom(),
                        (int) currProp.getRange().getTo()).generateValue());
                // currProp.setVal( (int)currProp.getRange().getFrom() + random.nextInt((int)(currProp.getRange().getTo()) - (int)(currProp.getRange().getFrom()) + 1));
            } else {
                currProp.setVal(ValueGeneratorFactory.createRandomInteger(1, 100));
                //currProp.setVal(random.nextInt(100) + 1);
            }
        }
    }

    public void setFloatValue(PropertyInstance currProp) {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        Float choice;

        // print for range if needed
        if (currProp.getRange() != null) {
            System.out.println("Env Property range: from: " + currProp.getRange().getFrom() +
                    ", to: " + currProp.getRange().getTo());
        }

        // if true need to get from user
        if (isValueInputOrGenerated()) {
            while (!isStop) {
                try {
                    System.out.println("Please enter Float valid value");
                    choice = scanner.nextFloat();

                    if ((currProp.getRange() != null) &&
                            (currProp.getRange().getFrom() > choice || currProp.getRange().getTo() < choice)) {
                        throw new InvalidInputFromUser("Value for this property out of rage, please try again and provide a valid value in between the range values");
                    }

                    currProp.setVal(choice);
                    isStop = true;

                } catch (InputMismatchException e) {
                    System.out.println("value is invalid, value need to be float, please try again");
                    scanner.next(); // to get any issues with the values that not int
                } catch (InvalidInputFromUser e) {
                    System.out.println(e);
                }
            }
        }
        // generate value
        else {
            //Random random = new Random();
            if (currProp.getRange() != null) {
                currProp.setVal(ValueGeneratorFactory.createRandomFloat(currProp.getRange().getFrom(),
                        currProp.getRange().getTo()).generateValue());
                //currProp.setVal(currProp.getRange().getFrom() + random.nextFloat()* ((currProp.getRange().getTo()) - (currProp.getRange().getFrom())));
            } else {
                //currProp.setVal(random.nextFloat()*100.0 + 1);
                currProp.setVal(ValueGeneratorFactory.createRandomFloat(1.0f, 100.0f));
            }
        }
    }

    public void setBooleanValue(PropertyInstance currProp) {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        boolean choice;

        // if true need to get from user
        if (isValueInputOrGenerated()) {
            while (!isStop) {
                try {
                    System.out.println("Please enter Boolean valid value");
                    choice = scanner.nextBoolean();
                    currProp.setVal(choice);
                    isStop = true;
                } catch (InputMismatchException e) {
                    System.out.println("value is invalid, value need to be boolean, please try again");
                    scanner.next(); // to get any issues with the values that not int
                }
            }
        }
        // generate value
        else {
            currProp.setVal(ValueGeneratorFactory.createRandomBoolean().generateValue());
        }
    }

    public void setStringValue(PropertyInstance currProp) {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        String choice;

        if (isValueInputOrGenerated()) {
            System.out.println("Please enter a string");
            choice = scanner.nextLine();
            currProp.setVal(choice);
        }
        // generate value
        else {
            currProp.setVal(ValueGeneratorFactory.createRandomString().generateValue());
        }
    }


    // function return true - user will insert value
    // function return false - value will be generated
    public boolean isValueInputOrGenerated() {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        String choice;

        while (!isStop) {
            System.out.println("Press 'Y' to set value manually, 'N' for default value");
            choice = scanner.nextLine();

            switch (choice.toLowerCase()) {
                case "y":
                    return true;
                case "n":
                    return false;
                default:
                    System.out.println("Invalid option, input can only be Y or N");
            }
        }
        return false;
    }

    private void simulationHistogram(List<SimulationHistory> simulationHistory) {
        // print menu for simulation id from user
        SimulationHistory sh = getSimulationId(simulationHistory);

        // histogram view
        histogramView(sh);
    }

    public SimulationHistory getSimulationId(List<SimulationHistory> simulationHistory) {
        Scanner scanner = new Scanner(System.in);
        int index = 1;
        Integer choice = null;
        boolean isStop = false;

        System.out.println("SIMULATION HISTORY LIST:");
        System.out.println("*********************");
        for (SimulationHistory sh : simulationHistory) {
            System.out.println(index + ". Date: " + simulationHistory.get(index-1).getDate()
                    + " | " + simulationHistory.get(index-1).getTime() + " ID: " + simulationHistory.get(index-1).getUniqID());
            index++;
        }
        System.out.println();

        while (!isStop) {
            try {
                System.out.println("Please select the simulation from the list by simulation index");
                choice = scanner.nextInt();
                choice--; // to be inline with the list indexes
                if ((choice < 0) || (choice > simulationHistory.size() - 1)) {

                    throw new InvalidInputFromUser("Value is outside of range, please select from list");
                }

                isStop = true;
            } catch (InputMismatchException e) {
                System.out.println("value is invalid, value need to be integer, please try again");
                scanner.next(); // to get any issues with the values that not int
            } catch (InvalidInputFromUser e) {
                System.out.println(e.getMessage());
            }
        }
        return simulationHistory.get(choice);
    }


    public void histogramView(SimulationHistory sh) {
        Scanner scanner = new Scanner(System.in);
        char choice;
        boolean isReturnToMainMenu = false;

        while (!isReturnToMainMenu) {
            // menu
            printHistogramMenu();
            choice = scanner.next().charAt(0);

            switch (choice) {

                // Option 1 = entity population
                case '1':
                    printEntitiesPopulation(sh);
                   break;
                // Option 2 = histogram propery
                case '2':
                    printPropertyHistogram(sh);
                    break;
                // Option 3 = exit this menu
                case '3':
                    isReturnToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice, please select again");
            }
        }
    }

    private void printEntitiesPopulation(SimulationHistory sh) {
        Simulation sEnd = sh.getEndSimulation();

        System.out.println("VIEW ENTITIES POPULATION HISTOGRAM:");
        System.out.println("*********************");

        // go over all entities struchers - take the population for start value
        // go to entity instanse - get list of instanse by entity name key and get size
        for(EntityStructure entityStructure : sEnd.getWorld().getEntityStructures().values()){
            System.out.println("Entity name: " + entityStructure.getEntityName());
            System.out.println("Start population value: " + entityStructure.getPopulation() +
                    ", End population value: " + sEnd.getWorld().getInstanceManager().getInstancesByName(entityStructure.getEntityName()).size());
            System.out.println();
        }
    }

    private void printPropertyHistogram(SimulationHistory sh) {
        // select entity
        String entityName = selectEntityFromSimulationHistory(sh);
        // select entity's property
        String propertName = selectPropertyFromSimulationHistory(sh.getEndSimulation().getWorld()
                .getEntityStructures().get(entityName));
        // show histogram
        printHistogramByEntityAndValue(sh,entityName, propertName);
    }

    // return the entity name the user choice
    public String selectEntityFromSimulationHistory(SimulationHistory sh){
        Scanner scanner = new Scanner(System.in);
        int index = 1;
        Integer choice = null;
        boolean isStop = false;
        System.out.println("Please select the desired entity:");
        System.out.println("*********************");
        List<String> list = new ArrayList<>();

        for(EntityStructure entityStructure : sh.getEndSimulation().getWorld().getEntityStructures().values()){
            System.out.println(index + ". " + entityStructure.getEntityName());
            list.add(entityStructure.getEntityName());
            index ++;
        }


        while (!isStop) {
            try {
                System.out.println("Please select the entity:");
                choice = scanner.nextInt();
                choice--; // to be inline with the list indexes
                if ((choice < 0) || (choice > list.size() - 1)) {

                    throw new InvalidInputFromUser("Value is outside of range, please select from list");
                }

                isStop = true;
            } catch (InputMismatchException e) {
                System.out.println("value is invalid, value need to be integer, please try again");
                scanner.next(); // to get any issues with the values that not int
            } catch (InvalidInputFromUser e) {
                System.out.println(e.getMessage());
            }
        }
        return list.get(choice);
    }


    // return the property name the user choice
    public String selectPropertyFromSimulationHistory(EntityStructure entityStructure){
        Scanner scanner = new Scanner(System.in);
        int index = 1;
        Integer choice = null;
        boolean isStop = false;
        System.out.println("Please select the desired property:");
        System.out.println("*********************");
        List<String> list = new ArrayList<>();

        for(PropertyStructure propertyStructure : entityStructure.getEntityPropMap().values()){
            System.out.println(index + ". " + propertyStructure.getName());
            list.add(propertyStructure.getName());
            index ++;
        }


        while (!isStop) {
            try {
                System.out.println("Please select the property:");
                choice = scanner.nextInt();
                choice--; // to be inline with the list indexes
                if ((choice < 0) || (choice > list.size() - 1)) {

                    throw new InvalidInputFromUser("Value is outside of range, please select from list");
                }

                isStop = true;
            } catch (InputMismatchException e) {
                System.out.println("value is invalid, value need to be integer, please try again");
                scanner.next(); // to get any issues with the values that not int
            } catch (InvalidInputFromUser e) {
                System.out.println(e.getMessage());
            }
        }
        return list.get(choice);
    }


    public void printHistogramByEntityAndValue(SimulationHistory sh, String entityName, String propertyName){
        Map<String, Integer> histogramPropery = new HashMap<>();




        Map<Object, Long> countByPropertyValue = sh.getEndSimulation().getWorld().getInstanceManager().getInstancesByName(entityName).stream()
                .collect(Collectors.groupingBy(
                                instance -> instance.getPropertyInstanceByName(propertyName).getVal(),
                                Collectors.counting()));


        List<Map.Entry<Object, Long>> sortedEntries = countByPropertyValue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        System.out.println("Occurrence : Value of Property");
        // Print the sorted entries
        for (Map.Entry<Object, Long> entry : sortedEntries) {
            System.out.println( entry.getValue() + ":" + entry.getKey());
        }
    }

}
