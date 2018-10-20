package cameronjump.monocle;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateJSON {

    public static String checkin(String user, String code) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "checkin");
        JSONObject data = new JSONObject();
        data.put("user",user);
        data.put("code",code);
        String string = data.toString();
        json.put("data",string);
        return json.toString();
    }

    public static String getCurrentQuestion(String id) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "getCurrentQuestion");
        JSONObject data = new JSONObject();
        data.put("id",id);
        String string = data.toString();
        json.put("data",string);
        return json.toString();
    }

    public static String answerQuestion(String id, String name, String answer) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("type", "answerQuestion");
        JSONObject data = new JSONObject();
        data.put("id",id);
        data.put("name",name);
        data.put("answer",answer);
        String string = data.toString();
        json.put("data",string);
        return json.toString();
    }


}
