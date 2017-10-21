package face06.radio

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Alexandre Leick on 10/09/2017,
 * StaffBooker Company.
 */

class Parametre : Activity() {
    var name: TextView?= null
    var layout_name: TextView?= null
    var propos: ImageView?= null
    var eval: ImageView?= null
    var contact: ImageView?= null
    var return_bar: ImageView?= null
    var facebook: ImageView?= null
    var insta: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.parametres)
        name = findViewById<TextView>(R.id.name) as TextView
        layout_name = findViewById<TextView>(R.id.layout_name) as TextView
        propos = findViewById<ImageView>(R.id.a_propos) as ImageView
        eval = findViewById<ImageView>(R.id.evaluer) as ImageView
        contact = findViewById<ImageView>(R.id.contact) as ImageView
        return_bar = findViewById<ImageView>(R.id.return_bar) as ImageView
        facebook = findViewById<ImageView>(R.id.facebook) as ImageView
        insta = findViewById<ImageView>(R.id.insta) as ImageView
        setFont(name!!, "Archive.otf", "RUN RADIO")
        setFont(layout_name!!, "Geomanist-Book.otf", "Paramètre")

        contact!!.setOnClickListener({
            val mails = arrayOf("alexandre.leick@epitech.eu")
            val i = Intent(Intent.ACTION_SEND)
            i.type = "plain/text"
            i.putExtra(Intent.EXTRA_SUBJECT, "Contact Run Radio Android")
            i.putExtra(Intent.EXTRA_EMAIL, mails)
            startActivity(Intent.createChooser(i, "Choix de l'application"))
        })
        propos!!.setOnClickListener({
            startActivity(Intent(this, APropos::class.java))
        })
        return_bar!!.setOnClickListener({
            startActivity(Intent(this, KMainActivityl::class.java))
        })

        facebook!!.setOnClickListener({
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.facebook.com/RunRadioNice/")
            startActivity(intent)
        })

        insta!!.setOnClickListener({
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.data = Uri.parse("https://www.instagram.com/runradio/")
            startActivity(intent)
        })
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