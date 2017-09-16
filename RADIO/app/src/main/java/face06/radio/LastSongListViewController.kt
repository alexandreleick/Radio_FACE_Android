package face06.radio

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.util.*

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */
class LastSongListViewController(context: Context, sInfo: ArrayList<KSongInformations>) : Dialog(context, R.style.AppTheme) {
    //internal var sLogo = arrayOf(R.drawable.ic_help_outline_black_18dp, R.drawable.legals, R.drawable.io)

    var sInfo: ArrayList<KSongInformations>
    private var title: TextView? = null
    private var artiste: TextView? = null
    private var player: MediaPlayer? = null
    private var playButton: ImageView? = null
    internal var cover: ImageView? = null
    var buttonOk: Boolean = false
    private var isStarted = true
    var name: TextView?= null
    var layout_name: TextView?= null
    private var returnButton: ImageView? = null


    init {
        this.sInfo = sInfo
    }
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.last_song_layout)
        Log.i("DEBUG", "ici" + sInfo!!.size)
        val lv = findViewById<ListView>(R.id.list)
        lv.adapter = LastSongViewController(context, sInfo!!)
        returnButton = findViewById<ImageView>(R.id.button) as ImageView
        name = findViewById<TextView>(R.id.name) as TextView
        layout_name = findViewById<TextView>(R.id.layout_name) as TextView
        title = findViewById<TextView>(R.id.title) as TextView
        artiste = findViewById<TextView>(R.id.artiste) as TextView
        playButton = findViewById<ImageView>(R.id.play) as ImageView
        setFont(name!!, "Archive.otf", "RUN RADIO")
        setFont(layout_name!!, "Geomanist-Book.otf", "Dernières musiques")
        Log.i("DEBUG", "" + APIRadio.getShared().bool!! + "ETAPE 1")
        isStarted = APIRadio.getShared().bool!!
        buttonOk = isStarted
        player = APIRadio.getShared().player
        RepetAction()
        if (isStarted == false)
            playButton!!.setImageResource(R.drawable.play_button)
        else if (isStarted == true)
            playButton!!.setImageResource(R.drawable.pause)
        playButton!!.setOnClickListener(onButtonClick)
        returnButton!!.setOnClickListener({
            context.startActivity(Intent(context, KMainActivityl::class.java))
        })

    }
    private val onButtonClick = View.OnClickListener { v ->
        if (buttonOk == true) {
            Log.i("DEBUG", "" + v.id)
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
        }
    }

    internal inner class RepetAction {
        var t: Timer

        init {
            t = Timer()
            t.schedule(MonAction(), 0, (10 * 1000).toLong())
        }

        internal inner class MonAction : TimerTask() {
            var nbrRepetitions = 100
            var conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            override fun run() {
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (nbrRepetitions > 0) {
                        sInfo = KSongInformations().getSongInformations()
                        title!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                if (buttonOk == false) {
                                    playButton!!.setImageResource(R.drawable.play_button)
                                    buttonOk = true

                                }
                                setFont(title!!, "OpenSans-Semibold.ttf", sInfo!![0].name!!)

                                println("change title")
                                i++
                                if (i == 100)
                                    i = 0
                                title!!.postDelayed(this, 5000)
                            }
                        })
                        artiste!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                setFont(artiste!!, "OpenSans-Regular.ttf", sInfo!![0].artiste!!)

                                println("change artiste")
                                i++
                                if (i == 100)
                                    i = 0
                                artiste!!.postDelayed(this, 5000)
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

    fun setFont(textView: TextView, fontName: String?, setText: String) {
        if (fontName != null) {
            try {
                val typeface = Typeface.createFromAsset(context.assets, "fonts/" + fontName)
                textView.typeface = typeface
                textView.text = setText
            } catch (e: Exception) {
                Log.e("FONT", fontName + " not found", e)
            }

        }
    }
}