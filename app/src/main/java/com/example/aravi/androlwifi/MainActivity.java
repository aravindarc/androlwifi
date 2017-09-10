package com.example.aravi.androlwifi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public boolean flag = false;

    public static final String MyPREFERENCES = "aravind";
    public static final String[] storedNames = {"keyValue1", "keyValue2", "keyValue3", "keyValue4", "keyValue5"};//device names storage
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    SharedPreferences sharedpreferences;


    public int id;
    public TextView temp;// temp variables


    public LinearLayout[] linearLayoutButtons = new LinearLayout[5];

    public TextView[] deviceTextView = new TextView[5];//TextViews of all devices

    public EditText dialogEditText;
    public LinearLayout dialogLinearLayout;//Change device name dialogLinearLayout variables

    /*............................................................................................*/


    /*http connection declarations*/
    /*............................................................................................*/

    final Context context = this;
    private String ipAddress = "192.168.43.254";
    private Switch[] switches = new Switch[5];

    /*............................................................................................*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /*layout initializations*/
        /*........................................................................................*/

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);//to store the device names permanently in ROM

        linearLayoutButtons[0] = (LinearLayout)findViewById(R.id.I_linear_layout_button);
        linearLayoutButtons[1] = (LinearLayout)findViewById(R.id.II_linear_layout_button);
        linearLayoutButtons[2] = (LinearLayout)findViewById(R.id.III_linear_layout_button);
        linearLayoutButtons[3] = (LinearLayout)findViewById(R.id.IV_linear_layout_button);
        linearLayoutButtons[4] = (LinearLayout)findViewById(R.id.V_linear_layout_button);

        deviceTextView[0] = (TextView)findViewById(R.id.IdeviceName);
        deviceTextView[1] = (TextView)findViewById(R.id.IIdeviceName);
        deviceTextView[2] = (TextView)findViewById(R.id.IIIdeviceName);
        deviceTextView[3] = (TextView)findViewById(R.id.IVdeviceName);
        deviceTextView[4] = (TextView)findViewById(R.id.VdeviceName);

        dialogEditText = (EditText)findViewById(R.id.change_edit_text);
        dialogLinearLayout = (LinearLayout)findViewById(R.id.change_linear_layout);

        /*........................................................................................*/


        /*http-connection initializations*/
        /*........................................................................................*/

        switches[0] = (Switch)findViewById(R.id.I_switch);
        switches[1] = (Switch)findViewById(R.id.II_switch);
        switches[2] = (Switch)findViewById(R.id.III_switch);
        switches[3] = (Switch)findViewById(R.id.IV_switch);
        switches[4] = (Switch)findViewById(R.id.V_switch);

        /*........................................................................................*/


        for(int i=0; i<5; i++)
            deviceTextView[i].setText(sharedpreferences.getString(storedNames[i], "Long press to edit"));


        linearLayoutButtons[0].setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                id = 0;
                temp = deviceTextView[0];
                MainActivity.this.onLongClick();
                return true;
            }
        });

        linearLayoutButtons[1].setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                id = 1;
                temp = deviceTextView[1];
                MainActivity.this.onLongClick();
                return true;
            }
        });

        linearLayoutButtons[2].setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                id = 2;
                temp = deviceTextView[2];
                MainActivity.this.onLongClick();
                return true;
            }
        });

        linearLayoutButtons[3].setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                id = 3;
                temp = deviceTextView[3];
                MainActivity.this.onLongClick();
                return true;
            }
        });

        linearLayoutButtons[4].setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                id = 4;
                temp = deviceTextView[4];
                MainActivity.this.onLongClick();
                return true;
            }
        });


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(6000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshSwitches();
                            }
                        });
                    } catch (Exception e) {
                        makeToast("Thread crashed");
                    }
                }
            }
        }).start();
    }

    private void onLongClick() {

        dialogLinearLayout.setVisibility(View.VISIBLE);
    }

    public void changeDialogSet(View view) {
        String tempToStoreEditText;
        tempToStoreEditText = dialogEditText.getText().toString();
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(storedNames[id], tempToStoreEditText);
        editor.commit();
        dialogLinearLayout.setVisibility(View.GONE);
        temp.setText(tempToStoreEditText);
        dialogEditText.setText("");
    }

    public void changeDialogClose(View view) {
        dialogEditText.setText("");
        dialogLinearLayout.setVisibility(View.GONE);
    }
    public void onSwitchClick(View view) {
        String message = "";

        if(ipAddress.equals(""))
            Toast.makeText(MainActivity.this, "Please enter the ip address...", Toast.LENGTH_SHORT).show();

        else {
            if( view == switches[0] ) {
                if ( switches[0].isChecked() )
                    message = "D0-ON";
                else
                    message = "D0-OFF";
            }
            else if( view == switches[1] ) {
                if ( switches[1].isChecked() )
                    message = "D1-ON";
                else
                    message = "D1-OFF";
            }
            else if( view == switches[2] ) {
                if( switches[2].isChecked() )
                    message = "D2-ON";
                else
                    message = "D2-OFF";
            }
            else if( view == switches[3] ) {
                if( switches[3].isChecked() )
                    message = "D3-ON";
                else
                    message = "D3-OFF";
            }
            else if( view == switches[4] ) {
                if( switches[4].isChecked() )
                    message = "D4-ON";
                else
                    message = "D4-OFF";
            }

            String serverAddress = ipAddress + ":" + "80";
            HttpRequestTask requestTask = new HttpRequestTask(serverAddress);
            requestTask.execute(message);
        }
    }

    public void onVoiceClick(View view) {
        speechPrompt();
    }

    private void speechPrompt()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        intent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault() );
        intent.putExtra( RecognizerIntent.EXTRA_PROMPT, "Say something" );
        try {
            startActivityForResult( intent, REQ_CODE_SPEECH_INPUT );
        } catch ( ActivityNotFoundException e ) {
            Toast.makeText( getApplicationContext(), "Speech input not supported in device!", Toast.LENGTH_SHORT ).show();
        }
    }

    @Override
    protected  void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );

        switch ( requestCode ) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    compare(result.get(0));
                }
                break;
            }
        }
    }

    protected void compare(String inputString) {

        String message = "";
        Toast.makeText(getApplicationContext(), inputString, Toast.LENGTH_SHORT).show();
        int[] indexList = new int[5];


        for (int i = 0; i < 5; i++)
            indexList[i] = inputString.toLowerCase().indexOf(deviceTextView[i].getText().toString().toLowerCase());

        int on = inputString.toLowerCase().indexOf("on");
        int off = inputString.toLowerCase().indexOf("off");
        int all = inputString.toLowerCase().indexOf("all");

        boolean flag = true;

        if (on != -1 || off != -1) {
            if (indexList[0] != -1) {
                if (on != -1)
                    message = "D0-ON";
                else
                    message = "D0-OFF";
                flag = false;
            }

            if (indexList[1] != -1) {
                if (on != -1)
                    message = "D1-ON";
                else
                    message = "D1-OFF";
                flag = false;
            }

            if (indexList[2] != -1) {
                if (on != -1)
                    message = "D2-ON";
                else
                    message = "D2-OFF";
                flag = false;
            }

            if (indexList[3] != -1) {
                if (on != -1)
                    message = "D3-ON";
                else
                    message = "D3-OFF";
                flag = false;
            }

            if (indexList[4] != -1) {
                if (on != -1)
                    message = "D4-ON";
                else
                    message = "D4-OFF";
                flag = false;
            }

            if (all != -1) {
                if (on != -1)
                    message = "ALL-ON";
                else
                    message = "ALL-OFF";
                flag = false;
            }

            String serverAddress = ipAddress + ":" + "80";
            HttpRequestTask requestTask = new HttpRequestTask(serverAddress);
            requestTask.execute(message);
        }


        if( flag )
            makeToast("Unrecognized voice command");

    }

    public void refreshSwitches() {
        flag = true;
        String serverAddress = ipAddress + ":" + "80";
        HttpRequestTask requestTask = new HttpRequestTask(serverAddress);
        requestTask.execute("1");
    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        private String serverAddress;
        private String serverResponse = "";

        public HttpRequestTask(String serverAddress) {
            this.serverAddress = serverAddress;
        }

        @Override
        protected String doInBackground(String... params) {
            String val = params[0];
            final  String url = "http://" + serverAddress + "/message/" + val;

            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet();
                getRequest.setURI(new URI(url));
                HttpResponse response = client.execute(getRequest);

                InputStream inputStream = null;
                inputStream = response.getEntity().getContent();
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));

                serverResponse = bufferedReader.readLine();
                inputStream.close();

            } catch (URISyntaxException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                serverResponse = e.getMessage();
            }

            return serverResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            if( flag ) {
                int switchStateSetter = -1;

                String[] temp = { "A", "B", "C", "D", "E" };
                try {
                    for (int i = 0; i < 5; i++) {
                        switchStateSetter = serverResponse.indexOf(temp[i]);
                        if (switchStateSetter != -1) {
                            switches[i].setChecked(true);
                        } else {
                            switches[i].setChecked(false);
                        }
                    }
                } catch (Exception e) {
                    makeToast("error");
                }
                flag = false;
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }

    void makeToast( String string ) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
}




