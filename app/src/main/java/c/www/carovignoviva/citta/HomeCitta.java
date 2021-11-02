package c.www.carovignoviva.citta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import c.www.carovignoviva.R;

public class HomeCitta extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventi);

            //CREO LA LISTA CON I DATI CONTENUTI NEL VETTORE DI MONUMENTI
            ListView listView = findViewById(R.id.Listeventi);
        ArrayList<Citta> cityes=null;
        try {
            cityes = new Citta().eventoFromJson(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // creo e istruisco l'adattatore
            final CustomListViewCitta adapter = new CustomListViewCitta(this, R.layout.list_item_citta,cityes);
            listView.setAdapter(adapter);
        final ArrayList<Citta> finalEvents = cityes;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                    Intent intent = new Intent(HomeCitta.this ,  CittaInfo.class);
                    intent.putExtra("Citta", finalEvents.get(pos));
                    startActivity(intent);
                }

        });

    }

}
