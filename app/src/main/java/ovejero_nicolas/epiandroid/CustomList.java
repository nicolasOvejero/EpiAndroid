package ovejero_nicolas.epiandroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

public class CustomList extends BaseAdapter  {
    Context context;
    private final String[] text;
    private final String[] url;
    private static LayoutInflater inflater = null;

    public CustomList(Context context,
                      String[] text, String[] url) {
        this.context = context;
        this.text = text;
        this.url = url;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return url.length;
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
            vi = inflater.inflate(R.layout.list_single, null);

        TextView txtTitle = (TextView) vi.findViewById(R.id.txt);

        txtTitle.setText(text[position]);

        Picasso.with(context).load(url[position]).into((ImageView) vi.findViewById(R.id.img));
        return vi;
    }
}
