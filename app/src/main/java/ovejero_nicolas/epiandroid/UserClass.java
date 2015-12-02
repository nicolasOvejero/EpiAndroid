package ovejero_nicolas.epiandroid;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

public class UserClass implements Serializable {
    private String login;
    private String token;
    private String title;
    private String promo;
    private String logTime;
    private int semester;
    private String city;
    private JSONArray project;
    private JSONArray notes;
    private JSONArray activity;
    private JSONArray modules;
    private JSONArray history;
    private JSONObject infos;
    private Bitmap picture;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPromo() {
        return promo;
    }

    public void setPromo(String promo) {
        this.promo = promo;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public JSONArray getProject() {
        return project;
    }

    public void setProject(JSONArray project) {
        this.project = project;
    }

    public JSONArray getNotes() {
        return notes;
    }

    public void setNotes(JSONArray notes) {
        this.notes = notes;
    }

    public JSONArray getActivity() {
        return activity;
    }

    public void setActivity(JSONArray activity) {
        this.activity = activity;
    }

    public JSONArray getModules() {
        return modules;
    }

    public void setModules(JSONArray modules) {
        this.modules = modules;
    }

    public JSONArray getHistory() {
        return history;
    }

    public void setHistory(JSONArray history) {
        this.history = history;
    }

    public JSONObject getInfos() {
        return infos;
    }

    public void setInfos(JSONObject infos) {
        this.infos = infos;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
