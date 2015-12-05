package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserFragment extends Fragment  {
    private View C;
    private ListView listProject;
    private ListView ListNote;
    private ListView historyList;
    private ArrayList<String> itemProject;
    private ArrayList<String> itemNote;
    private ArrayList<String> itemHistory;
    private ArrayAdapter<String> itemAdapterModule;
    private ArrayAdapter<String> itemAdapterNote;
    private ArrayAdapter<String> itemAdapterHistory;
    private UserClass user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        C = inflater.inflate(R.layout.fragment_user, container, false);

        Bundle extras = getArguments();
        if (extras != null) {
            user = (UserClass)extras.getSerializable("user");
        }
        putInfosInScreen();
        return C;
    }

    private void putInfosInScreen() {
        TextView title = (TextView) C.findViewById(R.id.Title);
        TextView login = (TextView) C.findViewById(R.id.loginText);
        TextView semestre = (TextView) C.findViewById(R.id.semestreText);
        TextView promo = (TextView) C.findViewById(R.id.promoText);
        TextView log = (TextView) C.findViewById(R.id.logTimeText);
        TextView city = (TextView) C.findViewById(R.id.cityText);

        title.setText("Welcome " + user.getTitle());
        login.setText(user.getLogin());
        semestre.setText(String.valueOf(user.getSemester()));
        promo.setText(user.getPromo());
        log.setText(user.getLogTime() + " h");
        city.setText(user.getCity());
        makeRequestImage("photo?token=" + user.getToken() + "&login=" + user.getLogin());
        setUpViewProject(5);
        setUpViewNote(5);
        setUpViewHistory(5);
    }


    private void setUpViewProject(int limit) {
        listProject = (ListView)C.findViewById(R.id.listProject);
        itemProject = new ArrayList<>();
        itemProject.clear();

        int all = 0;

        for (int i = 0; i < user.getProject().length() && i < limit; i++) {
            try {
                if (user.getProject().getJSONObject(i) != null)
                {
                    if (user.getProject().getJSONObject(i).has("title") && user.getProject().getJSONObject(i).has("timeline_start")
                            && user.getProject().getJSONObject(i).has("timeline_end")) {
                        all++;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        listProject.setAdapter(new myAdapter(C.getContext(), user, all));
    }

    private void setUpViewNote(int limit) {
        ListNote = (ListView)C.findViewById(R.id.listNote);
        itemNote = new ArrayList<>();
        itemNote.clear();

        itemAdapterNote = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemNote);
        ListNote.setAdapter(itemAdapterNote);

        for (int i = 0; i < user.getNotes().length() && i < limit; i++) {
            try {
                if (user.getNotes().getJSONObject(i) != null)
                {
                    itemNote.add(0, user.getNotes().getJSONObject(i).getString("title") + " : " +
                            user.getNotes().getJSONObject(i).getString("note") +
                            "\nAuteur : " + user.getNotes().getJSONObject(i).getString("noteur"));
                    itemAdapterNote.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpViewHistory(int limit) {
        historyList = (ListView)C.findViewById(R.id.historyList);
        itemHistory = new ArrayList<>();
        itemHistory.clear();

        itemAdapterHistory = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemHistory);
        historyList.setAdapter(itemAdapterHistory);

        for (int i = 0; i < user.getHistory().length() && i < limit; i++) {
            try {
                if (user.getHistory().getJSONObject(i) != null) {
                    itemHistory.add(0, stripHtml(user.getHistory().getJSONObject(i).getString("title")) + "\n\n" +
                            stripHtml(user.getHistory().getJSONObject(i).getString("content"))
                            + "\n\nPublished Date : " + user.getHistory().getJSONObject(i).getString("date"));
                    itemAdapterHistory.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String stripHtml(String html) {
        return Html.fromHtml(html).toString();
    }

    private void makeRequestImage(final String path)
    {
        final ImageView mImg = (ImageView) C.findViewById(R.id.profilImage);

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Picasso.with(C.getContext()).load(response.getString("url")).into(mImg);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;

                        ((TextView) C.findViewById(R.id.Title)).setText(error.toString());
                    }
                });
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }
}
