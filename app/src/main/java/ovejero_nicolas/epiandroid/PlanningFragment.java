package ovejero_nicolas.epiandroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PlanningFragment extends Fragment {

    View            _view;
    ListView        _activity;
    CalendarView    _calendar;
    ArrayAdapter<String> _adapter;
    ArrayList<String> _listItems = new ArrayList<>();
    ArrayList<activityItem> _activityList = new ArrayList<activityItem>();
    String _token;

    public final class activityItem {

        public String total_students_registered, titlemodule, codemodule, start, end, type_title, room, acti_title;
        boolean errors = false;

        public activityItem(JSONObject o) {
            try {
                total_students_registered = o.getString("total_students_registered");
                titlemodule = o.getString("titlemodule");
                codemodule = o.getString("codemodule");
                start = o.getString("start");
                end = o.getString("end");
                type_title = o.getString("type_title");
                total_students_registered = o.getJSONObject("room").getString("code");
                acti_title = o.getString("acti_title");
            } catch (Exception e) {
                errors = true;
                Log.e("ERROR", e.getMessage());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _view = inflater.inflate(R.layout.fragment_planning, container, false);
        _token = getArguments().getString("token");
        _activity = (ListView) _view.findViewById(R.id.activity);
        _adapter = new ArrayAdapter<>(_view.getContext(),
                android.R.layout.simple_list_item_1,
                _listItems);
        _activity.setAdapter(_adapter);
        _activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("POSITION", String.valueOf(position));
                activityItem tmp = _activityList.get(position);
                if (tmp.errors)
                    new AlertDialog.Builder(_view.getContext())
                            .setTitle(tmp.acti_title)
                            .setMessage("Titre : " + tmp.acti_title + "\n" +
                                    "Code du module : " + tmp.codemodule + "\n" +
                                    "Titre du module : " + tmp.titlemodule + "\n" +
                                    "Etudiants inscrits : " + tmp.total_students_registered + "\n" +
                                    "Type : " + tmp.type_title + "\n" +
                                    "Salle : " + tmp.room + "\n" +
                                    "Début : " + tmp.start + "\n" +
                                    "Fin : " + tmp.end + "\n")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
            }
        });
        _calendar = (CalendarView) _view.findViewById(R.id.calendar);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(_calendar.getDate()));
        setActivity(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
        _calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
                _listItems.clear();
                setActivity(year, month, day);
            }
        });

        return _view;
    }

    public void setActivity(int year, int month, int day) {
        String path = "planning?token=" + _token + "&start=" + year + "-" + month + "-" + day + "&end=" + year + "-" + month + "-" + day;
        Log.e("PATH", path);
        final ProgressDialog pd = ProgressDialog.show(_view.getContext(), "Chargement...", "Merci de patienter.");
        RequestQueue queue = MySingleton.getInstance(_view.getContext()).getRequestQueue();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            _activityList.clear();
                            for (int i = 0; response.getJSONObject(i) != null; i++) {
                                activityItem tmp = new activityItem(response.getJSONObject(i));
                                _activityList.add(i, tmp);
                                _listItems.add(tmp.acti_title + "\n" + "Début : " + tmp.start + "\n" + "Fin : " + tmp.end);
                                _adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
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
    }
}
