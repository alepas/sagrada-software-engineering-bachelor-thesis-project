package client.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import shared.configLoader.FileLoader;
import shared.configLoader.NetworkConfigLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ClientConfigLoader {
    private static final JSONParser parser = new JSONParser();
    public static final JSONObject jsonClientObject = getClientObject();
    public static final JSONObject jsonSharedObject = getSharedObject();
    private static final String language = (String) jsonClientObject.get("language");
    public static final JSONObject jsonClientLanguage = getClientLanguageObject();
    public static final JSONObject jsonSharedLanguage = getSharedLanguageObject();

    private static JSONObject getClientObject() {
        FileLoader.createDir("clientconfig");
        try {
            FileLoader.setFile("./clientconfig","/client/configLoader/clientConfig.json");
            FileReader reader = new FileReader("./clientconfig/clientConfig.json");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private static JSONObject getSharedObject() {
        FileLoader.createDir("clientconfig");
        try {
            FileLoader.setFile("./clientconfig","/shared/config/sharedConfig.json");
            FileReader reader = new FileReader("./clientconfig/sharedConfig.json");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private static JSONObject getClientLanguageObject() {
        FileReader reader = null;
        try {
            try {
                FileLoader.createDir("clientconfig/languages");
                FileLoader.setFile("./clientconfig/languages","/client/configLoader/language/" + language + ".json");
                reader = new FileReader("./clientconfig/languages/" + language + ".json");
            } catch (FileNotFoundException e) {
                FileLoader.createDir("clientconfig/languages");
                FileLoader.setFile("./clientconfig/languages","/client/configLoader/language/ita.json");
                reader = new FileReader("./clientconfig/languages/ita.json");
            }
            return (JSONObject) parser.parse(reader);
        } catch (Exception e){
            return new JSONObject();
        }
    }

    private static JSONObject getSharedLanguageObject() {
        FileReader reader = null;
        try {
            try {
                FileLoader.createDir("clientconfig/languages/shared");
                FileLoader.setFile("./clientconfig/languages/shared","/shared/config/language/" + language + ".json");
                reader = new FileReader("./clientconfig/languages/shared/" + language + ".json");
            } catch (FileNotFoundException e) {
                FileLoader.createDir("clientconfig/languages/shared");
                FileLoader.setFile("./clientconfig/languages/shared","/shared/config/language/ita.json");
                reader = new FileReader("./clientconfig/languages/shared/ita.json");
            }
            return (JSONObject) parser.parse(reader);
        } catch (Exception e){
            return new JSONObject();
        }
    }

    public static void loadConfig(){
        try {
            JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

            shared.configLoader.LanguageLoader.loadConfig(jsonSharedLanguage);
            NetworkConfigLoader.loadConfig(jsonNetwork);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
