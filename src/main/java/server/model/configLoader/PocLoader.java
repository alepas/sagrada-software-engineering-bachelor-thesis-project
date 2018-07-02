package server.model.configLoader;

import org.json.simple.JSONObject;
import static server.constants.POCConstants.*;

public class PocLoader {

    public static void loadConfig(JSONObject jsonPoc) {
        POC1_ID = (String) jsonPoc.get("POC1_ID");
        POC2_ID = (String) jsonPoc.get("POC2_ID");
        POC3_ID = (String) jsonPoc.get("POC3_ID");
        POC4_ID = (String) jsonPoc.get("POC4_ID");
        POC5_ID = (String) jsonPoc.get("POC5_ID");
        POC6_ID = (String) jsonPoc.get("POC6_ID");
        POC7_ID = (String) jsonPoc.get("POC7_ID");
        POC8_ID = (String) jsonPoc.get("POC8_ID");
        POC9_ID = (String) jsonPoc.get("POC9_ID");
        POC10_ID = (String) jsonPoc.get("POC10_ID");

        POC1_SCORE = ((Long) jsonPoc.get("POC1_SCORE")).intValue();
        POC2_SCORE = ((Long) jsonPoc.get("POC2_SCORE")).intValue();
        POC3_SCORE = ((Long) jsonPoc.get("POC3_SCORE")).intValue();
        POC4_SCORE = ((Long) jsonPoc.get("POC4_SCORE")).intValue();
        POC5_SCORE = ((Long) jsonPoc.get("POC5_SCORE")).intValue();
        POC6_SCORE = ((Long) jsonPoc.get("POC6_SCORE")).intValue();
        POC7_SCORE = ((Long) jsonPoc.get("POC7_SCORE")).intValue();
        POC8_SCORE = ((Long) jsonPoc.get("POC8_SCORE")).intValue();
        POC9_SCORE = ((Long) jsonPoc.get("POC9_SCORE")).intValue();
        POC10_SCORE = ((Long) jsonPoc.get("POC10_SCORE")).intValue();

    }
}
