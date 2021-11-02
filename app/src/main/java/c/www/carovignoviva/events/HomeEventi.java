package c.www.carovignoviva.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
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


public class HomeEventi extends Activity {
    ArrayList<Event> events=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventi);

            //CREO LA LISTA CON I DATI CONTENUTI NEL VETTORE DI MONUMENTI
        ListView listView = findViewById(R.id.Listeventi);

        this.getEventFromFirebase("carovigno", "eventi");

    }


    private void getEventFromFirebase(String... voids){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("city/"+voids[0]+"/");
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

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
                    events = new Event().eventoFromJson(json_array);
                    ListView listView = findViewById(R.id.Listeventi);
                    final CustomListViewEvents adapter = new CustomListViewEvents(getBaseContext(), R.layout.list_item_evento,events);
                    listView.setAdapter(adapter);
                    final ArrayList<Event> finalEvents = events;
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adattatore, final View componente, int pos, long id) {
                            Intent intent = new Intent(HomeEventi.this ,  EventInfo.class);
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



}
