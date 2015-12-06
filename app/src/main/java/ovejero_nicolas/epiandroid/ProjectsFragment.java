package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProjectsFragment extends Fragment {
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private View C;
    private String path;
    private UserClass user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        C = inflater.inflate(R.layout.fragment_projects, container, false);

        expListView = (ExpandableListView) C.findViewById(R.id.lvExp);

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass)extras.getSerializable("user");
        }
        if (user != null) {
            path = "projects?token=" + user.getToken();
        }

        makeRequestProjects();

        return C;
    }

    public void makeRequestProjects()
    {
        final ProgressDialog toto = ProgressDialog.show(C.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        prepareListData(response);
//                        listAdapter = new ExpandableListAdapter(C.getContext(), listDataHeader, listDataChild);
//                        expListView.setAdapter(listAdapter);
                        toto.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        toto.dismiss();
                    }
                });
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }

    private void prepareListData(JSONArray obj) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Semester 0");
        listDataHeader.add("Semester 1");
        listDataHeader.add("Semester 2");
        listDataHeader.add("Semester 3");
        listDataHeader.add("Semester 4");
        listDataHeader.add("Semester 5");
        listDataHeader.add("Semester 6");

        List<String> s0 = new ArrayList<>();
        List<String> s1 = new ArrayList<>();
        List<String> s2 = new ArrayList<>();
        List<String> s3 = new ArrayList<>();
        List<String> s4 = new ArrayList<>();
        List<String> s5 = new ArrayList<>();
        List<String> s6 = new ArrayList<>();

        if (obj != null) {
            for (int i = 0; i < obj.length(); i++) {
                try {
                    if (obj.getJSONObject(i) != null && obj.getJSONObject(i).has("semester")) {
                        System.out.println("123");
                        switch (obj.getJSONObject(i).getString("semester")) {
                            case "0":
                                s0.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "1":
                                s1.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "2":
                                s2.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "3":
                                s3.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "4":
                                s4.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "5":
                                s5.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                            case "6":
                                s6.add(obj.getJSONObject(i).getString("codemodule") + "\n" +
                                        obj.getJSONObject(i).getString("acti_title") + "\n" +
                                        "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        listDataChild.put(listDataHeader.get(0), s0);
        listDataChild.put(listDataHeader.get(1), s1);
        listDataChild.put(listDataHeader.get(2), s2);
        listDataChild.put(listDataHeader.get(3), s3);
        listDataChild.put(listDataHeader.get(4), s4);
        listDataChild.put(listDataHeader.get(5), s5);
        listDataChild.put(listDataHeader.get(6), s6);

    }
}
