package ovejero_nicolas.epiandroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class UserInfos extends AppCompatActivity {
    private JSONObject infoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infos);

        try {
            infoUser  = new JSONObject(getIntent().getStringExtra("info_user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        getPofilPicture();

        TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        final TabWidget tabWidget = tabHost.getTabWidget();
        final FrameLayout tabContent = tabHost.getTabContentView();

        TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
        for (int index = 0; index < tabWidget.getTabCount(); index++) {
            originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
        }
        tabWidget.removeAllViews();

        for (int index = 0; index < tabContent.getChildCount(); index++) {
            tabContent.getChildAt(index).setVisibility(View.GONE);
        }

        for (int index = 0; index < originalTextViews.length; index++) {
            final TextView tabWidgetTextView = originalTextViews[index];
            final View tabContentView = tabContent.getChildAt(index);
            TabHost.TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return tabContentView;
                }
            });
            if (tabWidgetTextView.getBackground() == null) {
                tabSpec.setIndicator(tabWidgetTextView.getText());
            } else {
                tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());
            }
            tabHost.addTab(tabSpec);
        }

        TextView login = (TextView) findViewById(R.id.login);
        TextView title = (TextView) findViewById(R.id.title);
        TextView mail = (TextView) findViewById(R.id.mail);
        TextView semestre = (TextView) findViewById(R.id.semestre);
        TextView year = (TextView) findViewById(R.id.year);
        NetworkImageView imgAvatar = (NetworkImageView) findViewById(R.id.imgAvatar);

        try {
            login.setText(infoUser.getJSONObject("infos").getString("login"));
            title.setText(infoUser.getJSONObject("infos").getString("title"));
            mail.setText(infoUser.getJSONObject("infos").getString("internal_email"));
            semestre.setText(infoUser.getJSONObject("infos").getString("semester"));
            year.setText(infoUser.getJSONObject("infos").getString("promo"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
