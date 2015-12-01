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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class UserFragment extends Fragment  {
    private String path;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        C = inflater.inflate(R.layout.fragment_user, container, false);

        Bundle args = getArguments();
        path = "infos?token=" + args.getString("token");
        makeRequestUserInfo();
        return C;
    }

    private void putInfosInScreen(JSONObject obj) {
        TextView title = (TextView) C.findViewById(R.id.Title);
        TextView login = (TextView) C.findViewById(R.id.loginText);
        TextView semestre = (TextView) C.findViewById(R.id.semestreText);
        TextView promo = (TextView) C.findViewById(R.id.promoText);
        TextView log = (TextView) C.findViewById(R.id.logTimeText);
        TextView city = (TextView) C.findViewById(R.id.cityText);
        Bundle args = getArguments();

        try {
            title.setText("Welcome " + obj.getJSONObject("infos").getString("title"));
            login.setText(obj.getJSONObject("infos").getString("login"));
            semestre.setText(obj.getJSONObject("infos").getString("semester"));
            promo.setText(obj.getJSONObject("infos").getString("promo"));
            log.setText((obj.getJSONArray("current").getJSONObject(0).getString("active_log")).substring(0, 5) + " h");
            city.setText(obj.getJSONObject("infos").getString("location"));
            makeRequestImage("photo?token=" + args.getString("token") + "&login=" + obj.getJSONObject("infos").getString("login"));
            setUpViewProject(obj.getJSONObject("board").getJSONArray("projets"), 5);
            setUpViewNote(obj.getJSONObject("board").getJSONArray("notes"), 5);
            setUpViewHistory(obj.getJSONArray("history"), 5);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setUpViewProject(JSONArray obj, int limit) {
        listProject = (ListView)C.findViewById(R.id.listProject);
        itemProject = new ArrayList<>();
        itemProject.clear();
        itemAdapterModule = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemProject);
        listProject.setAdapter(itemAdapterModule);

        for (int i = 0; i < obj.length() && i < limit; i++) {
            try {
                if (obj.getJSONObject(i) != null)
                {
                    if (obj.getJSONObject(i).has("title") && obj.getJSONObject(i).has("timeline_start")
                            && obj.getJSONObject(i).has("timeline_end")) {
                        itemProject.add(0, obj.getJSONObject(i).getString("title") + "\nFrom : " +
                                (obj.getJSONObject(i).getString("timeline_start")).substring(0, 10) +
                                " To " + (obj.getJSONObject(i).getString("timeline_end")).substring(0, 10));
                        itemAdapterModule.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpViewNote(JSONArray obj, int limit) {
        ListNote = (ListView)C.findViewById(R.id.listNote);
        itemNote = new ArrayList<>();
        itemNote.clear();

        itemAdapterNote = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemNote);
        ListNote.setAdapter(itemAdapterNote);

        for (int i = 0; i < obj.length() && i < limit; i++) {
            try {
                if (obj.getJSONObject(i) != null)
                {
                    itemNote.add(0, obj.getJSONObject(i).getString("title") + " : " +
                            obj.getJSONObject(i).getString("note") +
                            "\nAuteur : " + obj.getJSONObject(i).getString("noteur"));
                    itemAdapterNote.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setUpViewHistory(JSONArray obj, int limit) {
        historyList = (ListView)C.findViewById(R.id.historyList);
        itemHistory = new ArrayList<>();
        itemHistory.clear();

        itemAdapterHistory = new ArrayAdapter<>(C.getContext(), android.R.layout.simple_list_item_1, itemHistory);
        historyList.setAdapter(itemAdapterHistory);

        for (int i = 0; i < obj.length() && i < limit; i++) {
            try {
                if (obj.getJSONObject(i) != null) {
                    itemHistory.add(0, stripHtml(obj.getJSONObject(i).getString("title")) + "\n\n" +
                            stripHtml(obj.getJSONObject(i).getString("content"))
                            + "\n\nPublished Date : " + obj.getJSONObject(i).getString("date"));
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

    public void makeRequestUserInfo()
    {
        final TextView msg = (TextView) C.findViewById(R.id.Title);
        final ProgressDialog toto = ProgressDialog.show(C.getContext(), "Chargement...", "Merci de patienter.");

        RequestQueue queue = MySingleton.getInstance(C.getContext()).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        putInfosInScreen(response);
                        toto.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        msg.setText(error.toString());
                        toto.dismiss();
                    }
                });
        MySingleton.getInstance(C.getContext()).addToRequestQueue(jsObjRequest);
    }
}
