package com.example.searchengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;

import com.mongodb.Block;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.json.JSONArray;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    private TextView test;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ResultData> mResultDataArrayList;
    private JSONArray retArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        test =  findViewById(R.id.resultTextView) ;
        FetchResult mFetchResult = new FetchResult();
        mFetchResult.execute();
        mRecyclerView= findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResultDataArrayList = new ArrayList<>();
        mAdapter = new ResultAdapter(mResultDataArrayList,this);
        mRecyclerView.setAdapter(mAdapter);
        retArray = new JSONArray();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchView.setQuery(extras.getString("key"), false);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    private class FetchResult extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            getData();
            return null;
        }
    }

    public void getData(){
//      MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
        MongoClient mongoClient = new MongoClient("Localhost",27017);
        MongoDatabase database =  mongoClient.getDatabase("result");
        MongoCollection<Document> collection =  database.getCollection("result");
       collection.find().forEach(new Block<Document>() {
           @Override
           public void apply(Document document) {
               retArray.put(document);
           }
       });
    }
}
