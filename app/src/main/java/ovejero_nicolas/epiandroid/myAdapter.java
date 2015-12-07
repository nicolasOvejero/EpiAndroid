package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

public class myAdapter extends BaseAdapter {
    Context context;
    UserClass user;
    int nb;
    private static LayoutInflater inflater = null;

    public myAdapter(Context context, UserClass user, int nb) {
        this.context = context;
        this.user = user;
        this.nb = nb;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nb;
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
            vi = inflater.inflate(R.layout.row, null);

        TextView text = (TextView) vi.findViewById(R.id.text);
        TextView from = (TextView) vi.findViewById(R.id.from);
        ProgressBar pgb = (ProgressBar) vi.findViewById(R.id.progressBar);
        TextView to = (TextView) vi.findViewById(R.id.to);
        pgb.getProgressDrawable().setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
        try {
            pgb.setProgress((int)Float.parseFloat(user.getProject().getJSONObject(position).getString("timeline_barre")));
            text.setText(user.getProject().getJSONObject(position).getString("title"));
            from.setText((user.getProject().getJSONObject(position).getString("timeline_start")).substring(0, 10));
            to.setText((user.getProject().getJSONObject(position).getString("timeline_end")).substring(0, 10));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return vi;
    }
}

