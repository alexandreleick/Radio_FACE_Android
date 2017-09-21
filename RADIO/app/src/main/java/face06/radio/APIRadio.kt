package face06.radio

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.nio.channels.CompletionHandler
import java.util.*

/**
 * Created by Alexandre Leick on 08/09/2017,
 * StaffBooker Company.
 */

class APIRadio {
    var bool: Boolean? = null
    var player: MediaPlayer? = null
    var init: Int = 0

    /**
     * Singleton permettant d'initialiser le lien streaming et le bouton de lecture.
     */

    companion object {
        var instance: APIRadio? = null
        fun getShared(): APIRadio {
            if (instance == null) {
                instance = APIRadio()
            }
            return instance!!
        }
    }

    /**
     * @param applicationContext
     * @return : lien player permettant de recuperer le lien streaming.
     */

    fun manageRadio(applicationContext: Context, completionHandler: (player: MediaPlayer) -> Unit ){
        var streaming = "http://radioking.com/play/run-radio"
        player = MediaPlayer()
        player!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        PlayerTask().execute(streaming)
        completionHandler(player!!)
    }

    /**
     * Set la radio dès le début du lancement (SplashScreen)
     */

    internal inner class PlayerTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg strings: String): Boolean? {
            try {
                Log.i("DEBUG", strings[0])
                player!!.setDataSource(strings[0])
                player!!.prepare()
                APIRadio.getShared().player = player
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return true
        }
    }
}