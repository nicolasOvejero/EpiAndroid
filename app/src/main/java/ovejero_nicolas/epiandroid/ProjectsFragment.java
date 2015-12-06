package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProjectsFragment extends Fragment {
    ExpandableListAdapterProject listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<JSONObject>> listDataChild;
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
                        listAdapter = new ExpandableListAdapterProject(C.getContext(), listDataHeader,
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

    private void prepareListData(JSONArray obj) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Not Registered");
        listDataHeader.add("Registered");

        List<JSONObject> reg = new ArrayList<>();
        List<JSONObject> notreg = new ArrayList<>();

        if (obj != null) {
            for (int i = 0; i < obj.length(); i++) {
                try {
                    if (obj.getJSONObject(i) != null && obj.getJSONObject(i).has("registered")) {
                        switch (obj.getJSONObject(i).getString("registered")) {
                            case "0":
                                notreg.add(obj.getJSONObject(i));
                                break;
                            case "1":
                                reg.add(obj.getJSONObject(i));
                                break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        listDataChild.put(listDataHeader.get(0), notreg);
        listDataChild.put(listDataHeader.get(1), reg);
    }
}
