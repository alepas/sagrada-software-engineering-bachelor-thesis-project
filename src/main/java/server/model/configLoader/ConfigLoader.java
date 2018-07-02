package server.model.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.constants.UserDBConstants;
import server.model.cards.PocDB;
import server.model.cards.ToolCardDB;
import server.model.wpc.WpcDB;
import shared.configLoader.NetworkConfigLoader;

import java.io.FileReader;
import java.io.IOException;

public class ConfigLoader {

    public static void loadConfig(){
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonServerObject = (JSONObject) parser.parse(new FileReader("src/main/resources/server/model/configLoader/serverConfig.json"));
            JSONObject jsonSharedObject = (JSONObject) parser.parse(new FileReader("src/main/resources/shared/config/sharedConfig.json"));

            JSONObject jsonSettings = (JSONObject) jsonServerObject.get("settings");
            String language = (String) jsonSettings.get("language");
            JSONObject jsonLanguage = (JSONObject) parser.parse(new FileReader("src/main/resources/server/model/configLoader/language/" + language + ".json"));
            UserDBConstants.PATH_DB_FILE = (String) jsonSettings.get("userdb_path");

            JSONObject jsonGame = (JSONObject) jsonServerObject.get("game");
            JSONObject jsonDiceBag = (JSONObject) jsonServerObject.get("dicebag");
            JSONObject jsonPoc = (JSONObject) jsonServerObject.get("poc");
            JSONObject jsonToolcard = (JSONObject) jsonServerObject.get("toolcard");
            JSONObject jsonWpc = (JSONObject) jsonServerObject.get("wpc");

            JSONObject jsonNetwork = (JSONObject) jsonSharedObject.get("network");

            GameConfigLoader.loadConfig(jsonGame);
            DicebagConfigLoader.loadConfig(jsonDiceBag);
            PocLoader.loadConfig(jsonPoc);
            ToolcardLoader.loadConfig(jsonToolcard);
            WpcConfigLoader.loadConfig(jsonWpc);
            NetworkConfigLoader.loadConfig(jsonNetwork);
            LanguageLoader.loadConfig(jsonLanguage);

        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        WpcDB.getInstance();
        ToolCardDB.getInstance();
        PocDB.getInstance();
    }

}
