package ovejero_nicolas.epiandroid;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrombiFragment extends Fragment {

    private View _view;
    private UserClass user;
    private Spinner _year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_trombi, container, false);

        return _view;
/*
        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass) extras.getSerializable("user");
        }
        _year = (Spinner) _view.findViewById(R.id.spinner_year);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(_view.getContext(),
                R.array.res_year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _year.setAdapter(adapter);
        return _view;
    }
/*
    public void setActivity(int year, int month, int day) {
        String path = "planning?token=" + user.getToken() + "&start=" + year + "-" + month + "-" + day + "&end=" + year + "-" + month + "-" + day;
        final ProgressDialog pd = ProgressDialog.show(_view.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(_view.getContext()).getRequestQueue();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e("LOL", error.getMessage());
                        pd.dismiss();
                    }
                });
        MySingleton.getInstance(_view.getContext()).addToRequestQueue(jsObjRequest);
*/
    }
}
