package ovejero_nicolas.epiandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;


public class UserInfos extends FragmentActivity {
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_infos);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        Bundle bundleUser = new Bundle();

        bundleUser.putString("token", getIntent().getStringExtra("token"));

        mTabHost.addTab(mTabHost.newTabSpec("perso").setIndicator("Personnal"), UserFragment.class, bundleUser);
        mTabHost.addTab(mTabHost.newTabSpec("planning").setIndicator("Planning"), PlanningFragment.class, null);
//        mTabHost.addTab(mTabHost.newTabSpec("grades").setIndicator("Grades"), UserFragment.class, null);

//      ((TextView)findViewById(R.id.Title)).setText("Test");
    }
}