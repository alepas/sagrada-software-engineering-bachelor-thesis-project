package client.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shared.configLoader.FileLoader;
import shared.configLoader.NetworkConfigLoader;

import java.io.FileReader;
import java.io.IOException;

public class ClientConfigLoader {

    public static void loadConfig(){
        FileLoader.createDir("clientconfig");
        JSONParser parser = new JSONParser();
        try {
//            JSONObject jsonServerObject = (JSONObject) parser.parse(new FileReader("src/main/resources/config/server/serverConfig.json"));
            FileLoader.setFile("./clientconfig","/shared/config/sharedConfig.json");

            JSONObject jsonSharedObject = (JSONObject) parser.parse(new FileReader("./clientconfig/sharedConfig.json"));

            JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

            NetworkConfigLoader.loadConfig(jsonNetwork);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
