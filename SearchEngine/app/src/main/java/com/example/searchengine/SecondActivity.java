package com.example.searchengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
import com.mongodb.util.JSON;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.UnknownHostException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    private TextView test;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<ResultData> mResultDataArrayList;
    private JsonElement mJson;
    private String mJsonString;
    private RetrofitInterface mRetrofitInterface;
    private Retrofit mRetrofit;
    private String Base_URL="http://192.168.1.9:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        test =  findViewById(R.id.resultTextView) ;
        mRecyclerView= findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResultDataArrayList = new ArrayList<>();
        mAdapter = new ResultAdapter(mResultDataArrayList,this);
        mRecyclerView.setAdapter(mAdapter);
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
        handelDataResult();

    }

    private void handelDataResult() {
        Call<JsonArray> call = mRetrofitInterface.executeData();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if (response.code()==200){
                    jsonConverter(response.body());
                    Toast.makeText(SecondActivity.this,"Data get successful",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(SecondActivity.this,"Data didn't get successful",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(SecondActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void jsonConverter(JsonArray response) {
        if (response !=null){
            for (int i=0; i<response.size();i++){

                mJson = response.get(i);
                mJsonString = mJson.toString();
                Gson gson = new Gson();
                ResultData mResultData = gson.fromJson(mJsonString,ResultData.class);
                mResultDataArrayList.add(mResultData);
                mAdapter.notifyDataSetChanged();
            }
        }
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


}
