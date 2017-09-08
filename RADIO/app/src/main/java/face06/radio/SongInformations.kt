package face06.radio

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */

class KSongInformations {

    var name: String? = null
    var artiste: String? = null
    var pochette: String? = null

    fun getSongInformations(): ArrayList<KSongInformations> {

        val song = ArrayList<KSongInformations>()

        try {
            val myurl = "https://www.radioking.com/widgets/api/v1/radio/12856/track/history"

            val url = URL(myurl)
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream = connection.inputStream
            /*
             * InputStreamOperations est une classe complémentaire:
             * Elle contient une méthode InputStreamToString.
             */
            val result = InputStreamOperations.InputStreamToString(inputStream)

            // On récupère le JSON complet
            val jsonArray = JSONArray(result)
            for (i in 0..jsonArray.length() - 1) {
                val jsonobject = jsonArray.getJSONObject(i)
                val songI = KSongInformations()
                songI.name = (jsonobject.getString("title"))
                songI.artiste = (jsonobject.getString("artist"))
                songI.pochette = (jsonobject.getString("cover"))
                song.add(songI)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        // On retourne la liste des personnes
        return song
    }
}
