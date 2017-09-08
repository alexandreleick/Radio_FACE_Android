package face06.radio

import android.app.ListActivity
import android.content.Context
import android.database.Cursor
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import org.w3c.dom.Text
import java.io.IOException
import java.lang.Double
import java.math.BigDecimal
import java.util.*

/**
 * Created by Alexandre Leick on 20/08/2017,
 * StaffBooker Company.
 */

class PlayAudioExample : AppCompatActivity() {

    private var title: TextView? = null
    private var seekbar: SeekBar? = null
    private var player: MediaPlayer? = null
    private var playButton: ImageButton? = null
    internal var song: ArrayList<KSongInformations>? = null
    internal var streaming = "http://radioking.com/play/run-radio"
    internal val context: Context = this
    internal var cover: ImageView? = null
    var buttonOk: Boolean = false
    private var isStarted = true
    private var currentFile = ""
    private var isMoveingSeekBar = false

    private val handler = Handler()

    private val updatePositionRunnable = Runnable { updatePosition() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.player_view)

        title = findViewById(R.id.song) as TextView
        seekbar = findViewById(R.id.seekbar) as SeekBar
        playButton = findViewById(R.id.play) as ImageButton
        cover = findViewById(R.id.cover) as ImageView


        player = MediaPlayer()
        player!!.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
        player!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        PlayerTask().execute(streaming)

        player!!.setOnCompletionListener(onCompletion)
        player!!.setOnErrorListener(onError)
        seekbar!!.setOnSeekBarChangeListener(seekBarChanged)
        playButton!!.setImageResource(android.R.drawable.ic_menu_upload)
        playButton!!.setOnClickListener(onButtonClick)
    }

    internal inner class PlayerTask : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg strings: String): Boolean? {
            try {
                player!!.setDataSource(strings[0])
                player!!.prepare()
                RepetAction()


            } catch (e: IOException) {
                e.printStackTrace()
            }

            return true
        }

        override fun onPostExecute(aBoolean: Boolean?) {
            super.onPostExecute(aBoolean)
            playButton!!.isEnabled = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        //handler.removeCallbacks(updatePositionRunnable)
        //player!!.stop()
        //player!!.reset()
        //player!!.release()

       // player = null
    }

    private fun startPlay(file: String) {
        Log.i("Selected: ", file)

        title!!.text = file
        seekbar!!.progress = 0

        player!!.stop()
        player!!.reset()

        try {
            player!!.setDataSource(file)
            player!!.prepare()
            player!!.start()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        seekbar!!.max = player!!.duration
        playButton!!.setImageResource(android.R.drawable.ic_media_pause)

        updatePosition()

        isStarted = true
    }

    private fun stopPlay() {
        player!!.stop()
        player!!.reset()
        playButton!!.setImageResource(android.R.drawable.ic_media_play)
        handler.removeCallbacks(updatePositionRunnable)
        seekbar!!.progress = 0

        isStarted = false
    }

    private fun updatePosition() {
        handler.removeCallbacks(updatePositionRunnable)

        seekbar!!.progress = player!!.currentPosition

        handler.postDelayed(updatePositionRunnable, UPDATE_FREQUENCY.toLong())
    }

    private val onButtonClick = View.OnClickListener { v ->
        if (buttonOk == true) {
            when (v.id) {
                R.id.play -> {
                    if (player!!.isPlaying) {
                        handler.removeCallbacks(updatePositionRunnable)
                        player!!.pause()
                        playButton!!.setImageResource(android.R.drawable.ic_media_play)
                    } else {
                        if (isStarted) {
                            player!!.start()
                            playButton!!.setImageResource(android.R.drawable.ic_media_pause)

                            updatePosition()

                        }
                    }
                }
            }
        }
    }

    private val onCompletion = MediaPlayer.OnCompletionListener { stopPlay() }

    private val onError = MediaPlayer.OnErrorListener { mp, what, extra -> false }

    private val seekBarChanged = object : SeekBar.OnSeekBarChangeListener {
        override fun onStopTrackingTouch(seekBar: SeekBar) {
            isMoveingSeekBar = false
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
            isMoveingSeekBar = true
        }

        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            if (isMoveingSeekBar) {
                player!!.seekTo(progress)

                Log.i("OnSeekBarChangeListener", "onProgressChanged")
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

            override fun run() {
                if (nbrRepetitions > 0) {
                    song = KSongInformations().getSongInformations()
                    title!!.post(object : Runnable {
                        internal var i = 0
                        override fun run() {
                            title!!.text = song!![0].name + " - " + song!![0].artiste
                            if (buttonOk == false) {
                                playButton!!.setImageResource(android.R.drawable.ic_media_play)
                                buttonOk = true

                            }

                            println("changeText")
                            i++
                            if (i == 100)
                                i = 0
                            title!!.postDelayed(this, 5000)
                        }
                    })
                    cover!!.post(object : Runnable {
                        internal var i = 0
                        override fun run() {
                            if (song!![0].pochette == "null") {
                                cover!!.setImageResource(R.drawable.cover)
                            } else {
                                Picasso.with(applicationContext).load(song!![0].pochette).into(cover)

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

        private val UPDATE_FREQUENCY = 500
        private val STEP_VALUE = 4000
    }
}