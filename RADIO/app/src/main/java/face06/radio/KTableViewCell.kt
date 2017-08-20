package face06.radio

import android.view.View
import android.widget.ImageView
import android.widget.TextView


/**
 * Created by alexandre on 17/08/17.
 */

open class KTableViewCell(row: View?) {
    val name_search: TextView
    val cover: ImageView
    val artist_search: TextView

    init {
        this.name_search = row?.findViewById<TextView>(R.id.name_search) as TextView
        this.artist_search = row?.findViewById<TextView>(R.id.artist_search) as TextView
        this.cover = row?.findViewById<ImageView>(R.id.cover) as ImageView

    }
}