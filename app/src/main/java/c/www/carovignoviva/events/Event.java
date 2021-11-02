package c.www.carovignoviva.events;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable {
    private String codice;
    private String nome;
    private String luogo;
    private String ora_inizio;
    private String ora_fine;
    private String image;
    private String description;

    ArrayList<Event> eventoFromJson(JSONArray jsonarray) throws JSONException {
        ArrayList<Event> events=new ArrayList<> ();
        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);
            Event evento=new Event();
            evento.nome = jsonobject.getString("nome");
            evento.luogo= jsonobject.getString("luogo");
            evento.ora_inizio= jsonobject.getString("ora_inizio");
            evento.ora_fine= jsonobject.getString("ora_fine");
            evento.image=jsonobject.getString("immagine");
            evento.description=jsonobject.getString("descrizione");

            events.add(evento);
        }
        return events;
    }

    public String getCodice() {
        return codice;
    }

    String getNome() {
        return nome;
    }

    String getLuogo() {
        return luogo;
    }

    String getOra_inizio() {
        return ora_inizio;
    }

    String getOra_fine() {
        return ora_fine;
    }

    String getImage() {
        return image;
    }

    String getDescription() {
        return description;
    }
}
