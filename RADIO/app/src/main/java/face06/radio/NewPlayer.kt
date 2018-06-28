package face06.radio

import android.app.Activity
import android.app.ListActivity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Typeface
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.*
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.makeramen.roundedimageview.RoundedImageView
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import org.w3c.dom.Text
import java.io.IOException
import java.lang.Double
import java.math.BigDecimal
import java.util.*

/**
 * Created by Alexandre Leick on 20/08/2017,
 * StaffBooker Company.
 */

class PlayAudioExample : Activity() {

    private var title: TextView? = null
    private var artiste: TextView? = null
    private var player: MediaPlayer? = null
    private var playButton: ImageView? = null
    private var returnButton: ImageView? = null
    internal var song: ArrayList<KSongInformations>? = null
    internal var cover: ImageView? = null
    var buttonOk: Boolean = false
    private var isStarted = true
    var name: TextView?= null
    var layout_name: TextView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_layout)
        name = findViewById<TextView>(R.id.name) as TextView
        layout_name = findViewById<TextView>(R.id.layout_name) as TextView
        title = findViewById<TextView>(R.id.song) as TextView
        artiste = findViewById<TextView>(R.id.artiste) as TextView
        playButton = findViewById<ImageView>(R.id.play) as ImageView
        cover = findViewById<ImageView>(R.id.cover) as ImageView
        returnButton = findViewById<ImageView>(R.id.button) as ImageView
        setFont(name!!, "Archive.otf", "RUN RADIO")
        setFont(layout_name!!, "Geomanist-Book.otf", "En direct")

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
        returnButton!!.setOnClickListener {
            var intent = Intent(this, KMainActivityl::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }

    private val onButtonClick = View.OnClickListener { v ->
        if (buttonOk == true) {
            Log.i("DEBUG", "" + v.id)
            when (v.id) {
                R.id.play -> {
                    if (player!!.isPlaying) {
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
            var conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            override fun run() {
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    if (nbrRepetitions > 0) {
                        song = KSongInformations().getSongInformations()
                        title!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                if (buttonOk == false) {
                                    playButton!!.setImageResource(R.drawable.play_button)
                                    buttonOk = true

                                }
                                setFont(title!!, "OpenSans-Semibold.ttf", song!![0].name!!)
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
                                setFont(artiste!!, "OpenSans-Regular.ttf", song!![0].artiste!!)

                                println("change artiste")
                                i++
                                if (i == 100)
                                    i = 0
                                artiste!!.postDelayed(this, 5000)
                            }
                        })
                        cover!!.post(object : Runnable {
                            internal var i = 0
                            override fun run() {
                                if (song!![0].pochette == "null") {
                                    cover!!.setImageResource(R.drawable.cover)
                                } else {
                                    Picasso.with(applicationContext).load(song!![0].pochette).transform(RoundedCornersTransformation(20, 20)).into(cover)

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

    companion object {

        private val UPDATE_FREQUENCY = 500
        private val STEP_VALUE = 4000
    }
}