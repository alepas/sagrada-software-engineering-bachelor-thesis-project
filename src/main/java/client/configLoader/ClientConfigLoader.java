package client.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shared.configLoader.NetworkConfigLoader;

import java.io.FileReader;
import java.io.IOException;

public class ClientConfigLoader {

    public static void loadConfig(){
        JSONParser parser = new JSONParser();
        try {
//            JSONObject jsonServerObject = (JSONObject) parser.parse(new FileReader("src/main/resources/config/server/serverConfig.json"));
            JSONObject jsonSharedObject = (JSONObject) parser.parse(new FileReader("src/main/resources/shared/config/sharedConfig.json"));

            JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

            NetworkConfigLoader.loadConfig(jsonNetwork);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}
