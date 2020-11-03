package es.miw.fem.rafa.ufoodbefree;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LastSearchesActivity extends AppCompatActivity {

    FirebaseAuth fAuth;

    ListView lvSearches;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_searches);
        lvSearches = findViewById(R.id.lvLast_Searches);

        // TODO Implementar BBDD Firebase con búsquedas
        String[] mock = {"Prueba1", "Prueba2", "Prueba3"};
        ArrayAdapter<CharSequence> adaptador = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mock
        );
        lvSearches.setAdapter(adaptador);
        lvSearches.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String str = ((TextView) view).getText().toString();
                Toast.makeText(LastSearchesActivity.this, str, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        fAuth  = FirebaseAuth.getInstance();

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
