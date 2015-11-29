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
import android.widget.CheckBox;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
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
    ArrayList<activityItem> _activityList = new ArrayList<>();
    String _token;
    private boolean B1 = true;
    private boolean B2 = true;
    private boolean B3 = true;
    private boolean B4 = true;
    private boolean B5 = true;
    private boolean B6 = true;

    public final class activityItem {

        public String total_students_registered, titlemodule, codemodule, start, end, type_title, room, acti_title;
        int semester;
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
                semester = o.getInt("semester");
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
                activityItem tmp = _activityList.get(position);
                if (tmp.errors)
                    new AlertDialog.Builder(_view.getContext())
                            .setTitle(tmp.acti_title)
                            .setMessage("Titre  du module : " + tmp.titlemodule+ "\n" +
                                    "Code du module : " + tmp.codemodule + "\n" +
                                    "Titre : " + tmp.acti_title + "\n" +
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

        CheckBox button1 = (CheckBox)_view.findViewById(R.id.CB1);
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B1 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button2 = (CheckBox)_view.findViewById(R.id.CB2);
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B2 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button3 = (CheckBox)_view.findViewById(R.id.CB3);
        button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B3 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button4 = (CheckBox)_view.findViewById(R.id.CB4);
        button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                B4 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button5 = (CheckBox)_view.findViewById(R.id.CB5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B5 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });
        CheckBox button6 = (CheckBox)_view.findViewById(R.id.CB6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B6 = ((CheckBox) v).isChecked();
                setUpViewProject();
            }
        });

        return _view;
    }

    private void setUpViewProject()
    {
        _adapter.clear();
        for (activityItem maBite : _activityList) {
            if (maBite.semester == 1 && B1) {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
            else if (maBite.semester == 2 && B2)
            {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
            else if (maBite.semester == 3 && B3)
            {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
            else if (maBite.semester == 4 && B4)
            {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
            else if (maBite.semester == 5 && B5)
            {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
            else if (maBite.semester == 6 && B6)
            {
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
            }
        }
    }

    public void setActivity(int year, int month, int day) {
        String path = "planning?token=" + _token + "&start=" + year + "-" + month + "-" + day + "&end=" + year + "-" + month + "-" + day;
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
                                setUpViewProject();
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
