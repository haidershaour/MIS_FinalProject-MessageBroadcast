package com.example.shaour.myapplicationnew;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    Button button,clearbotton,trans;


    public morseplayer mp,mp2,mp3,mp4;

    private Recorder recorder;
    private AudioCalculator audioCalculator;
    private Handler handler;

    public TextView textFrequency;
    public TextView textReturnValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.RECORD_AUDIO},1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mp = new com.example.shaour.myapplicationnew.morseplayer(14550, 25);// -
        mp2 = new com.example.shaour.myapplicationnew.morseplayer(10550, 25); // .
        mp3 = new com.example.shaour.myapplicationnew.morseplayer(12550, 25); //  "/"
        //mp4 = new com.example.shaour.myapplicationnew.morseplayer(11550, 25);//  " "

        //----------thread test------------
        for(int i=1;i<2;i++){
            MyAsyncTask task = new MyAsyncTask("task"+i);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        }

        final RecordingTask RecordingTask = new RecordingTask();

        RecordingTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);

        audioCalculator = new AudioCalculator();

        textReturnValue = (TextView) findViewById(R.id.textReturnValue);
        textFrequency = (TextView) findViewById(R.id.textFrequency);

        final int dur = 140;     // (short) beep length (in ms)

        editText=(EditText) findViewById(R.id.editText);
        textView=(TextView) findViewById(R.id.textView);
        button=(Button) findViewById(R.id.button);
        clearbotton=(Button) findViewById(R.id.button2);
        trans=(Button) findViewById(R.id.trans);

        textFrequency.setText(RecordingTask.fre);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(convert(editText.getText().toString()));
                //Changing String to String Array
                String text=convert(editText.getText().toString());
                System.out.println(text);
                String strArray[] = text.split("");
                int len = strArray.length;
                int pause = 0;
                mp.playpreparing(len);

                for (int i = 0; i < len; i++) {
                    switch (strArray[i]) {

                        case ".":
                            mp2.playdot();
                            pause = 4;
                            break;
                        case "-":
                            mp.playdash();
                            pause = 4;
                            break;
                        case "/":
                            mp3.playdash();
                            pause = 4;
                            break;
                        case " ":
                            mp3.playdash();
                            pause = 4;
                            break;
                    }
                    delay (pause * dur);
                }
                delay (4 * dur);
            }
        });

        clearbotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
                textReturnValue.setText("");
                editText.setText("");
                RecordingTask.morsecode = "/";
            }
        });
        trans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Test K");
                String eng = reconvert(RecordingTask.morsecode);
                System.out.println(eng);
                textReturnValue.setText(eng.replace("//"," ").replace("/",""));
            }
        });
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission Granted,After granted restart once. Ignore!" +
                            "If Restarted Already", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Not Granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recorder Start
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Recorder Stop
    }

    private void delay (int t) {
        try {
            Thread.sleep (t);
        } catch (Exception e) { }
    }

    public String convert(String text){
        String temp="";
        temp=text.toUpperCase();
        temp=temp.replace("0","-----/");
        temp=temp.replace("1",".----/");
        temp=temp.replace("2","..---/");
        temp=temp.replace("3","...--/");
        temp=temp.replace("4","....-/");
        temp=temp.replace("5","...../");
        temp=temp.replace("6","-..../");
        temp=temp.replace("7","--.../");
        temp=temp.replace("8","---../");
        temp=temp.replace("9","----./");
        //Alpha
        temp=temp.replace("A",".-/");
        temp=temp.replace("B","-.../");
        temp=temp.replace("C","-.-./");
        temp=temp.replace("D","-../");
        temp=temp.replace("E","./");
        temp=temp.replace("F","..-./");
        temp=temp.replace("G","--./");
        temp=temp.replace("H","..../");
        temp=temp.replace("I","../");
        temp=temp.replace("J",".---/");
        temp=temp.replace("K","-.-/");
        temp=temp.replace("L",".-../");
        temp=temp.replace("M","--/");
        temp=temp.replace("N","-./");
        temp=temp.replace("O","---/");
        temp=temp.replace("P",".--./");
        temp=temp.replace("Q","--.-/");
        temp=temp.replace("R",".-./");
        temp=temp.replace("S",".../");
        temp=temp.replace("T","-/");
        temp=temp.replace("U","..-/");
        temp=temp.replace("V","...-/");
        temp=temp.replace("W",".--/");
        temp=temp.replace("X","-..-/");
        temp=temp.replace("Y","-.--/");
        temp=temp.replace("Z","--../");
        return temp;
    }

    public String reconvert(String text){
        String temp="";
        temp=text.toUpperCase();
        //Numeric
        temp=temp.replace("/////////","/   /");
        temp=temp.replace("///////","/  /");
        temp=temp.replace("/////","/ /");
        temp=temp.replace("/-----/","0");
        temp=temp.replace("/.----/","1");
        temp=temp.replace("/..---/","2");
        temp=temp.replace("/...--/","3");
        temp=temp.replace("/....-/","4");
        temp=temp.replace("/...../","5");
        temp=temp.replace("/-..../","6");
        temp=temp.replace("/--.../","7");
        temp=temp.replace("/---../","8");
        temp=temp.replace("/----./","9");
        //Alpha
        temp=temp.replace("/.-/","A");
        temp=temp.replace("/-.../","B");
        temp=temp.replace("/-.-./","C");
        temp=temp.replace("/-../","D");
        temp=temp.replace("/./","E");
        temp=temp.replace("/..-./","F");
        temp=temp.replace("/--./","G");
        temp=temp.replace("/..../","H");
        temp=temp.replace("/../","I");
        temp=temp.replace("/.---/","J");
        temp=temp.replace("/-.-/","K");
        temp=temp.replace("/.-../","L");
        temp=temp.replace("/--/","M");
        temp=temp.replace("/-./","N");
        temp=temp.replace("/---/","O");
        temp=temp.replace("/.--./","P");
        temp=temp.replace("/--.-/","Q");
        temp=temp.replace("/.-./","R");
        temp=temp.replace("/.../","S");
        temp=temp.replace("/-/","T");
        temp=temp.replace("/..-/","U");
        temp=temp.replace("/...-/","V");
        temp=temp.replace("/.--/","W");
        temp=temp.replace("/-..-/","X");
        temp=temp.replace("/-.--/","Y");
        temp=temp.replace("/--../","Z");
        return temp;
    }
    public MainActivity() {
        mainActivity = this;
    }
    public static MainActivity getMainActivity() {
        return mainActivity;
    }
    private static MainActivity mainActivity;
    public void function(){}
}
