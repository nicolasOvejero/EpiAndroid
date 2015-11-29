package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import java.util.Objects;

public class ProjectsFragment extends Fragment {
    private View C;
    private String path;
    private ListView listProject;
    private ArrayList<String> itemProject;
    private ArrayAdapter<String> itemAdapterProject;
    private boolean AllProject = true;
    private JSONArray obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        C = inflater.inflate(R.layout.fragment_projects, container, false);

        Bundle args = getArguments();
        path = "projects?token=" + args.getString("token");

        CheckBox button = (CheckBox)C.findViewById(R.id.checkAll);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AllProject = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });

        makeRequestProjects();

        return C;
    }

    private void setUpViewProject() {
        listProject = (ListView)C.findViewById(R.id.project);
        itemProject = new ArrayList<>();
        itemProject.clear();
        itemAdapterProject = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemProject);
        listProject.setAdapter(itemAdapterProject);

        for (int i = 0; i < obj.length(); i++) {
            try {
                if ( !AllProject ) {
                    if (obj.getJSONObject(i) != null && !Objects.equals(obj.getJSONObject(i).getString("registered"), "1"))
                    {
                        itemProject.add(0, obj.getJSONObject(i).getString("codemodule") + "\n" +
                                obj.getJSONObject(i).getString("acti_title") + "\n" +
                                "End of Project : " + obj.getJSONObject(i).getString("end_acti") + "\n");
                        itemAdapterProject.notifyDataSetChanged();
                    }
                }
                else {
                    if (obj.getJSONObject(i) != null) {
                        itemProject.add(0, obj.getJSONObject(i).getString("codemodule") + "\n" +
                                obj.getJSONObject(i).getString("acti_title") + "\n" +
                                "End of Project : " + obj.getJSONObject(i).getString("end_acti"));
                        itemAdapterProject.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                        obj = response;
                        setUpViewProject();
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
