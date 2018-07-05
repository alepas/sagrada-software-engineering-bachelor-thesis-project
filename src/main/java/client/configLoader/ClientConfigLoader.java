package client.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import shared.configLoader.FileLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ClientConfigLoader {
    private static final JSONParser parser = new JSONParser();
    public static final JSONObject jsonClientObject = getClientObject();
    private static final String language = (String) jsonClientObject.get("language");
    public static final JSONObject jsonClientLanguage = getClientLanguageObject();

    private static JSONObject getClientObject() {
        FileLoader.createDir("config");
        try {
            FileLoader.setFile("./config","/client/configLoader/clientConfig.json");
            FileReader reader = new FileReader("./config/clientConfig.json");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private static JSONObject getClientLanguageObject() {
        try {
            FileReader reader;
            try {
                FileLoader.createDir("config/languages/client");
                FileLoader.setFile("./config/languages/client","/client/configLoader/language/" + language + ".json");
                reader = new FileReader("./config/languages/client/" + language + ".json");
            } catch (FileNotFoundException e) {
                FileLoader.createDir("config/languages/client");
                FileLoader.setFile("./config/languages/client","/client/configLoader/language/ita.json");
                reader = new FileReader("./config/languages/client/ita.json");
            }
            return (JSONObject) parser.parse(reader);
        } catch (Exception e){
            return new JSONObject();
        }
    }

}
