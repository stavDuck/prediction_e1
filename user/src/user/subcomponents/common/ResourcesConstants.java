package user.subcomponents.common;

import com.google.gson.Gson;

public class ResourcesConstants {
    //private static final String BASE_PACKAGE = "/screens";
    private static final String BASE_PACKAGE = "/user/subcomponents";
    public static final String APP_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +"/app/app.fxml";

    // for actions fxml
    public static final String CALCULATION_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Calculation/CalculateComponent.fxml";
    public static final String MULTIPLE_CONDITION_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Condition/multiple/MultipleConditionComponent.fxml";
    public static final String SINGEL_CONDITION_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Condition/single/SingleConditionComponent.fxml";
    public static final String DECREASE_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Decrease/DecreaseComponent.fxml";
    public static final String INCREACE_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Increase/IncreaseComponent.fxml";
    public static final String KILL_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Kill/KillComponent.fxml";
    public static final String PROXIMITY_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Proximity/ProximityComponent.fxml";
    public static final String REPLACE_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Replace/ReplaceComponent.fxml";
    public static final String SET_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +
            "/actions/Set/SetComponent.fxml";





    // login constance
    public final static String LINE_SEPARATOR = System.getProperty("line.separator");
    public final static String JHON_DOE = "<Anonymous>";
    public final static int REFRESH_RATE = 2000;
    public final static String CHAT_LINE_FORMATTING = "%tH:%tM:%tS | %.10s: %s%n";

    // fxml locations
    public final static String LOGIN_PAGE_FXML_RESOURCE_LOCATION = "/user/login/login.fxml";


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/prediction-web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_PAGE = FULL_SERVER_PATH + "/loginResponse";
    public final static String LOGOUT = FULL_SERVER_PATH + "/logoutUserResponse";
    public final static String GET_DTO = FULL_SERVER_PATH + "/dtoResponse";
    public final static String GET_DTO_XML_MAP = FULL_SERVER_PATH + "/getXmlsServlet";
    public final static String NEW_EXECUTION_WINDOW = FULL_SERVER_PATH + "/executeNewSimulationServlet";
    public final static String UPLOAD_XML = FULL_SERVER_PATH + "/uploadXml";
    public final static String ADD_NEW_REQUEST_FROM_USER = FULL_SERVER_PATH + "/addNewRequestFromUser";
    public final static String GET_USER_REQUESTS = FULL_SERVER_PATH + "/getUserRequests";
    public final static String GENERATE_RANDOM = FULL_SERVER_PATH + "/generateRandom";
    public final static String RUN_SIMULATION = FULL_SERVER_PATH + "/runSimulation";
    public final static String UPDATE_REQUEST_AFTER_EXECUTION = FULL_SERVER_PATH + "/updateRequestAfterExecution";

    // GSON instance
    public final static Gson GSON_INSTANCE = new Gson();
}
