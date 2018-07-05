package shared.configLoader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SharedConfigLoader {
    private static final JSONParser parser = new JSONParser();
    public static final JSONObject jsonSharedObject = getSharedObject();
    private static final String language = (String) jsonSharedObject.get("language");
    public static final JSONObject jsonSharedLanguage = getSharedLanguageObject();

    private static JSONObject getSharedObject() {
        FileLoader.createDir("config");
        try {
            FileLoader.setFile("./config","/shared/config/sharedConfig.json");
            FileReader reader = new FileReader("./config/sharedConfig.json");
            return (JSONObject) parser.parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    private static JSONObject getSharedLanguageObject() {
        FileReader reader = null;
        try {
            try {
                FileLoader.createDir("config/languages/shared");
                FileLoader.setFile("./config/languages/shared","/shared/config/language/" + language + ".json");
                reader = new FileReader("./config/languages/shared/" + language + ".json");
            } catch (FileNotFoundException e) {
                FileLoader.createDir("config/languages/shared");
                FileLoader.setFile("./config/languages/shared","/shared/config/language/ita.json");
                reader = new FileReader("./config/languages/shared/ita.json");
            }
            return (JSONObject) parser.parse(reader);
        } catch (Exception e){
            return new JSONObject();
        }
    }
}
