package face06.radio

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Alexandre Leick on 19/08/2017,
 * StaffBooker Company.
 */
//sPos: Array<Int>
class LastSongViewController(context: Context, infoSong: ArrayList<KSongInformations>) : BaseAdapter() {
    private val mInflator: LayoutInflater
    var context: Context
    //var infoSong: ArrayList<KSongInformations>
    var infoSong: ArrayList<KSongInformations>
    //var sPos: Array<Int>
    init {
        this.mInflator = LayoutInflater.from(context)
        this.infoSong = infoSong
        this.context = context
      //  this.sPos = sPos
    }

    override fun getCount(): Int {
        return infoSong.size
    }

    override fun getItem(position: Int): Any {
        return infoSong[position]!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val vh: KTableViewCell
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.row_last_song, parent, false)
            vh = KTableViewCell(view!!)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as KTableViewCell
        }
        setFont(vh.name_search, "Geomanist-Book.otf", infoSong[position].name!!)
        //vh.cover.setImageResource(infoSong[position].pochette)
        if (infoSong[position].pochette == "null" || infoSong[position].pochette == "") {
            vh.cover.setImageResource(R.drawable.cover)
            vh.cover.layoutParams.width = 200
            vh.cover.layoutParams.height = 200
        } else {
            Picasso.with(context).load(infoSong!![position].pochette).into(vh.cover)
            vh.cover.layoutParams.width = 200
            vh.cover.layoutParams.height = 200
        }
        setFont(vh.artist_search, "Geomanist-Light.otf", infoSong[position].artiste!!)
        return view
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