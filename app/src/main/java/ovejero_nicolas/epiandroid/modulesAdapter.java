package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class modulesAdapter extends ArrayAdapter<JSONObject> {
    Context context;
    List<JSONObject> obj;
    private int resourceId;
    private static LayoutInflater inflater = null;
    private UserClass user;

    public modulesAdapter(Context context, int resourceId, List<JSONObject> objects, UserClass user) {
        super(context, resourceId, objects);
        this.context = context;
        this.obj = objects;
        this.user = user;
        this.resourceId = resourceId;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void subscribe(final JSONObject obj, View v) {
        RequestQueue queue = MySingleton.getInstance(v.getContext()).getRequestQueue();
        final ProgressDialog pd = ProgressDialog.show(v.getContext(), "Chargement...", "Merci de patienter.");

        final Switch status = (Switch) v.findViewById(R.id.status);

        StringRequest sr = new StringRequest(Request.Method.POST,
                "http://epitech-api.herokuapp.com/module", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (status.isChecked())
                {
                    status.setText("You are registered");
                    status.setChecked(true);
                }
                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ko " + error);
                pd.dismiss();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                try {
                    params.put("token", user.getToken());
                    params.put("scolaryear", obj.getString("scolaryear"));
                    params.put("codemodule", obj.getString("code"));
                    params.put("codeinstance", obj.getString("codeinstance"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return params;
            }
        };
        MySingleton.getInstance(v.getContext()).addToRequestQueue(sr);
    }

    private void unsubscribe(final JSONObject obj, View v) {
        final ProgressDialog pd = ProgressDialog.show(v.getContext(), "Chargement...", "Merci de patienter.");

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams toto = new RequestParams();
        try {
            toto.put("token", user.getToken());
            toto.put("scolaryear", obj.getString("scolaryear"));
            toto.put("codemodule", obj.getString("code"));
            toto.put("codeinstance", obj.getString("codeinstance"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Switch status = (Switch) v.findViewById(R.id.status);

        client.delete("http://epitech-api.herokuapp.com/module", toto, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode == 200)
                {
                    if (!status.isChecked())
                    {
                        status.setText("You are not registered");
                        status.setChecked(false);
                    }
                }
                pd.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pd.dismiss();
            }
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row_modules, null);
        }

        final JSONObject item = getItem(position);

        TextView title = (TextView) v.findViewById(R.id.title);
        TextView begin = (TextView) v.findViewById(R.id.begin);
        TextView end = (TextView) v.findViewById(R.id.end);
        final Switch status = (Switch) v.findViewById(R.id.status);
        TextView valid = (TextView) v.findViewById(R.id.valid);

        status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (status.isChecked())
                        subscribe(item, v);
                    else
                        unsubscribe(item, v);
                }
            });

        try {
            title.setText(item.getString("title"));
            begin.setText("From : " + item.getString("begin"));
            end.setText("To : " + item.getString("end"));
            if (item.has("status")) {
                if (item.getString("status").equals("valid"))
                    status.setVisibility(View.GONE);
                else if (item.getString("status").equals("notregistered")) {
                    status.setText("You are not registered");
                    status.setChecked(false);
                    valid.setVisibility(View.GONE);
                    status.setVisibility(View.VISIBLE);
                } else {
                    status.setText("You are registered");
                    status.setChecked(true);
                    valid.setVisibility(View.GONE);
                    status.setVisibility(View.VISIBLE);
                }
            }
            else if (item.has("rights")) {
                String all = "";
                for (int i = 0; i < item.getJSONArray("rights").length(); i++) {
                    if ((item.getJSONArray("rights").getString(i)).equals("assistant") ||
                            (item.getJSONArray("rights").getString(i)).equals("prof_inst"))
                        all += item.getJSONArray("rights").getString(i) + "\n";
                }
                status.setVisibility(View.GONE);
                valid.setText(all);
                valid.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
