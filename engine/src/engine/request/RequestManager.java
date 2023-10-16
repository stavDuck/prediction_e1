package engine.request;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class RequestManager {
    // request manager will contain group order by user name,
    // and in every group can search request by requstID
    private Map<String, Map<Integer,Request>> requestManager;

    public RequestManager() {
        requestManager = new HashMap<>();
    }

    // add new userName innerMap
    public void addNewUserRequestMap(String userName){
        Map<Integer,Request> innerMap = new LinkedHashMap<>();
        requestManager.put(userName, innerMap);
    }

    // add new request according to userName
    public void addNewRequestForUser(String userName, Request request){
        // if user Map doesn't exist - add new map for this userName
        if(requestManager.get(userName) == null) {
            addNewUserRequestMap(userName);
        }
        requestManager.get(userName).put(request.getRequestId(), request);
    }

    // get requst by ID and,or userName
    public Request getRequestById(Integer id){
        return getRequestByIdAndUserName(id, null);
    }
    public Request getRequestByIdAndUserName(Integer id, String userName){
        if(userName != null){
            return requestManager.get(userName).get(id);
        }
        else {
            for(Map<Integer,Request> currInnerMap : requestManager.values()){
                if(currInnerMap.get(id) != null){
                    return currInnerMap.get(id);
                }
            }
        }

        return null;
    }

    // get map of all the request for a spesific user
    public Map<Integer,Request> getAllRequestsForAUser(String userName){
        return requestManager.get(userName);
    }

    // get the map of all requests for all users
    public Map<String, Map<Integer,Request>> getAllRequestsInManager(){
        return requestManager;
    }
}
