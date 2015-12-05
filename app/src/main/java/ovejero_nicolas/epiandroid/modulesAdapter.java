package ovejero_nicolas.epiandroid;

import android.content.Context;
import android.text.style.LineHeightSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class modulesAdapter extends BaseAdapter {
    Context context;
    JSONArray modules;
    int nb;
    private static LayoutInflater inflater = null;
    private boolean b0, b1, b2, b3, b4, b5, b6;

    public modulesAdapter(Context context, JSONArray modules) {
        this.context = context;
        this.modules = modules;
        b0 = false;
        b1 = false;
        b2 = false;
        b3 = false;
        b4 = false;
        b5 = false;
        b6 = false;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nb;
    }

    public void setCount(int nb) {
        this.nb = nb;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.row_modules  , null);
        TextView code = (TextView) vi.findViewById(R.id.code);
        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView begin = (TextView) vi.findViewById(R.id.begin);
        TextView end = (TextView) vi.findViewById(R.id.end);
        Switch status = (Switch) vi.findViewById(R.id.status);

        try {
            if (modules.getJSONObject(position).getInt("semester") == 0 && b0) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 1 && b1) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 2 && b2) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 3 && b3) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 4 && b4) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 5 && b5) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }
            else if (modules.getJSONObject(position).getInt("semester") == 6 && b6) {
                code.setText(modules.getJSONObject(position).getString("code"));
                title.setText(modules.getJSONObject(position).getString("title"));
                begin.setText(modules.getJSONObject(position).getString("begin"));
                end.setText(modules.getJSONObject(position).getString("end"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vi;
    }

    public void setB0(boolean b0) {
        this.b0 = b0;
    }

    public void setB1(boolean b1) {
        this.b1 = b1;
    }

    public void setB2(boolean b2) {
        this.b2 = b2;
    }

    public void setB3(boolean b3) {
        this.b3 = b3;
    }

    public void setB4(boolean b4) {
        this.b4 = b4;
    }

    public void setB5(boolean b5) {
        this.b5 = b5;
    }

    public void setB6(boolean b6) {
        this.b6 = b6;
    }
}
