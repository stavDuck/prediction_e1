package admin.subcomponents.common;

public class ResourcesConstants {
    //private static final String BASE_PACKAGE = "/screens";
    private static final String BASE_PACKAGE = "/admin/subcomponents";
    public static final String APP_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +"/app/app.fxml";
    public static final String ERROR_LOGIN_FXML_INCLUDE_RESOURCE = BASE_PACKAGE +"/app/error/errorLogInPage.fxml";


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


    // Server resources locations
    public final static String BASE_DOMAIN = "localhost";
    private final static String BASE_URL = "http://" + BASE_DOMAIN + ":8080";
    private final static String CONTEXT_PATH = "/prediction-web";
    private final static String FULL_SERVER_PATH = BASE_URL + CONTEXT_PATH;

    public final static String LOGIN_ADMIN = FULL_SERVER_PATH + "/loginAdminResponse";
    public final static String LOGOUT_ADMIN = FULL_SERVER_PATH + "/logoutAdminResponse";

    public final static String UPLOAD_XML = FULL_SERVER_PATH + "/uploadXml";

    // GSON instance
    // public final static Gson GSON_INSTANCE = new Gson();
}
