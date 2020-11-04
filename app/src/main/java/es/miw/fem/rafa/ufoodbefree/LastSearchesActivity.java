package es.miw.fem.rafa.ufoodbefree;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import es.miw.fem.rafa.ufoodbefree.models.LastSearch;

public class LastSearchesActivity extends AppCompatActivity {

    private final String SAVED_SEARCH = "search";

    FirebaseAuth fAuth;
    FirebaseDatabase fDatabase;
    DatabaseReference fDatabaseReference;
    ChildEventListener fChildEventListener;

    LastSearchAdapter adaptador;

    ListView lvSearches;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_searches);
        lvSearches = findViewById(R.id.lvLast_Searches);

        fAuth  = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        fDatabaseReference = fDatabase.getReference().child("lastSearches");

        List<LastSearch> lastSearches = new ArrayList<>();

        adaptador = new LastSearchAdapter(this, R.layout.search_item, lastSearches);
        lvSearches.setAdapter(adaptador);
        lvSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(fAuth.getCurrentUser() != null) {
                    String str = ((TextView) view.findViewById(R.id.tvSearchItem)).getText().toString();

                    Intent intent = new Intent(LastSearchesActivity.this, SearchActivity.class);
                    intent.putExtra(SAVED_SEARCH, str);
                    startActivity(intent);
                } else {
                    Toast.makeText(LastSearchesActivity.this, getString(R.string.sign_in_required), Toast.LENGTH_SHORT)
                            .show();
                }


            }
        });

        this.attachDatabaseReadListener();

        // Mostrar el icono back en la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Icono Actionbar
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_miw_launcher_round);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        detachDatabaseReadListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.sign_out);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                this.signOut();
                break;
        }
        return true;
    }

    private void attachDatabaseReadListener() {
        if(fChildEventListener == null) {
            // btb Listener will be called when changes were performed in DB
            fChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    LastSearch lastSearch = dataSnapshot.getValue(LastSearch.class);
                    if(lastSearch.getUsername().equals(fAuth.getCurrentUser().getEmail())) adaptador.add(lastSearch);;
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            };
            fDatabaseReference.addChildEventListener(fChildEventListener);
        }
    }

    private void detachDatabaseReadListener() {
        if(fChildEventListener != null) {
            fDatabaseReference.removeEventListener(fChildEventListener);
            fChildEventListener = null;
        }
    }

    private void signOut() {
        fAuth.signOut();
        String strLoggedOut = "Usuario desconectado";
        Toast.makeText(this, strLoggedOut, Toast.LENGTH_SHORT)
                .show();
        startActivity(new Intent(this, MainActivity.class));
    }
}
