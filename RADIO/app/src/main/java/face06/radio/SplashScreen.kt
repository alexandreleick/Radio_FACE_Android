package face06.radio

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog

/**
 * Created by Alexandre Leick on 08/09/2017,
 * StaffBooker Company.
 */

/**
 * Classe permettant de lancer une page de chargement avec un gif afin de charger durée le temps
 * de chargement le JSON de la radio ainsi que le playerMedia contenant le lien streaming.
 */

class  SplashScreen : Activity() {
    companion object {
        var SPLASH_TIME_OUT: Long = 3000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        var conMgr = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            if (APIRadio.getShared().init == 0) {
                APIRadio.getShared().manageRadio(applicationContext, { player: MediaPlayer ->
                    APIRadio.getShared().player = player
                    APIRadio.getShared().bool = false
                    APIRadio.getShared().init = 1
                    Handler().postDelayed(Runnable {
                        kotlin.run {
                            var i: Intent = Intent(this, KMainActivityl::class.java)
                            startActivity(i)
                            finish()
                        }
                    }, SPLASH_TIME_OUT)
                })
            }
            Handler().postDelayed(Runnable {
                kotlin.run {
                    var i: Intent = Intent(this, KMainActivityl::class.java)
                    startActivity(i)
                    finish()
                }
            }, SPLASH_TIME_OUT)
        } else {
            val simpleAlert = AlertDialog.Builder(this@SplashScreen).create()
            simpleAlert.setTitle("Attention")
            simpleAlert.setMessage("Vous n'êtes pas connecté à Internet ! Merci de vous connecter à un réseau WIFI - MOBILE")
            simpleAlert.setButton(AlertDialog.BUTTON_POSITIVE, "OK", { dialogInterface, i ->
                SplashScreen().finish()
            })
            simpleAlert.show()
        }


    }
}

