package face06.radio

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ListView

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */
class LastSongListViewController(context: Context, sInfo: ArrayList<KSongInformations>) : Dialog(context, R.style.AppTheme) {
    //internal var sLogo = arrayOf(R.drawable.ic_help_outline_black_18dp, R.drawable.legals, R.drawable.io)
    var sInfo: ArrayList<KSongInformations>

    init {
        this.sInfo = sInfo
    }
    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)
        setContentView(R.layout.last_song_view)
        Log.i("DEBUG", "ici" + sInfo!!.size)
        val lv = findViewById<ListView>(R.id.list)
        //sInfo = KSongInformations().getSongInformations()
        var sName = arrayOf("Coucou", "BITE", "LOL")

        lv.adapter = LastSongViewController(context, sInfo!!)

    }
}