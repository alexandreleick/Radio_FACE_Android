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
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */

class KMainActivity : AppCompatActivity() {
    internal var prefs: SharedPreferences? = null
    internal var play: Button? = null
    internal var lastSong: Button? = null
    internal var pause: Button? = null
    internal var title: TextView? = null
    internal var artist: TextView? = null
    internal var cover: ImageView? = null
    internal var started: Boolean = false
    internal var song: ArrayList<KSongInformations>? = null

    internal var mediaPlayer: MediaPlayer? = null
    internal var streaming = "http://radioking.com/play/run-radio"
    internal var prepared = false
    internal val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        Log.i("DEBUG", "BOOL = " + started)
        prefs = getSharedPreferences("face06.radio.RADIO", Context.MODE_PRIVATE)
        play = findViewById(R.id.play) as Button
        play!!.isEnabled = false

        pause = findViewById(R.id.pause) as Button
        lastSong = findViewById(R.id.LastSong) as Button
        title = findViewById(R.id.song) as TextView
        artist = findViewById(R.id.artiste) as TextView
        cover = findViewById(R.id.pochette) as ImageView

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        PlayerTask().execute(streaming)

        play!!.setOnClickListener {
            started = true
            whoDialog = true
            mediaPlayer!!.start()
            title!!.text = song!![0].name
            artist!!.text = song!![0].artiste
            song!![0].pochette.let {
                Picasso.with(applicationContext).load(song!![0].pochette).into(cover)
            }
        }
        pause!!.setOnClickListener {
            started = false
            mediaPlayer!!.pause()
        }

        lastSong!!.setOnClickListener {
            Log.i("DEBUG", "" + song!!.size)
            val paramDialog = LastSongListViewController(this, song!!)
            paramDialog.show()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("DEBUG", "SAVE")

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("DEBUG", "RESTORE")

    }

    internal inner class PlayerTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg strings: String): Boolean? {
            try {
                mediaPlayer!!.setDataSource(strings[0])
                mediaPlayer!!.prepare()
                prepared = true
                RepetAction()


            } catch (e: IOException) {
                e.printStackTrace()
            }

            return prepared
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            play!!.isEnabled = true
        }
    }


    override fun onResume() {
        Log.i("DEBUG", "RESUME" + whoDialog)
        if (whoDialog == false)
            myDialog()

        super.onResume()
        //if (started) {
        //  mediaPlayer.start();
        //}
    }

    internal fun myDialog() {
        val alertDialogBuilder = AlertDialog.Builder(
                context)

        // set title
        alertDialogBuilder.setTitle("Your Title")

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    SystemClock.sleep(2500)
                    started = true
                    mediaPlayer!!.start()
                    title!!.text = song!![0].name
                    artist!!.text = song!![0].artiste
                   song!![0].pochette.let {
                        Picasso.with(applicationContext).load(song!![0].pochette).into(cover)
                    }
                    whoDialog = true
                    dialog.cancel()
                    //MainActivity.this.finish();
                }
                .setNegativeButton("No") { dialog, id ->
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    finishActivity(0)
                }

        // create alert dialog
        val alertDialog = alertDialogBuilder.create()

        // show it
        alertDialog.show()
    }

    internal inner class RepetAction {
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
                    title!!.post(object : Runnable {
                        internal var i = 0
                        override fun run() {
                            title!!.text = song!![0].name
                            println("changeText")
                            i++
                            if (i == 100)
                                i = 0
                            title!!.postDelayed(this, 5000)
                        }
                    })
                    artist!!.post(object : Runnable {
                        internal var i = 0
                        override fun run() {
                            artist!!.text = song!![0].artiste
                            println("changeArtiste")
                            i++
                            if (i == 100)
                                i = 0
                            artist!!.postDelayed(this, 5000)
                        }
                    })
                    cover!!.post(object : Runnable {
                        internal var i = 0
                        override fun run() {
                            song!![0].pochette.let {
                                Picasso.with(applicationContext).load(song!![0].pochette).into(cover)
                                println("Pochette OK")
                            }
                            i++
                            if (i == 100)
                                i = 0
                            cover!!.postDelayed(this, 5000)
                        }
                    })
                    println("Ca bosse dur!")
                    nbrRepetitions--
                } else {
                    println("Terminé!")
                    t.cancel()
                }
            }
        }
    }

    companion object {
        internal var whoDialog: Boolean = false
    }
}
