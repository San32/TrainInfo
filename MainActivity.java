package com.example.kyj.traininfo;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {

    String[] item;
    Button b1;
    Spinner spinner;
    String value;
    //manager
    AssetManager assetManager;

    Document doc = null;
    private String trainInfo;
//    ArrayList<Item> list = null;


    //첫 페이됨
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        item = new String[]{"선택하세요", "1","2","3","46","47","48","49","50","51"};

        b1 = (Button)findViewById(R.id.button);
        Button btnReadFile = (Button) findViewById(R.id.button2);
        spinner = (Spinner) findViewById(R.id.spinner);

        assetManager = getResources().getAssets();

        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrainActivity.class);
                intent.putExtra("key", value);
                startActivity(intent);
            }
        });

        btnReadFile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mOnAssetData(v);
                Intent intent = new Intent(getApplicationContext(), TrainActivity.class);
                intent.putExtra("key", value);
                startActivity(intent);
            }
        });


        //데이터를 저장하게 되는 리스트
        List<String> spinner_items = new ArrayList<>();

        //스피너와 리스트를 연결하기 위해 사용되는 어댑터
        ArrayAdapter<String> spinner_adapter=new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinner_items);

        spinner_items.add("선택하세요");
        spinner_items.add("1");
        spinner_items.add("2");
        spinner_items.add("3");
        spinner_items.add("4");
        spinner_items.add("5");
        spinner_items.add("46");
        spinner_items.add("47");
        spinner_items.add("48");
        spinner_items.add("49");
        spinner_items.add("50");
        spinner_items.add("51");

        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //스피너의 어댑터 지정
        spinner.setAdapter(spinner_adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                value = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void mOnAssetData(View v){
        getDataFromAsset();
    }

    public void getDataFromAsset(){
        InputStream inputStream = null;

        try{
            //asset manager에게서 inputstream 가져오기
            inputStream = assetManager.open("tcmsinfo.xml", AssetManager.ACCESS_BUFFER);

            //문자로 읽어들이기
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //파일읽기
            String strResult = "";
            String line = "";
            while((line=reader.readLine()) != null){
                strResult += line;
            }

            //읽은내용 출력
//            txtData.setText(strResult);
            value = strResult;
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try { inputStream.close(); } catch (IOException e) {}
            }
        }
    }





}
