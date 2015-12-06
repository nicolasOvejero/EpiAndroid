package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapterProject extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<JSONObject>> _listDataChild;
    private UserClass user;

    public ExpandableListAdapterProject(Context context, List<String> listDataHeader,
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

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final JSONObject childText = (JSONObject) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.project_list, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView start = (TextView) convertView.findViewById(R.id.begin);
        TextView end = (TextView) convertView.findViewById(R.id.end);
        Button subscribe = (Button) convertView.findViewById(R.id.subscribe);

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RequestQueue queue = MySingleton.getInstance(v.getContext()).getRequestQueue();
                final ProgressDialog pd = ProgressDialog.show(v.getContext(), "Chargement...", "Merci de patienter.");

                StringRequest sr = new StringRequest(Request.Method.POST,
                        "http://epitech-api.herokuapp.com/project", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                            params.put("scolaryear", childText.getString("scolaryear"));
                            params.put("codemodule", childText.getString("codemodule"));
                            params.put("codeinstance", childText.getString("codeinstance"));
                            params.put("codeacti", childText.getString("codeacti"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return params;
                    }
                };
                MySingleton.getInstance(v.getContext()).addToRequestQueue(sr);
            }
        });

        DateFormat sm = new SimpleDateFormat("dd-mm-yyyy");

        try {
            if (childText.getString("registered").equals("0"))
                subscribe.setVisibility(View.VISIBLE);
            else
                subscribe.setVisibility(View.GONE);
            JSONArray nvx = childText.getJSONArray("rights");
            if (!nvx.toString().contains("prof_inst") && !nvx.toString().contains("assistant")) {
                title.setText(childText.getString("acti_title"));
                start.setText("From : " + (childText.getString("begin_acti")).substring(0, 10));
                end.setText(" To : " + (childText.getString("end_acti")).substring(0, 10));
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

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
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
