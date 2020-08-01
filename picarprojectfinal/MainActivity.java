package com.example.picarprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.PaginatedList;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.google.gson.Gson;


public class MainActivity extends AppCompatActivity {

    Button btnAvoidance;
    Button btnRecognition;
    EditText txtIP;
    Button btnShowTable;

    Socket myAppSocket = null;
    public int count;

    public static String wifiModuleIP ="";
    public static int wifiModulePort = 0;
    public static String CMD = "0";

    public List<PicarDO> result;
    ArrayList<PicarData> picarList;
    PicarData pd;

    // Declare a DynamoDBMapper object
    public DynamoDBMapper dynamoDBMapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // AWSMobileClient enables AWS user credentials to access your table
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {

            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {

                // Add code to instantiate a AmazonDynamoDBClient
                AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
                dynamoDBMapper = DynamoDBMapper.builder()
                        .dynamoDBClient(dynamoDBClient)
                        .awsConfiguration(
                                AWSMobileClient.getInstance().getConfiguration())
                        .build();

            }
        }).execute();
        setUpCar();
        setUpNewActivity();
    }

    public void setUpNewActivity(){
        btnShowTable = (Button) findViewById(R.id.btnTable);

        btnShowTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUPTable();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        openShowTable();
                    }
                }, 2000);

            }
        });
    }

    public void openShowTable(){


        Intent intent = new Intent(this, ShowTable.class);
        intent.putParcelableArrayListExtra("cars", picarList);
        startActivity(intent);
    }

    public void setUPTable(){
        readBooks();

    }

    public void readBooks() {
        picarList = new ArrayList();
        new Thread(new Runnable() {
            @Override
            public void run() {

                DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
                result = dynamoDBMapper.scan(PicarDO.class, scanExpression);
                for (PicarDO p: result){
                    pd = new PicarData(p.get_devID(), p.get_time(), p.getDistance());
                    picarList.add(pd);
                }

            }
        }).start();
    }


    public void setUpCar(){
        btnAvoidance = (Button) findViewById(R.id.btnAvoidance);
        btnRecognition = (Button) findViewById(R.id.btnRecognition);

        txtIP = (EditText) findViewById(R.id.txtIP);

        btnAvoidance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
                CMD = "a";

                Socket_AsyncTask cmd_avoid = null;
                try {
                    cmd_avoid = new Socket_AsyncTask();
                    cmd_avoid.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnRecognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
                CMD = "r";
                Socket_AsyncTask cmd_avoid = new Socket_AsyncTask();
                cmd_avoid.execute();
            }
        });

    }

    public void getAddress(){
        String address = txtIP.getText().toString();
        String t[] = address.split(":");
        wifiModuleIP = t[0];
        wifiModulePort = Integer.valueOf(t[1]);
    }

    public class Socket_AsyncTask extends AsyncTask<Void, Void, Void> {
        Socket socket;

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                InetAddress inetAddress = InetAddress.getByName(MainActivity.wifiModuleIP);
                socket = new java.net.Socket(inetAddress, MainActivity.wifiModulePort);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeBytes(CMD);
                dataOutputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
