package ovejero_nicolas.epiandroid;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by leoma on 27/11/2015.
 */
public class User {

    private JSONObject _infos;
    public String login, title, email, semester, promo, studentyear, lastname, firstname, scolaryear, location, id_promo, course_code;

    public User(JSONObject infos) {
       _infos = infos;
    }

    public boolean init () {
        try {
            login = _infos.getJSONObject("infos").getString("login");
            title = _infos.getJSONObject("infos").getString("title");
            email = _infos.getJSONObject("infos").getString("email");
            semester = _infos.getJSONObject("infos").getString("semester");
            lastname = _infos.getJSONObject("infos").getString("lastname");
            firstname = _infos.getJSONObject("infos").getString("firstname");
            studentyear = _infos.getJSONObject("infos").getString("studentyear");
            location = _infos.getJSONObject("infos").getString("location");
            id_promo = _infos.getJSONObject("infos").getString("id_promo");
            course_code = _infos.getJSONObject("infos").getString("course_code");
            promo = _infos.getJSONObject("infos").getString("promo");
            scolaryear = String.valueOf(Integer.parseInt(promo) - Integer.parseInt(studentyear));
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
