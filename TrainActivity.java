package com.example.kyj.traininfo;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import fr.arnaudguyon.xmltojsonlib.XmlToJson;

public class TrainActivity extends AppCompatActivity {

    AssetManager assetManager;
    TextView textView;
    Document doc = null;
    TrainInfo trainInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
        setTitle("hello");

        textView = (TextView)findViewById(R.id.textView);
        trainInfo = new TrainInfo();


//        Intent intent = getIntent();
//        String text = intent.getExtras().getString("key");
//        textView.setText(text);
//        setTitle(text);

//        GetXMLTask task = new GetXMLTask();
//        task.execute("http://220.95.178.158:82/trainruninfo.aspx?lineno=2");

    }

    public void go(View view){
//        GetXMLTask task = new GetXMLTask();
//        task.execute("http://220.95.178.158:82/trainruninfo.aspx?lineno=2");
        GetXMLTaskFile task = new GetXMLTaskFile();
        task.execute("tcmsinfo.xml");
        Toast.makeText(getBaseContext(), "btn click", Toast.LENGTH_SHORT).show();
//        textView.setText(readfile("tcmsinfo.xml"));

    }

    public void gourl(View view){

        // URL 설정.
//        String url = "http://       .cafe24.com/LoadPat        ";
//        String url = "http://220.95.178.158:82/trainruninfo.aspx?lineno=2";
        String url = "http://101.252.2.84/tcmsinfo.aspx?lineno=1&&orgno=47";

        // AsyncTask를 통해 HttpURLConnection 수행.
        NetworkTask networkTask = new NetworkTask(url, null);
        networkTask.execute();
        Toast.makeText(getBaseContext(), "btn HTTP click", Toast.LENGTH_SHORT).show();
//        textView.setText(readfile("tcmsinfo.xml"));

    }

    private String readfile(String filename){
        BufferedReader reader = null;
        String ss = "int";
        String mLine = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open(filename), "UTF-8"));

            // do reading, usually loop until end of file reading
            while ((mLine = reader.readLine()) != null) {
                //process line
                ss += mLine;

            }
        } catch (IOException e) {
            //log the exception
            ss ="error";
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return ss;
    }

    //private inner class extending AsyncTask
    private class GetXMLTaskFile extends AsyncTask<String, Void, TrainInfo> {



        @Override
        protected TrainInfo doInBackground(String... urls) {

            AssetManager assetManager = getAssets();
            InputStream inputStream = null;
            XmlToJson xmlToJson = null;
            try {
                inputStream = assetManager.open("tcmsinfo.xml");
                xmlToJson = new XmlToJson.Builder(inputStream, null).build();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String result = xmlToJson.toFormattedString();
            JSONObject jsonObject = xmlToJson.toJson();

            try {
                JSONObject rootObj = jsonObject.getJSONObject("Humetro");
                JSONObject trainsObj = rootObj.getJSONObject("Trains");
                JSONObject trainObj = trainsObj.getJSONObject("Train");
//                result = "object\n"+trainObj.getString("OrgNo");

                trainInfo.setCarType(trainObj.getString("CarType"));
                trainInfo.setDestStationNo(trainObj.getString("DestStationNo"));
                trainInfo.setLastWriteTime(trainObj.getString("LastWriteTime"));
                trainInfo.setLineNo(trainObj.getString("LineNo"));
                trainInfo.setNxtStationNo(trainObj.getString("NxtStationNo"));
                trainInfo.setSpeed(trainObj.getString("Speed"));
                trainInfo.setOrgNo(trainObj.getString("OrgNo"));
                trainInfo.setStationNo(trainObj.getString("StationNo"));
                trainInfo.setTrainNo(trainObj.getString("TrainNo"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return trainInfo;

        }

        @Override
        protected void onPostExecute(TrainInfo ti) {
            String ss = ti.getDestStationNo();

            textView.setText(ss);
            super.onPostExecute(ti);
        }

    }//end inner class - GetXMLTask

    //private inner class extending AsyncTask
    private class GetXMLTask extends AsyncTask<String, Void, Document> {


        @Override
        protected Document doInBackground(String... urls) {

            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder(); //XML문서 빌더 객체를 생성
                doc = db.parse(new InputSource(url.openStream())); //XML문서를 파싱한다.
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error", Toast.LENGTH_SHORT).show();
            }
            return doc;

        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            //data태그가 있는 노드를 찾아서 리스트 형태로 만들어서 반환
            NodeList nodeList = doc.getElementsByTagName("Train");
            //data 태그를 가지는 노드를 찾음, 계층적인 노드 구조를 반환

            for(int i = 0; i< nodeList.getLength(); i++){

                //날씨 데이터를 추출
                s += "" +i + ": 열차 정보 : ";
                Node node = nodeList.item(i); //data엘리먼트 노드
                Element fstElmnt = (Element) node;
                NodeList nameList  = fstElmnt.getElementsByTagName("TrainNo");
                Element nameElement = (Element) nameList.item(0);
                nameList = nameElement.getChildNodes();
                s += "열번 = "+ ((Node) nameList.item(0)).getNodeValue() +" ,";

                NodeList websiteList = fstElmnt.getElementsByTagName("StationName");
                //<wfKor>맑음</wfKor> =====> <wfKor> 태그의 첫번째 자식노드는 TextNode 이고 TextNode의 값은 맑음
                s += "현재역 = "+  websiteList.item(0).getChildNodes().item(0).getNodeValue() +"\n";
            }

            textView.setText(s);
            super.onPostExecute(doc);
        }

    }//end inner class - GetXMLTask

    public class NetworkTask extends AsyncTask<Void, Void, TrainInfo> {

        private String url;
        private ContentValues values;

        public NetworkTask(String url, ContentValues values) {

            this.url = url;
            this.values = values;
        }

        @Override
        protected TrainInfo doInBackground(Void... params) {

            String result; // 요청 결과를 저장할 변수.
            RequestHttpConnection requestHttpURLConnection = new RequestHttpConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.

            XmlToJson xmlToJson = new XmlToJson.Builder(result).build();
//            result = xmlToJson.toFormattedString();

            JSONObject jsonObject = xmlToJson.toJson();

            try {
                JSONObject rootObj = jsonObject.getJSONObject("Humetro");
                JSONObject trainsObj = rootObj.getJSONObject("Trains");
                JSONObject trainObj = trainsObj.getJSONObject("Train");
//                result = "object\n"+trainObj.getString("OrgNo");

                trainInfo.setCarType(trainObj.getString("CarType"));
                trainInfo.setDestStationNo(trainObj.getString("DestStationNo"));
                trainInfo.setLastWriteTime(trainObj.getString("LastWriteTime"));
                trainInfo.setLineNo(trainObj.getString("LineNo"));
                trainInfo.setNxtStationNo(trainObj.getString("NxtStationNo"));
                trainInfo.setSpeed(trainObj.getString("Speed"));
                trainInfo.setOrgNo(trainObj.getString("OrgNo"));
                trainInfo.setStationNo(trainObj.getString("StationNo"));
                trainInfo.setTrainNo(trainObj.getString("TrainNo"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trainInfo;
        }

        @Override
        protected void onPostExecute(TrainInfo ti) {
            super.onPostExecute(ti);


            String result = "json : "+ti.toString();

            textView.setText(result);
        }
    }
}
