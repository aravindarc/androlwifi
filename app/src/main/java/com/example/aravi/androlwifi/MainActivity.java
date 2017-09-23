package com.example.aravi.androlwifi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AlertDialog;
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
    final public int MAX = 10;
    public boolean flag = false;

    public static final String MyPREFERENCES = "aravind";
    public static final String[] storedNames = {"keyValue0", "keyValue1", "keyValue2", "keyValue3", "keyValue4", "keyValue5", "keyValue6", "keyValue7", "keyValue8", "keyValue9"};//device names storage
    private static final int REQ_CODE_SPEECH_INPUT = 100;
    SharedPreferences sharedpreferences;


    public int id;
    public TextView temp;// temp variables


    public LinearLayout[] linearLayoutButtons = new LinearLayout[10];

    public TextView[] deviceTextView = new TextView[10];//TextViews of all devices

    public EditText dialogEditText;
    public LinearLayout dialogLinearLayout;//Change device name dialogLinearLayout variables

    /*............................................................................................*/


    /*http connection declarations*/
    /*............................................................................................*/

    final Context context = this;
    private String ipAddress = "192.168.43.254";
    private Switch[] switches = new Switch[10];

    /*............................................................................................*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        refreshSwitches();

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
        linearLayoutButtons[5] = (LinearLayout)findViewById(R.id.VI_linear_layout_button);
        linearLayoutButtons[6] = (LinearLayout)findViewById(R.id.VII_linear_layout_button);
        linearLayoutButtons[7] = (LinearLayout)findViewById(R.id.VIII_linear_layout_button);
        linearLayoutButtons[8] = (LinearLayout)findViewById(R.id.IX_linear_layout_button);
        linearLayoutButtons[9] = (LinearLayout)findViewById(R.id.X_linear_layout_button);

        deviceTextView[0] = (TextView)findViewById(R.id.IdeviceName);
        deviceTextView[1] = (TextView)findViewById(R.id.IIdeviceName);
        deviceTextView[2] = (TextView)findViewById(R.id.IIIdeviceName);
        deviceTextView[3] = (TextView)findViewById(R.id.IVdeviceName);
        deviceTextView[4] = (TextView)findViewById(R.id.VdeviceName);
        deviceTextView[5] = (TextView)findViewById(R.id.VIdeviceName);
        deviceTextView[6] = (TextView)findViewById(R.id.VIIdeviceName);
        deviceTextView[7] = (TextView)findViewById(R.id.VIIIdeviceName);
        deviceTextView[8] = (TextView)findViewById(R.id.IXdeviceName);
        deviceTextView[9] = (TextView)findViewById(R.id.XdeviceName);

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
        switches[5] = (Switch)findViewById(R.id.VI_switch);
        switches[6] = (Switch)findViewById(R.id.VII_switch);
        switches[7] = (Switch)findViewById(R.id.VIII_switch);
        switches[8] = (Switch)findViewById(R.id.IX_switch);
        switches[9] = (Switch)findViewById(R.id.X_switch);

        /*........................................................................................*/


        for(int i=0; i<MAX; i++)
            deviceTextView[i].setText(sharedpreferences.getString(storedNames[i], "Long press to edit"));

        View.OnLongClickListener listener = new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                LinearLayout linearLayout = (LinearLayout) v;
                final int childCount = linearLayout.getChildCount();
                for(int i=0; i<childCount; i++) {
                    View element = linearLayout.getChildAt(i);
                    if(element instanceof TextView) {
                        TextView textView = (TextView) element;
                        temp = textView;
                        String forCompare = temp.getText().toString();

                        for(int j=0; j<MAX; j++)
                            if(linearLayout.getId() == linearLayoutButtons[j].getId())
                                id = j;

                        MainActivity.this.onLongClick();
                        break;
                    }
                }
                return true;
            }
        };

        linearLayoutButtons[0].setOnLongClickListener(listener);
        linearLayoutButtons[1].setOnLongClickListener(listener);
        linearLayoutButtons[2].setOnLongClickListener(listener);
        linearLayoutButtons[3].setOnLongClickListener(listener);
        linearLayoutButtons[4].setOnLongClickListener(listener);
        linearLayoutButtons[5].setOnLongClickListener(listener);
        linearLayoutButtons[6].setOnLongClickListener(listener);
        linearLayoutButtons[7].setOnLongClickListener(listener);
        linearLayoutButtons[8].setOnLongClickListener(listener);
        linearLayoutButtons[9].setOnLongClickListener(listener);

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

            int index = -1;

            for(int i=0; i<MAX; i++)
                if(view == switches[i]) {
                    index = i;
                    break;
                }

            if(switches[index].isChecked())
                message = "D"+index+"-ON";
            else
                message = "D"+index+"-OFF";

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
        int[] indexList = new int[MAX];


        for (int i = 0; i < MAX; i++)
            indexList[i] = inputString.toLowerCase().indexOf(deviceTextView[i].getText().toString().toLowerCase());

        int on = inputString.toLowerCase().indexOf("on");
        int off = inputString.toLowerCase().indexOf("off");
        int all = inputString.toLowerCase().indexOf("all");

        boolean flag = true;



            for(int i=0; i<MAX; i++) {
                if (indexList[i] != -1) {
                    if (on != -1)
                        message = "D" + i + "-ON";
                    else if(off != -1)
                        message = "D" + i + "-OFF";
                    flag = false;
                    break;
                }
            }

            if(all != -1) {
                if(on != -1)
                    message = "ALL-ON";
                else if(off != -1)
                    message = "ALL-OFF";
                flag = false;
            }

        if( flag )
            makeToast("Unrecognized voice command");
        else {

            String serverAddress = ipAddress + ":" + "80";
            HttpRequestTask requestTask = new HttpRequestTask(serverAddress);
            requestTask.execute(message);

            refreshSwitches();
        }
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
        private AlertDialog dialog;

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

                String[] temp = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", };
                try {
                    for (int i = 0; i < MAX; i++) {
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




