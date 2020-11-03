package es.miw.fem.rafa.ufoodbefree;

import android.content.Intent;
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
                // TODO Iniciar actividad search realizando búsqueda seleccionada
                String str = ((TextView) view).getText().toString();
                Toast.makeText(LastSearchesActivity.this, str, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        // btb Listener will be called when changes were performed in DB
        fChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                // Deserialize data from DB into our FriendlyMessage object
                LastSearch lastSearch = dataSnapshot.getValue(LastSearch.class);
                adaptador.add(lastSearch);
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

        // Mostrar el icono back en la ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Icono Actionbar
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_miw_launcher_round);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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

    private void signOut() {
        fAuth.signOut();
        String strLoggedOut = "Usuario desconectado";
        Toast.makeText(this, strLoggedOut, Toast.LENGTH_SHORT)
                .show();
        startActivity(new Intent(this, MainActivity.class));
    }
}
