package menu;
import engine.property.PropertyInstance;
import engine.property.type.Type;
import engine.simulation.Simulation;
import engine.value.generator.ValueGeneratorFactory;
import sun.awt.windows.WPrinterJob;

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;


public class Menu {
    final static int OPTION_ONE = 1;
    final static int OPTION_TWO = 2;
    final static int OPTION_THREE = 3;
    final static int OPTION_FOUR = 4;
    final static int OPTION_FIVE = 5;
    public void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        boolean isFileHasValue = false;
        String fileName, tempFile;
        int choice;
        Simulation simulation = null, tempSimlate = null;

        System.out.println("Welcome to Prediction !");
        // main menu
        while (!isStop) {
            printMainMenu();
            // get choice from user
            choice = scanner.nextInt();

           switch (choice){
               // Option one = load file + copy info from xml
               case(OPTION_ONE):
                   // load the file
                   tempFile = getFileNameFromUser();
                    if(tempFile != null){
                        try {
                            tempSimlate = loadFileXML(tempFile);

                            // if all went good - file name and file load
                            fileName =  tempFile;
                            isFileHasValue = true;
                            simulation = tempSimlate;

                            System.out.println("File has loaded successfully, all information exported, ready to run simulation !!! \n");
                        }
                        catch (RuntimeException e){
                            System.out.println(e.getMessage() +
                                    "\n please try to fix the issue and reload the xml again.");
                        }
                    }
                    else
                       System.out.println("File has not loaded because the file path is not valid, file needs to be an XML type,  " +
                               "the last valid file has been loaded");
                   break;

                  // Option two = over view of entities, rule and termination flow
               case(OPTION_TWO):
                   if(isFileHasValue){
                       overViewMenu(simulation);
                   }
                   else
                       System.out.println("File has not been loaded yet. please use option one first and load a new file.");
                   break;

               // Option three = run simulation
               case(OPTION_THREE):
                   // set env values
                   setSimulationEnvValues(simulation);

                   // Print all env names + values
                   printEnvLivsNamesAndValues(simulation);

                   // run simulation
                   simulation.run();
                   break;
               case(OPTION_FOUR):
                   break;
               case(OPTION_FIVE):
                   // stopint the application
                   isStop = true;
                   break;
               default:
                   System.out.println("Invalid option number, please try again with an option from the menu only");
           }


        }
        scanner.close();
    }



    public void printMainMenu(){
        System.out.println("MENU:");
        System.out.println("*********************");
        System.out.println("Please select the desire action");
        System.out.println("1) Load new XML file by file path");
        System.out.println("2) Present simulation information");
        System.out.println("3) Start simulation");
        System.out.println("4) Present previous simulation's information");
        System.out.println("5) Exit");
    }
    public void printOverViewMenu(){
        System.out.println("OVER - VIEW - MENU:");
        System.out.println("*********************");
        System.out.println("Please select the desire action");
        System.out.println("1) Present entities");
        System.out.println("2) Present rules");
        System.out.println("3) Present termination conditions");
        System.out.println("4) Return to main-menu");
    }

    public void printEnvLivsNamesAndValues(Simulation simulation){
        int counter = 1;
        System.out.println("List of all environment names and values:");
        System.out.println("*********************");

        for(PropertyInstance prop: simulation.getWorld().getEnvironment().getPropertyInstancesMap().values()){
            System.out.println(counter + ". Name: " + prop.getName() + ", Value: " + prop.getType().convert(prop.getVal()));
        }


    }
    public String getFileNameFromUser(){
        Scanner scanner = new Scanner(System.in);
        String inputString;

        System.out.println("Please enter the full path of your XML file");
        inputString = scanner.nextLine();
        if(validateFilePath(inputString))
            return inputString;
        // if file is not XML return null
        return null;
    }
    public boolean validateFilePath(String path){
        if(!path.contains(".xml"))
            return false;

        return true;
    }
    public Simulation loadFileXML(String fileName) throws RuntimeException{

       // Simulation simulation= new Simulation("C:\\Users\\USER\\IdeaProjects\\prediction_e1\\engine\\src\\resources\\ex1-cigarets.xml");
       // Simulation simulation = new Simulation("C:/study/java/prediction/engine/src/resources/ex1-cigarets.xml");
         Simulation simulation = new Simulation("C:/study/java/prediction/engine/src/resources/master-ex1.xml");

        return simulation;
    }

    public void overViewMenu(Simulation simulation){
        Scanner scanner = new Scanner(System.in);
        int choice;
        boolean isReturnToMainMenu = false;

        while(!isReturnToMainMenu){
            printOverViewMenu();
            // get choice from user
            choice = scanner.nextInt();

            switch (choice){
                    // Option one = show entitis
                case OPTION_ONE:
                    simulation.printEntities();
                    break;
                    // Option two = show rules
                case OPTION_TWO:
                    simulation.printRules();
                    break;
                    // Option three = show termination
                case OPTION_THREE:
                    simulation.printTermination();
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
        Scanner scanner = new Scanner(System.in);
        boolean isFinishInput = false;
        String input;
        System.out.println("Please enter value for the following environment properies:");
        System.out.println("For every property you can press 'Y' to set a valid value, or press 'N' to get default values");
        System.out.println("*********************");

        // going over all env properties and set values from user/ random
        for (PropertyInstance currProp : simulation.getWorld().getEnvironment().getPropertyInstancesMap().values()){
            System.out.println("Env Property name: " + currProp.getName());
            System.out.println("Env Property type: " + currProp.getType().name().toLowerCase());
            setEnvValueByType(currProp);
        }

        System.out.println("All environment values are set successfully");
    }

    public void setEnvValueByType(PropertyInstance currProp){
        Type type = currProp.getType();

        switch (type){
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

    public void setDecimalValue(PropertyInstance currProp){
        Scanner scanner = new Scanner(System.in);
        boolean isStop = false;
        int choice;

        // print for range if needed
        if(currProp.getRange() != null) {
            System.out.println("Env Property range: from: " + currProp.getRange().getFrom() +
                    ", to: "+ currProp.getRange().getTo());
        }

        // if true need to get from user
        if(isValueInputOrGenerated()){
            while(!isStop){
                try {
                System.out.println("Please enter Integer valid value");
                choice = scanner.nextInt();
                currProp.setVal(choice);
                isStop = true;
                }
                catch (InputMismatchException e){
                    System.out.println("value is invalid, value need to be integer, please try again");
                }
            }
        }
        // generate value
        else{
            //Random random = new Random();
            if(currProp.getRange() != null) {
                currProp.setVal(ValueGeneratorFactory.createRandomInteger((int) currProp.getRange().getFrom(),
                        (int)currProp.getRange().getTo()));
               // currProp.setVal( (int)currProp.getRange().getFrom() + random.nextInt((int)(currProp.getRange().getTo()) - (int)(currProp.getRange().getFrom()) + 1));
            }
            else {
                currProp.setVal(ValueGeneratorFactory.createRandomInteger(1,100));
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
                currProp.setVal(choice);
                isStop = true;

                } catch (InputMismatchException  e) {
                    System.out.println("value is invalid, value need to be float, please try again");
                }
            }
        }
        // generate value
        else {
            //Random random = new Random();
            if (currProp.getRange() != null) {
                currProp.setVal( ValueGeneratorFactory.createRandomFloat(currProp.getRange().getFrom(),
                        currProp.getRange().getTo()));
                //currProp.setVal(currProp.getRange().getFrom() + random.nextFloat()* ((currProp.getRange().getTo()) - (currProp.getRange().getFrom())));
            } else {
                //currProp.setVal(random.nextFloat()*100.0 + 1);
                currProp.setVal(ValueGeneratorFactory.createRandomFloat(1.0f,100.0f));
            }
        }
    }

    public void setBooleanValue(PropertyInstance currProp){
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
                }
            }
        }
        // generate value
        else {
            Random random = new Random();
            currProp.setVal(ValueGeneratorFactory.createRandomBoolean().generateValue());
        }
    }

    public void setStringValue(PropertyInstance currProp){
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

}
