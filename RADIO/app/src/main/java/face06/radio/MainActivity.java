package face06.radio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

import static face06.radio.SongInformations.getSongInformations;

public class MainActivity extends AppCompatActivity {
    static     boolean whoDialog;
    SharedPreferences prefs = null;
    Button play;
    Button pause;
    TextView title;
    TextView artist;
    ImageView cover;
    boolean started;
    ArrayList<SongInformations> song;

    MediaPlayer mediaPlayer;
    String streaming = "http://radioking.com/play/run-radio";
    boolean prepared = false;
    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.i("DEBUG", "BOOL = " + started);
        prefs = getSharedPreferences("face06.radio.RADIO", MODE_PRIVATE);
        play = (Button) findViewById(R.id.play);
        play.setEnabled(false);

        pause = (Button) findViewById(R.id.pause);

        title = (TextView) findViewById(R.id.song);
        artist = (TextView) findViewById(R.id.artiste);
        cover = (ImageView) findViewById(R.id.pochette);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(streaming);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    started = true;
                    whoDialog = true;
                    mediaPlayer.start();
                    title.setText((song.get(0).getName()));
                    artist.setText(song.get(0).getArtiste());
                    if (song.get(0).getPochette() != null && song.get(0).getPochette() != "") {
                        Picasso.with(getApplicationContext()).load(song.get(0).getPochette()).into(cover);
                    }
                }
            });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                started = false;
                mediaPlayer.pause();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i("DEBUG", "SAVE");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i("DEBUG", "RESTORE");

    }

    class PlayerTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                mediaPlayer.setDataSource(strings[0]);
                mediaPlayer.prepare();
                prepared = true;
                new RepetAction();


            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            play.setEnabled(true);
        }
    }


    @Override
    protected void onResume() {
        Log.i("DEBUG", "RESUME" + whoDialog);
        if (whoDialog == false)
            myDialog();

        super.onResume();
        //if (started) {
          //  mediaPlayer.start();
        //}
    }

void myDialog() {
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
            context);

    // set title
    alertDialogBuilder.setTitle("Your Title");

    // set dialog message
    alertDialogBuilder
            .setMessage("Click yes to exit!")
            .setCancelable(false)
            .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    SystemClock.sleep(2500);
                    started = true;
                    mediaPlayer.start();
                    title.setText((song.get(0).getName()));
                    artist.setText(song.get(0).getArtiste());
                    if (song.get(0).getPochette() != null && song.get(0).getPochette() != "") {
                        Picasso.with(getApplicationContext()).load(song.get(0).getPochette()).into(cover);
                    }
                    whoDialog = true;
                    dialog.cancel();
                    //MainActivity.this.finish();
                }
            })
            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog,int id) {
                    // if this button is clicked, just close
                    // the dialog box and do nothing
                    finishActivity(0);
                }
            });

    // create alert dialog
    AlertDialog alertDialog = alertDialogBuilder.create();

    // show it
    alertDialog.show();
}
    class RepetAction {
        Timer t;

        public RepetAction() {
            t = new Timer();
            t.schedule(new MonAction(), 0, 10*1000);
        }

        class MonAction extends TimerTask {
            int nbrRepetitions = 100;

            public void run() {
                if (nbrRepetitions > 0) {
                    song = getSongInformations();
                    title.post(new Runnable() {
                        int i = 0;
                        @Override
                        public void run() {
                            title.setText((song.get(0).getName()));
                            System.out.println("changeText");
                            i++;
                            if (i ==100)
                                i = 0;
                            title.postDelayed(this, 5000);
                        }
                    });
                    artist.post(new Runnable() {
                        int i = 0;
                        @Override
                        public void run() {
                            artist.setText((song.get(0).getArtiste()));
                            System.out.println("changeArtiste");
                            i++;
                            if (i ==100)
                                i = 0;
                            title.postDelayed(this, 5000);
                        }
                    });
                    cover.post(new Runnable() {
                        int i = 0;
                        @Override
                        public void run() {
                            if (song.get(0).getPochette() != null && song.get(0).getPochette() != "") {
                                Picasso.with(getApplicationContext()).load(song.get(0).getPochette()).into(cover);
                                System.out.println("Pochette OK");
                            }
                            i++;
                            if (i ==100)
                                i = 0;
                            title.postDelayed(this, 5000);
                        }
                    });
                    System.out.println("Ca bosse dur!");
                    nbrRepetitions--;
                } else {
                    System.out.println("Terminé!");
                    t.cancel();
                }
            }
        }
    }
}
