package ovejero_nicolas.epiandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModulesFragment extends Fragment {
    private View C;
    private String path;
    private JSONArray _obj;
    private UserClass user;
    ExpandableListAdapterPast listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        C = inflater.inflate(R.layout.fragment_modules, container, false);
        expListView = (ExpandableListView) C.findViewById(R.id.lvExp);

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass)extras.getSerializable("user");
        }
        if (user != null) {
            path = "modules?token=" + user.getToken();
        }

        if (user != null) {
            getDataFromModule("marks?token=" + user.getToken());
        }
        makeRequestProjects();

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                JSONObject toto = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                String all = "";
                for (int i = 0; i < _obj.length(); i++) {
                    try {
                        if (_obj.getJSONObject(i).getString("codemodule").contains(toto.getString("codemodule")))
                        {
                            all += _obj.getJSONObject(i).getString("title") + " : " +
                                    _obj.getJSONObject(i).getString("final_note") + "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                new AlertDialog.Builder(C.getContext())
                        .setTitle("Marks : ")
                        .setMessage((all.equals("") ? "No marks for this module !" : all))
                        .show();
                return false;
            }

        });

        return C;
        }

    private void getDataFromModule(String pathr)
    {
        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + pathr, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            _obj = response.getJSONArray("notes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        }
                });
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }

    private void setUpViewProject(JSONArray obj) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Semester 0");
        listDataHeader.add("Semester 1");
        listDataHeader.add("Semester 2");
        listDataHeader.add("Semester 3");
        listDataHeader.add("Semester 4");
        listDataHeader.add("Semester 5");
        listDataHeader.add("Semester 6");

        List<JSONObject> s0 = new ArrayList<>();
        List<JSONObject> s1 = new ArrayList<>();
        List<JSONObject> s2 = new ArrayList<>();
        List<JSONObject> s3 = new ArrayList<>();
        List<JSONObject> s4 = new ArrayList<>();
        List<JSONObject> s5 = new ArrayList<>();
        List<JSONObject> s6 = new ArrayList<>();

        if (obj != null) {
            for (int i = 0; i < obj.length(); i++) {
                try {
                    if (obj.getJSONObject(i) != null && obj.getJSONObject(i).has("semester")) {
                        switch (obj.getJSONObject(i).getString("semester")) {
                            case "0":
                                s0.add(obj.getJSONObject(i));
                                break;
                            case "1":
                                s1.add(obj.getJSONObject(i));
                                break;
                            case "2":
                                s2.add(obj.getJSONObject(i));
                                break;
                            case "3":
                                s3.add(obj.getJSONObject(i));
                                break;
                            case "4":
                                s4.add(obj.getJSONObject(i));
                                break;
                            case "5":
                                s5.add(obj.getJSONObject(i));
                                break;
                            case "6":
                                s6.add(obj.getJSONObject(i));
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

    public void makeRequestProjects()
    {
        final ProgressDialog toto = ProgressDialog.show(C.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            setUpViewProject(response.getJSONArray("modules"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        listAdapter = new ExpandableListAdapterPast(C.getContext(), listDataHeader,
                                listDataChild, user);
                        expListView.setAdapter(listAdapter);
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
}
