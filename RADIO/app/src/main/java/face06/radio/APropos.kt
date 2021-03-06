package face06.radio

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView



/**
 * Created by Alexandre Leick on 20/09/2017,
 * StaffBooker Company.
 */

class APropos : Activity() {

    var name: TextView?= null
    var faq_text: TextView?= null
    var return_bar: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_propos_layout)
        name = findViewById<TextView>(R.id.name)
        faq_text = findViewById<TextView>(R.id.faq_text)
        return_bar = findViewById<ImageView>(R.id.return_bar)
        setFont(name!!, "Geomanist-Book.otf", "A propos")
        setFont(faq_text!!, "Geomanist-Book.otf", setText = "La Radio Universitaire Niçoise (RUN) est\n une webradio qui a pour vocation de \nconnecter d'une nouvelle manière les \ncampus azuréens de l'Université Nice \nSophia Antipolis.\n\n\n9 Rue d’Alsace Lorraine, 06000 Nice")
        return_bar!!.setOnClickListener {
            startActivity(Intent(this, Parametre::class.java))
            finish()
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

}
