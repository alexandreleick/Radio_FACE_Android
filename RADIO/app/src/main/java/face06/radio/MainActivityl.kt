package face06.radio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.util.*
import android.graphics.Typeface
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.DialogInterface
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Toast


/**
 * Created by Alexandre Leick on 08/09/2017,
 * StaffBooker Company.
 */

class KMainActivityl : Activity() {

    var song: ArrayList<KSongInformations>? = null
    var title: TextView?= null
    var artiste: TextView?= null
    var name: TextView?= null
    var layout_name: TextView?= null
    var inLive: Button?= null
    var settings: ImageView?= null
    var lastSong: Button? = null
     var player: MediaPlayer? = null
     var playButton: ImageView? = null
    var buttonOk: Boolean = false
     var isStarted = true
    var main: PlayAudioExample?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.home_layout)
        name = findViewById<TextView>(R.id.name) as TextView
        layout_name = findViewById<TextView>(R.id.layout_name) as TextView
        inLive = findViewById<Button>(R.id.inLive) as Button
        lastSong = findViewById<Button>(R.id.lastSong) as Button
        title = findViewById<TextView>(R.id.title) as TextView
        artiste = findViewById<TextView>(R.id.artiste) as TextView
        settings = findViewById<Button>(R.id.settings) as ImageView
        playButton = findViewById<ImageView>(R.id.play) as ImageView

        setFont(name!!, "Archive.otf", "RUN RADIO")
        setFont(layout_name!!, "Geomanist-Book.otf", "Accueil")
        player = APIRadio.getShared().player

        ReaptAction()
            SystemClock.sleep(100)
            isStarted = APIRadio.getShared().bool!!
            buttonOk = isStarted
            if (isStarted == false)
                playButton!!.setImageResource(R.drawable.play_button)
            else if (isStarted == true)
                playButton!!.setImageResource(R.drawable.pause)
            playButton!!.setOnClickListener(onButtonClick)

        //setFont(artiste!!, "OpenSans-Regular.ttf", song!![0].artiste!!)
        //setFont(title!!, "OpenSans-Regular.ttf", song!![0].name!!)

        inLive!!.setOnClickListener({
            Log.i("DEBUG", "" + "1")
            intent = Intent(this, PlayAudioExample::class.java)
            startActivity(intent)
        })

        settings!!.setOnClickListener({
            Log.i("DEBUG", "" + "3")
            intent = Intent(this, Parametre::class.java)
            startActivity(intent)
        })

        lastSong!!.setOnClickListener({
            val paramDialog = LastSongListViewController(this, song!!)
            paramDialog.show()
            Log.i("DEBUG", "" + "2")
        })
        //ReaptAction()
        //title!!.text = song!![0].name
        //artiste!!.text = song!![0].artiste

    }

    private val onButtonClick = View.OnClickListener { v ->
        Log.i("DEBUG", "ici")
        Log.i("DEBUG", "" + v.id)
        var conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            when (v.id) {
                R.id.play -> {
                    if (player!!.isPlaying) {
                        // handler.removeCallbacks(updatePositionRunnable)
                        player!!.pause()
                        playButton!!.setImageResource(R.drawable.play_button)
                        APIRadio.getShared().bool = false
                        Log.i("DEBUG", "" + APIRadio.getShared().bool)
                    } else {

                        player!!.start()
                        playButton!!.setImageResource(R.drawable.pause)
                        APIRadio.getShared().bool = true
                        Log.i("DEBUG", "" + APIRadio.getShared().bool)

                    }
                }
            }
        } else {
            val simpleAlert = AlertDialog.Builder(this@KMainActivityl).create()
            simpleAlert.setTitle("Attention")
            simpleAlert.setMessage("Vous n'êtes pas connecté à Internet ! Merci de vous connecter à un réseau WIFI - MOBILE")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialogInterface, i ->
                KMainActivityl().finish()
            })
            simpleAlert.show()
        }
    }

    fun setFont(textView: TextView, fontName: String?, setText: String) {
        if (fontName != null) {
            try {
                val typeface = Typeface.createFromAsset(assets, "fonts/" + fontName)
                textView.typeface = typeface
                textView.text = setText
            } catch (e: Exception) {
                Log.e("FONT", fontName + " not found", e)
            }

        }
    }
    internal inner class ReaptAction {
        var t: Timer

        init {
            t = Timer()
            t.schedule(MonAction(), 0, (10 * 1000).toLong())
        }

        internal inner class MonAction : TimerTask() {
            var nbrRepetitions = 100
            var conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            override fun run() {
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (nbrRepetitions > 0) {
                        song = KSongInformations().getSongInformations()
                        title!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                //Log.i("Debug",  song!![0].name)
                                setFont(title!!, "OpenSans-Semibold.ttf", song!![0].name!!)

                                println("changeText")
                                i++
                                if (i == 100)
                                    i = 0
                                title!!.postDelayed(this, 5000)
                            }
                        })
                        artiste!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                setFont(artiste!!, "OpenSans-Regular.ttf", song!![0].artiste!!)

                                println("changeText")
                                i++
                                if (i == 100)
                                    i = 0
                                artiste!!.postDelayed(this, 5000)
                            }
                        })
                        playButton!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                if (APIRadio.getShared().bool == false)
                                    playButton!!.setImageResource(R.drawable.play_button)
                                else if (APIRadio.getShared().bool == true)
                                    playButton!!.setImageResource(R.drawable.pause)

                                println("changeText")
                                i++
                                if (i == 100)
                                    i = 0
                                playButton!!.postDelayed(this, 5000)
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
    }
}
