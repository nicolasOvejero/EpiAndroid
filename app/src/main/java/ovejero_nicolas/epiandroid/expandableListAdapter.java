package ovejero_nicolas.epiandroid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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

import cz.msebera.android.httpclient.Header;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<JSONObject>> _listDataChild;
    private UserClass user;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<JSONObject>> listChildData,
                                 UserClass user) {
        this.user = user;
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
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
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final JSONObject childText = (JSONObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_project, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView begin = (TextView) convertView.findViewById(R.id.begin);
        TextView end = (TextView) convertView.findViewById(R.id.end);
        final Switch status = (Switch) convertView.findViewById(R.id.status);
        TextView valid = (TextView) convertView.findViewById(R.id.valid);

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status.isChecked())
                    subscribe(childText, v);
                else
                    unsubscribe(childText, v);
            }
        });

        try {
            title.setText(childText.getString("title"));
            begin.setText("From : " + childText.getString("begin"));
            end.setText("To : " + childText.getString("end"));
            if (childText.has("status")) {
                if (childText.getString("status").equals("valid"))
                    status.setVisibility(View.GONE);
                else if (childText.getString("status").equals("notregistered")) {
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
            else if (childText.has("rights")) {
                String all = "";
                for (int i = 0; i < childText.getJSONArray("rights").length(); i++) {
                    if ((childText.getJSONArray("rights").getString(i)).equals("assistant") ||
                            (childText.getJSONArray("rights").getString(i)).equals("prof_inst"))
                        all += childText.getJSONArray("rights").getString(i) + "\n";
                }
                status.setVisibility(View.GONE);
                valid.setText(all);
                valid.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.header_row_project, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
