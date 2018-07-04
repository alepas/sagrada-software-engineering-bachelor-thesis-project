package client.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import shared.configLoader.FileLoader;
import shared.configLoader.NetworkConfigLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ClientConfigLoader {

    public static void loadConfig(){
        FileLoader.createDir("clientconfig");
        JSONParser parser = new JSONParser();
        try {
            FileLoader.setFile("./clientconfig","/client/configLoader/clientConfig.json");
            FileLoader.setFile("./clientconfig","/shared/config/sharedConfig.json");

            JSONObject jsonClientObject = (JSONObject) parser.parse(new FileReader("./clientconfig/clientConfig.json"));
            JSONObject jsonSharedObject = (JSONObject) parser.parse(new FileReader("./clientconfig/sharedConfig.json"));

            String language = (String) jsonClientObject.get("language");
            JSONObject jsonClientLanguage; JSONObject jsonSharedLanguage;
            try {
                jsonClientLanguage = (JSONObject) parser.parse(new FileReader("src/main/resources/client/configLoader/language/" + language + ".json"));
                jsonSharedLanguage = (JSONObject) parser.parse(new FileReader("src/main/resources/shared/config/language/" + language + ".json"));
            }catch (FileNotFoundException e){
                System.out.println("Language not found. Revert back to Italian");
                jsonClientLanguage = (JSONObject) parser.parse(new FileReader("src/main/resources/client/configLoader/language/ita.json"));
                jsonSharedLanguage = (JSONObject) parser.parse(new FileReader("src/main/resources/shared/config/language/ita.json"));
            }

            JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

            LanguageLoader.loadConfig(jsonClientLanguage);
            shared.configLoader.LanguageLoader.loadConfig(jsonSharedLanguage);
            NetworkConfigLoader.loadConfig(jsonNetwork);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
