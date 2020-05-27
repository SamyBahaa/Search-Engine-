package com.example.searchengine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    private int mNumberOfDocuments;
    private String mSearchText;
    private String finalText;
    private SearchView searchView;
    private TextView mResultTextView;
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
        mResultTextView =  findViewById(R.id.resultTextView) ;
        mRecyclerView= findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mResultDataArrayList = new ArrayList<>();
        mAdapter = new ResultAdapter(mResultDataArrayList,this);
        mRecyclerView.setAdapter(mAdapter);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS).build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Base_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mRetrofitInterface = mRetrofit.create(RetrofitInterface.class);
//        handelDataResult();
        numbersOfDocument();


    }

    private void numbersOfDocument() {
        Call<JsonElement> call = mRetrofitInterface.executeNumbers();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                JsonElement jsonElement = response.body();
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                mNumberOfDocuments = Integer.parseInt(String.valueOf(jsonObject.get("result")));
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(SecondActivity.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
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
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSearchText = extras.getString("key");
            searchView.setQuery(mSearchText, false);
            finalText = "Your result about: " + mSearchText;
            mResultTextView.setText(finalText);
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mResultTextView.setText("Your result about: "+query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    public void getSpeechInput(View view) {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mSearchText=(result.get(0));
                    searchView.setQuery(mSearchText, false);
                    finalText="Your result about: "+mSearchText;
                    mResultTextView.setText(finalText);
                }
                break;
        }
    }

}
