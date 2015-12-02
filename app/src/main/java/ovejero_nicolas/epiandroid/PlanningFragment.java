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
import java.util.Objects;

public class PlanningFragment extends Fragment {

    View            _view;
    ListView        _activity;
    CalendarView    _calendar;
    ArrayAdapter<String> _adapter;
    ArrayList<String> _listItems = new ArrayList<>();
    ArrayList<activityItem> _activityList = new ArrayList<>();
    ArrayList<activityItem> _activityListSave = new ArrayList<>();
    UserClass user;
    private boolean B0 = true;
    private boolean B1 = true;
    private boolean B2 = true;
    private boolean B3 = true;
    private boolean B4 = true;
    private boolean B5 = true;
    private boolean B6 = true;

    public final class activityItem {
        private String total_students_registered = null;
        private String titlemodule = null;
        private String codemodule = null;
        private String start = null;
        private String end = null;
        private String type_title = null;
        private String room = null;
        private String acti_title = null;
        private int semester = 0;
        boolean errors = false;

        public activityItem(activityItem o) {
            total_students_registered = o.total_students_registered;
            titlemodule = o.titlemodule;
            codemodule = o.codemodule;
            start = o.start;
            end = o.end;
            type_title = o.type_title;
            total_students_registered = o.total_students_registered;
            acti_title = o.acti_title;
            semester = o.semester;
        }

        public activityItem(JSONObject o) {
            try {
                if (o.has("total_students_registered"))
                    total_students_registered = o.getString("total_students_registered");
                if (o.has("titlemodule"))
                    titlemodule = o.getString("titlemodule");
                if (o.has("codemodule"))
                    codemodule = o.getString("codemodule");
                if (o.has("start"))
                    start = o.getString("start");
                if (o.has("end"))
                    end = o.getString("end");
                if (o.has("type_title"))
                    type_title = o.getString("type_title");
                if (o.getJSONObject("room") != null)
                    if (o.getJSONObject("room").has("code"))
                        total_students_registered = o.getJSONObject("room").getString("code");
                if (o.has("acti_title"))
                    acti_title = o.getString("acti_title");
                if (o.has("semester"))
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

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass)extras.getSerializable("user");
        }

        System.out.println(user);

        _activity = (ListView) _view.findViewById(R.id.activity);
        _adapter = new ArrayAdapter<>(_view.getContext(),
                android.R.layout.simple_list_item_1,
                _listItems);
        _activity.setAdapter(_adapter);

        _activity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activityItem tmp = _activityList.get(position);
                if (!tmp.errors) {
                    new AlertDialog.Builder(_view.getContext())
                            .setTitle(tmp.acti_title)
                            .setMessage("Titre  du module : " + tmp.titlemodule + "\n" +
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
                setActivity(year, month + 1, day);
            }
        });

        CheckBox button0 = (CheckBox)_view.findViewById(R.id.CB0);
        button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                B0 = ((CheckBox) v).isChecked();
                setUpViewProject();
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

    private void setUpViewProject() {
        ArrayList<activityItem> activityList = new ArrayList<>();
        _adapter.clear();
        int i = 0;
        for (activityItem maBite : _activityListSave) {
            if (maBite.semester == 0 && B0) {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 1 && B1) {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 2 && B2)
            {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 3 && B3)
            {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 4 && B4)
            {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 5 && B5)
            {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
            else if (maBite.semester == 6 && B6)
            {
                activityList.add(i, maBite);
                _listItems.add(maBite.titlemodule + "\n" + "Début : " + maBite.start + "\n" + "Fin : " + maBite.end);
                _adapter.notifyDataSetChanged();
                i++;
            }
        }
        _activityList = activityList;
    }

    public void setActivity(int year, int month, int day) {
        String path = "planning?token=" + user.getToken() + "&start=" + year + "-" + month + "-" + day + "&end=" + year + "-" + month + "-" + day;
        final ProgressDialog pd = ProgressDialog.show(_view.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(_view.getContext()).getRequestQueue();
        JsonArrayRequest jsObjRequest = new JsonArrayRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            _activityList.clear();
                            for (int i = 0; i < response.length(); i++) {
                                activityItem tmp = new activityItem(response.getJSONObject(i));
                                _listItems.add(tmp.titlemodule + "\n" +
                                        "Début : " + tmp.start + "\n" +
                                        "Fin : " + tmp.end);
                                _adapter.notifyDataSetChanged();
                                _activityListSave.add(i, tmp);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        _activityList = _activityListSave;
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
