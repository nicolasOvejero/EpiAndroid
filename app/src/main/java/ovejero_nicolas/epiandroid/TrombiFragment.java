package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrombiFragment extends Fragment {
    private View C;
    private String path;
    private UserClass user;
    Spinner _1, _2;
    Button _search;
    ListView _list;
    CustomList _adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        C = inflater.inflate(R.layout.fragment_trombi, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass) extras.getSerializable("user");
        }
        _1 = (Spinner) C.findViewById(R.id.spinner1);
        _2 = (Spinner) C.findViewById(R.id.spinner2);
        _search = (Button) C.findViewById(R.id.button);
        _list = (ListView) C.findViewById(R.id.historyList);
        _search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getProfiles(_1.getSelectedItem().toString(), _2.getSelectedItem().toString());
            }
        });

        return C;
    }

    private void print(JSONArray tromb)
    {
        ListView historyList = (ListView)C.findViewById(R.id.historyList);
        String url[] = new String[tromb.length()];
        String text[] = new String[tromb.length()];

        for (int i = 0; i < tromb.length(); i++) {
            try {
                JSONObject lol = tromb.getJSONObject(i);
                url[i] = lol.getString("picture");
                text[i] = lol.getString("title");
                Log.e("lol", lol.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        historyList.setAdapter(new CustomList(C.getContext(), text, url));
    }

    public void getProfiles(String year, String location) {
        String path = "trombi?token=" + user.getToken() + "&year=" + location + "&location=" + year;
        final ProgressDialog pd = ProgressDialog.show(C.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
            "http://epitech-api.herokuapp.com/" + path, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        System.out.println(response.getJSONArray("items"));
                        if (response.has("items")) {
                            print(response.getJSONArray("items"));
                        }
                        pd.dismiss();
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;

                    System.out.println(error);
                    pd.dismiss();
                }
            }
        );
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }
}
