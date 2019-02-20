package com.example.android.earthquake;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=4";
    private WordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = findViewById(R.id.list);

         mAdapter = new WordAdapter(this,new ArrayList<Word>());

        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Word currentWord = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentWord.getmUrl());

                Intent intent = new Intent(Intent.ACTION_VIEW,earthquakeUri);

                startActivity(intent);


            }
        });

        EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        task.execute(USGS_REQUEST_URL);


    }

    private  class EarthquakeAsyncTask extends AsyncTask<String,Void,List<Word>>{


        @Override
        protected List<Word> doInBackground(String... url) {
         if (url.length < 1 || url[0] == null){
             return null ;
         }

         List<Word> result = QueryUtils.fetchEarthquakeData(url[0]);

        return result;
        }

        @Override
        protected void onPostExecute(List<Word> wordList) {
            mAdapter.clear();

            if(wordList != null&& !wordList.isEmpty() ) {


                mAdapter.addAll(wordList);
            }

        }
    }

}
