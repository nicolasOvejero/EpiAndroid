package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

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

public class TrombiFragment extends Fragment {
    private View C;
    private UserClass user;
    private Spinner _1, _2;
    private Button _search;
    private ListView _list;
    private int off = 0;
    private int total;
    private ArrayList<String> url = new ArrayList<>();
    private ArrayList<String> text = new ArrayList<>();

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
                url.clear();
                text.clear();
                off = 0;
                getProfiles(_1.getSelectedItem().toString(), _2.getSelectedItem().toString(), 0);
            }
        });

        return C;
    }

    private void print(JSONArray tromb, String year, String location)
    {

        for (int i = 0; i < tromb.length(); i++) {
            try {
                JSONObject lol = tromb.getJSONObject(i);
                url.add(i + off, lol.getString("picture"));
                text.add(i + off, lol.getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        off += 48;
        if (off < total)
            getProfiles(year, location, off);
        _list.setAdapter(new CustomList(C.getContext(), text, url));
    }

    public void getProfiles(final String year, final String location, int offset) {
        String path = "trombi?token=" + user.getToken() + "&year=" + location + "&location=" + year + "&offset=" + offset;

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
            "http://epitech-api.herokuapp.com/" + path, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        total = response.getInt("total");
                        if (response.has("items")) {
                            print(response.getJSONArray("items"), year, location);
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    System.out.println(error);
                }
            }
        );
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }
}
