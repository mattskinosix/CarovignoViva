package c.www.carovignoviva.citta;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import c.www.carovignoviva.R;
import c.www.carovignoviva.events.CustomListViewEvents;
import c.www.carovignoviva.events.Event;
import c.www.carovignoviva.events.EventInfo;
import c.www.carovignoviva.events.HomeEventi;

public class HomeCitta extends Activity {
    private ProgressDialog nDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventi);
        nDialog = new ProgressDialog(this);
        nDialog.setMessage("Loading..");
        nDialog.setTitle("Sto consultando l'enciclopedia");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();
            //CREO LA LISTA CON I DATI CONTENUTI NEL VETTORE DI MONUMENTI
        getCittaFromFirebase();

    }
    private void getCittaFromFirebase(String... voids){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("city/");
        myRef.keepSynced(true);
        myRef.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                error();
            }
        });
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                nDialog.dismiss();
                JSONArray json_array = new JSONArray();
                for (DataSnapshot children : task.getResult().child(voids[1]).getChildren()) {
                    Log.i("test", children.getKey() );
                    JSONObject json_obj = new JSONObject();
                    for (DataSnapshot children2 : task.getResult().child(voids[1]).child(children.getKey()).getChildren()) {
                        try {

                            Log.i("test", children2.getKey() );
                            json_obj.put(children2.getKey(), children2.getValue());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    json_array.put(json_obj);
                }
                try {
                    Log.i("test", json_array.toString() );
                    ListView listView = findViewById(R.id.Listeventi);
                    ArrayList<Citta> cityes=null;
                    
                    cityes = new Citta().eventoFromJson(null);
                  

                    // creo e istruisco l'adattatore
                    final CustomListViewCitta adapter = new CustomListViewCitta(getBaseContext(), R.layout.list_item_citta,cityes);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void error() {
        setContentView(R.layout.error_network);
        TextView textView = findViewById(R.id.errorview);
        textView.setText("intetnet è spento");

    }
}
