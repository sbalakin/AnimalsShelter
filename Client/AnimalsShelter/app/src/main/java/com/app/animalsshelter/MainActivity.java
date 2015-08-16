package com.app.animalsshelter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.app.animalsshelter.adapters.CustomAdapterDrawer;
import com.app.animalsshelter.content.get_list_animals.GetListAnimalsFragment;
import com.app.animalsshelter.content.get_list_lost_animals.GetListLostAnimalsFragment;
import com.app.animalsshelter.content.help_about.AboutUsFragment;
import com.app.animalsshelter.content.help_about.HelpUsFragment;
import com.app.animalsshelter.content.send_animal.SendAnimalFragment;
import com.app.animalsshelter.content.send_lost_animal.GoogleMapTrackLostAnimalFragment;
import com.app.animalsshelter.content.send_lost_animal.SendLostAnimalFragment;


public class MainActivity extends ActionBarActivity {

    protected FrameLayout frameLayout;
    private DrawerLayout drawerLayout;
    protected ListView listItemDrawer;
    private CustomAdapterDrawer adapterDrawer;
    private ActionBarDrawerToggle drawerListener;
    protected static int position = 0;
    private static boolean isLaunch = true;
    private static int lastClicked = 0;

    public static int getPosition() {
        return position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIDsHere();

        setEventsHere();
    }


    private void getIDsHere() {
        frameLayout = (FrameLayout) findViewById(R.id.content_frame);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        listItemDrawer = (ListView) findViewById(R.id.drawerList);
        adapterDrawer = new CustomAdapterDrawer(MainActivity.this);
        listItemDrawer.setAdapter(adapterDrawer);
        //adapterDrawer.setSelectedPosition(position);
    }

    private void setEventsHere() {
        listItemDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //selectActivityInDrawer(position);
                //view.setClickable(false);
                //view.setEnabled(false);
                //adapterDrawer.setSelectedPosition(position);
                //openActivity(position);
                /*if (lastClicked != position) {
                    //openActivity(position);
                }
                lastClicked = position;*/
                /*listItemDrawer.setItemChecked(position, true);
                listItemDrawer.setSelection(position);*/
                adapterDrawer.setSelectedPosition(position);
                listItemDrawer.setItemChecked(position, true);
                String[] title = getResources().getStringArray(R.array.drawer_items);
                setTitle(title[position]);
                switchFragments(position);

                drawerLayout.closeDrawer(Gravity.LEFT);
            }
        });
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                //getSupportActionBar().setTitle("Closed!");
                invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //getSupportActionBar().setTitle("Open!");
                invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };
        drawerListener.setDrawerIndicatorEnabled(true);

        drawerLayout.setDrawerListener(drawerListener);

        if (isLaunch) {
            /**
             *Setting this flag false so that next time it will not open our first activity.
             *We have to use this flag because we are using this BaseActivity as parent activity to our other activity.
             *In this case this base activity will always be call when any child activity will launch.
             */
            isLaunch = false;
            //openActivity(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerListener.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        // Activate the navigation drawer toggle
        if (drawerListener.onOptionsItemSelected(item)) {
            return true;
        }

        return false;
    }

    BaseFragment fragment = null;

    private void switchFragments(int position) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        switch (position) {
            case 0:
                fragment = new GetListAnimalsFragment();
                break;
            case 1:
                fragment = new GetListAnimalsFragment();
                break;
            case 2:
                fragment = new SendAnimalFragment();
                break;
            case 3:
                fragment = new GetListLostAnimalsFragment();
                break;
            case 4:
                fragment = new SendLostAnimalFragment();
                break;
            case 5:
                fragment = new HelpUsFragment();
                break;
            case 6:
                fragment = new AboutUsFragment();
                break;
        }
        transaction.replace(R.id.content_frame, fragment);
       /* transaction.addToBackStack(null);*/
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            //this.finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


}
