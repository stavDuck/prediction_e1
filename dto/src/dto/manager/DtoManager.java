package dto.manager;

import dto.Dto;

import java.util.HashMap;
import java.util.Map;

public class DtoManager {
    private Map<String, Dto> dtoXmlManager;

    public DtoManager() {
        this.dtoXmlManager = new HashMap<>();
    }
    public void addNewDtoToManager(String xmlName, Dto dto){
        dtoXmlManager.put(xmlName,dto);
    }
    public Map<String, Dto> getDtoXmlManager() {
        return dtoXmlManager;
    }

    public void setDtoXmlManager(Map<String, Dto> dtoXmlManager) {
        this.dtoXmlManager = dtoXmlManager;
    }
}
