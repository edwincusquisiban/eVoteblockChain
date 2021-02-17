package com.ech.votoelectronicoseguridadinformatica;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView textView;
    static public Block block;
    public static String poid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1=findViewById(R.id.btn_1);
        button2=findViewById(R.id.btn_2);
        button3=findViewById(R.id.btn_3);
        textView=findViewById(R.id.textView2);


        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edittext, null);
        EditText editText = dialogView.findViewById(R.id.edit_text);
        AlertDialog editDialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Ingrese su código")
                .setView(dialogView)
                .setPositiveButton("OK", (dialogInterface, i) -> {
                    textView.setText(editText.getText().toString());
                    poid=(editText.getText().toString());
                })
                .create();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDialog.show();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.block=new Block(poid);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        RegistrarBlock(poid,"","");
                    }
                }).start();





                startActivity(new Intent(MainActivity.this,PollingActivity.class));

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Bloque registrado  ", Toast.LENGTH_LONG).show();
                for (String transaction :block.getTransaction()){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("transaction: "+transaction);
                            RegistrarTransact(poid,transaction);

                        }
                    }).start();



                }



            }
        });



    }







    private static String RegistrarTransact(String poid, String transact){
        String scripurl="http://seguridadinformatica.freevar.com/inserttransact.php";
        String resultado="";
        try{

            URL url= new URL(scripurl);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("poid", "UTF-8") + "=" + URLEncoder.encode(poid, "UTF-8")
                    + "&" + URLEncoder.encode("transaction","UTF-8") + "=" + URLEncoder.encode(transact, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);

            }
            resultado = stringBuilder.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        }catch (Exception e){
            System.out.println("RegistrarTecladoHost gg");
        }
        System.out.println(resultado+"----RESULTADO");

        return resultado;
    }

    private static String RegistrarBlock(String poid, String hash, String prevhash){
        String scripurl="http://seguridadinformatica.freevar.com/insertblock.php";
        String resultado="";
        try{

            URL url= new URL(scripurl);
            HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
            String data = URLEncoder.encode("poid", "UTF-8") + "=" + URLEncoder.encode(poid, "UTF-8")
                    + "&" + URLEncoder.encode("hash","UTF-8") + "=" + URLEncoder.encode(hash, "UTF-8")
                    + "&" + URLEncoder.encode("prevhash", "UTF-8") + "=" + URLEncoder.encode(prevhash, "UTF-8");


            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);

            }
            resultado = stringBuilder.toString();
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
        }catch (Exception e){
            System.out.println("RegistrarTecladoHost gg  "+e);
        }
        System.out.println(resultado+"----RESULTADO");

        return resultado;
    }







}