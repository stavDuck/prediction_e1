package menu;
import engine.simulation.Simulation;
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
        Simulation simulation, tempSimlate;

        System.out.println("Welcome to Prediction !");
        // main menu
        while (!isStop) {
            printMainMenu();
            // get choice from user
            choice = scanner.nextInt();

           switch (choice){
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
                        }
                        catch (RuntimeException e){
                            System.out.println(e.getMessage() +
                                    "\n please try to fix the issue and reload the xml again.");
                        }
                    }
                    else
                       System.out.println("File is not load because file path is not valid,file neex to be XML type,  " +
                               "the last valid file is loaded");
                   break;
               case(OPTION_TWO):
                   if(isFileHasValue){

                   }
                   else
                       System.out.println("File is not load yet. please use option one first and load a new file.");

                   break;
               case(OPTION_THREE):
                   break;
               case(OPTION_FOUR):
                   break;
               case(OPTION_FIVE):
                   // stopint the application
                   isStop = true;
                   break;
               default:
                   System.out.println("Invalid option number, please try again with the values in the menu only");

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

        Simulation simulation= new Simulation("C:\\Users\\USER\\IdeaProjects\\prediction_e1\\engine\\src\\resources\\master-ex1.xml");
        //Simulation simulation = new Simulation("C:/study/java/prediction/engine/src/resources/ex1-cigarets.xml");
        return simulation;
    }
}
