package face06.radio;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */

public class SongInformations {
    String name;
    String artiste;
    String pochette;


    public String getPochette() {
        return pochette;
    }

    public String getArtiste() {
        return artiste;
    }

    public String getName() {
        return name;
    }

    public void setArtiste(String artiste) {
        this.artiste = artiste;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPochette(String pochette) {
        this.pochette = pochette;
    }

    public static ArrayList<SongInformations> getSongInformations() {

        ArrayList<SongInformations> song = new ArrayList<SongInformations>();

        try {
            String myurl = "https://www.radioking.com/widgets/api/v1/radio/12856/track/history";

            URL url = new URL(myurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            /*
             * InputStreamOperations est une classe complémentaire:
             * Elle contient une méthode InputStreamToString.
             */
            String result = InputStreamOperations.InputStreamToString(inputStream);

            // On récupère le JSON complet
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                SongInformations songI = new SongInformations();
                songI.setName(jsonobject.getString("title"));
                songI.setArtiste(jsonobject.getString("artist"));
                songI.setPochette(jsonobject.getString("cover"));
                song.add(songI);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On retourne la liste des personnes
        return song;
    }
}
