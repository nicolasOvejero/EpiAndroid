package ovejero_nicolas.epiandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class principal extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private android.support.v7.widget.Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private UserClass user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_principal);

        user = (UserClass)getIntent().getSerializableExtra("user");
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);

        makeRequestImage();
        makeRequestUserInfo();
    }

    private void makeRequestImage()
    {
        final Context C = this;

        RequestQueue queue = MySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,
                "http://epitech-api.herokuapp.com/photo?token=" +
                        user.getToken() +
                        "&login=" + user.getLogin(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Picasso.with(C).load(response.getString("url"))
                                    .into((ImageView) (nvDrawer.inflateHeaderView(R.layout.nav_header)).findViewById(R.id.pictureWiew));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;

                        ((TextView)findViewById(R.id.Title)).setText(error.toString());
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void First() {
        Fragment fragment = null;
        try {
            fragment = (UserFragment.class).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if (fragment != null) {
            fragment.setArguments(extras);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        setTitle(R.string.title_info_user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            }

            return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                }
        );
    }

    public void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;

        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = UserFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = PlanningFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = TrombiFragment.class;
                break;
                case R.id.nav_four_fragment:
                fragmentClass = ProjectsFragment.class;
                break;
            case R.id.nav_my_past_modules:
                fragmentClass = ModulesFragment.class;
                break;
            case R.id.nav_my_modules:
                fragmentClass = AllModuleFragment.class;
                break;
            default:
                fragmentClass = UserFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = new Bundle();
        if (fragment != null) {
            extras.putSerializable("user", user);
            fragment.setArguments(extras);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    public void makeRequestUserInfo()
    {
        final TextView msg = (TextView) findViewById(R.id.Title);
        final ProgressDialog toto = ProgressDialog.show(this, "Chargement...", "Merci de patienter.");

        String path = "infos?token=" + user.getToken();
        RequestQueue queue = MySingleton.getInstance(this).getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, "http://epitech-api.herokuapp.com/" + path, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject obj) {
                        try {
                            user.setTitle(obj.getJSONObject("infos").getString("title"));
                            user.setPromo(obj.getJSONObject("infos").getString("promo"));
                            user.setLogTime((obj.getJSONArray("current").getJSONObject(0).getString("active_log")).substring(0, 5));
                            user.setSemester(Integer.parseInt(obj.getJSONObject("infos").getString("semester")));
                            user.setCity(obj.getJSONObject("infos").getString("location"));
                            user.setProject(obj.getJSONObject("board").getJSONArray("projets"));
                            user.setNotes(obj.getJSONObject("board").getJSONArray("notes"));
                            user.setModules(obj.getJSONObject("board").getJSONArray("modules"));
                            user.setActivity(obj.getJSONObject("board").getJSONArray("activites"));
                            user.setHistory(obj.getJSONArray("history"));
                            user.setInfos(obj.getJSONObject("infos"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        First();
                        toto.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        System.out.println("Error");
                        msg.setText(error.toString());
                        toto.dismiss();
                    }
                });
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
    }
}
