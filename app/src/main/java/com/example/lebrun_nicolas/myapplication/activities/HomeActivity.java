package com.example.lebrun_nicolas.myapplication.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.lebrun_nicolas.myapplication.R;
import com.example.lebrun_nicolas.myapplication.fragments.notes.NoteDetailFragment;
import com.example.lebrun_nicolas.myapplication.fragments.notes.NoteItem;
import com.example.lebrun_nicolas.myapplication.fragments.notes.NotesFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotesFragment.OnItemClickListener, NoteDetailFragment.OnNoteDetailListener {

    private FragmentManager manager;  //getSupportManager
    private Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.createButton = (Button) this.findViewById(R.id.create_button);
        this.createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment detailFrag = NoteDetailFragment.newInstance(new NoteItem(0, "", "", null));
                FragmentManager manager = getFragmentManager(); //getSupportManager
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_container,detailFrag).addToBackStack("AddNote");
                transaction.commit();
            }
        });

        this.manager = getFragmentManager();

        this.goToNotesList();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.notes) {
            goToNotesList();
        } else if (id == R.id.libelle) {

        } else if (id == R.id.deconnexion) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void goToNotesList() {

        NotesFragment.OnItemClickListener listener = new NotesFragment.OnItemClickListener() {
            @Override
            public void onNoteItemClick(NoteItem item) {
                Fragment detailFrag = NoteDetailFragment.newInstance(item);
                FragmentManager manager = getFragmentManager(); //getSupportManager
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_container,detailFrag).addToBackStack("NoteDetail");
                transaction.commit();
            }
        };

        FragmentTransaction transaction = manager.beginTransaction();
        Fragment frag = NotesFragment.newInstance(1, listener);

        transaction.replace(R.id.frag_container,frag);
        transaction.commit();
    }


    @Override
    public void onNoteItemClick(NoteItem item) {
        Fragment detailFrag = NoteDetailFragment.newInstance(item);
        FragmentManager manager = getFragmentManager(); //getSupportManager
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frag_container,detailFrag).addToBackStack("NoteDetail");
        transaction.commit();
    }

    @Override
    public void onNoteDetailInteraction(Uri uri) {

    }
}
