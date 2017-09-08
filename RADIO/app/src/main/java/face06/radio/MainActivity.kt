package face06.radio

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TabHost
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */

class KMainActivity : AppCompatActivity() {

    var radio: ImageView? = null
    var lastSong: ImageView? = null
    var emission: ImageView? = null
    var sociaux: ImageView? = null
    var song: ArrayList<KSongInformations>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_view)

        radio = findViewById(R.id.radio) as ImageView
        lastSong = findViewById(R.id.lastSong) as ImageView
        emission = findViewById(R.id.emission) as ImageView
        sociaux = findViewById(R.id.sociaux) as ImageView

        ReaptSong()
        radio!!.setOnClickListener( {
            Log.i("DEBUG", "RADIO")
            intent = Intent(this, PlayAudioExample::class.java)
            startActivity(intent)
        })

        lastSong!!.setOnClickListener( {
            Log.i("SONG", "" + song!!.size)
            val paramDialog = LastSongListViewController(this, song!!)
            paramDialog.show()
        })

        emission!!.setOnClickListener( {

        })

        sociaux!!.setOnClickListener( {

        })

    }

    internal inner class ReaptSong {
        var t: Timer

        init {
            t = Timer()
            t.schedule(MonAction(), 0, (10 * 1000).toLong())
        }

        internal inner class MonAction : TimerTask() {
            var nbrRepetitions = 100

            override fun run() {
                if (nbrRepetitions > 0) {
                    song = KSongInformations().getSongInformations()

                    println("Ca bosse dur!")
                    nbrRepetitions--
                } else {
                    println("Terminé!")
                    t.cancel()
                }
            }
        }
    }
}
