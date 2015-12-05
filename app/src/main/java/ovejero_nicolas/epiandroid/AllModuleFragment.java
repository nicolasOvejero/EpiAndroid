package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllModuleFragment extends Fragment {
    private View C;
    private String path;
    private boolean B0 = true;
    private boolean B1 = true;
    private boolean B2 = true;
    private boolean B3 = true;
    private boolean B4 = true;
    private boolean B5 = true;
    private boolean B6 = true;
    private UserClass user;
    private JSONArray obj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        C = inflater.inflate(R.layout.fragment_all_module, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass)extras.getSerializable("user");
        }
        if (user != null) {
            path = "allmodules?token=" + user.getToken() + "&location=FR/LYN&scolaryear=2015&course=bachelor/classic";
        }

        CheckBox button0 = (CheckBox)C.findViewById(R.id.CB0);
        button0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B0 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button1 = (CheckBox)C.findViewById(R.id.CB1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B1 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button2 = (CheckBox)C.findViewById(R.id.CB2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B2 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button3 = (CheckBox)C.findViewById(R.id.CB3);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B3 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button4 = (CheckBox)C.findViewById(R.id.CB4);
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B4 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button5 = (CheckBox)C.findViewById(R.id.CB5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B5 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button6 = (CheckBox)C.findViewById(R.id.CB6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B6 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });

        makeRequestProjects();
        return C;
    }

    private void setUpViewProject() {
        ListView list = (ListView) C.findViewById(R.id.allmodules);
        ArrayList<String> item = new ArrayList<>();
        item.clear();
        modulesAdapter adapter = new modulesAdapter(C.getContext(), obj);
        int all = 0;

        for (int i = 0; i < obj.length(); i++) {
            try {
                if (obj.getJSONObject(i) != null)
                {
                    if (obj.getJSONObject(i).getInt("semester") == 0 && B0)
                        adapter.setB0(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 1 && B1)
                        adapter.setB1(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 2 && B2)
                        adapter.setB2(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 3 && B3)
                        adapter.setB3(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 4 && B4)
                        adapter.setB4(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 5 && B5)
                        adapter.setB5(true);
                    else if (obj.getJSONObject(i).getInt("semester") == 6 && B6)
                        adapter.setB6(true);
                    else
                        all--;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            all++;
        }
        adapter.setCount(all);
        list.setAdapter(adapter);
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
                            obj = response.getJSONArray("items");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
