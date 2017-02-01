package com.saber.app.distributionplanning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class AddMissionActivity extends AppCompatActivity {

    private static final String FRAGMENT_TAG = "AddMissionFragment";
    private AddMissionFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_mission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("添加配单");
        init();
    }

    private void init() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = (AddMissionFragment) fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        if (mFragment == null) {
            mFragment = AddMissionFragment.newInstance();
        }

        Intent intent = getIntent();
        if (intent != null) {
            int position = intent.getIntExtra(AddMissionFragment.PARAM_POSITION, -1);
            Bundle bundle = new Bundle();
            bundle.putInt(AddMissionFragment.PARAM_POSITION, position);
            mFragment.setArguments(bundle);
        }

        fragmentManager.beginTransaction()
                .add(R.id.fl_container, mFragment, FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_mission, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            mFragment.onDone(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
