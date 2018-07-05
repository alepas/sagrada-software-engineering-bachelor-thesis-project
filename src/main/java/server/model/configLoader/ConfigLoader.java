package server.model.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import server.model.cards.PocDB;
import server.model.cards.ToolCardDB;
import server.model.wpc.WpcDB;
import shared.configLoader.FileLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ConfigLoader {
    private static final JSONParser parser = new JSONParser();
    public static final JSONObject jsonServerObject = getServerObject();
    private static final String language = (String) ((JSONObject)jsonServerObject.get("settings")).get("language");
    public static final JSONObject jsonServerLanguage = getServerLanguageObject();

    private static JSONObject getServerObject() {
        FileLoader.createDir("config");
        try {
            FileLoader.setFile("./config","/server/model/configLoader/serverConfig.json");
            FileReader reader = new FileReader("./config/serverConfig.json");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private static JSONObject getServerLanguageObject() {
        FileReader reader = null;
        try {
            try {
                FileLoader.createDir("config/languages/server");
                FileLoader.setFile("./config/languages/server","/server/model/configLoader/language/" + language + ".json");
                reader = new FileReader("./config/languages/server/" + language + ".json");
            } catch (FileNotFoundException e) {
                FileLoader.createDir("config/languages/server");
                FileLoader.setFile("./config/languages/server","/server/model/configLoader/language/ita.json");
                reader = new FileReader("./config/languages/server/ita.json");
            }
            return (JSONObject) parser.parse(reader);
        } catch (Exception e){
            return new JSONObject();
        }
    }

    public static void loadConfig(){
        WpcDB.getInstance().loadData();
        ToolCardDB.getInstance();
        PocDB.getInstance();
    }

}
