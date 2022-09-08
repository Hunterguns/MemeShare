package com.example.memeshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private Button shareButton,nextButton;
    private ProgressBar progressBar;
    private ImageView memeImage;
    private String imageUrl;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareButton=findViewById(R.id.shareButton);
        nextButton=findViewById(R.id.nextButton);
        memeImage=findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
        loadMeme();
        Log.d(TAG,"func ran once");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMeme();
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = imageUrl;
                intent.putExtra(Intent.EXTRA_SUBJECT,"Sharing Subject here");
                intent.putExtra(Intent.EXTRA_TEXT,"Hey...Check out this cool meme I just found "+ shareBody);
                startActivity(Intent.createChooser(intent, "Share this meme link via"));
            }
        });
    }

    private void loadMeme(){
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://meme-api.herokuapp.com/gimme";
         JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 try {
                     imageUrl = response.getString("url");
                     Glide.with(MainActivity.this).asBitmap().load(imageUrl).listener(new RequestListener<Bitmap>() {
                         @Override
                         public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                             progressBar.setVisibility(View.GONE);
                             return false;
                         }

                         @Override
                         public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                             progressBar.setVisibility(View.GONE);
                             return false;
                         }
                     }).into(memeImage);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(MainActivity.this, "Something went Wrong...Please Try Again!!!", Toast.LENGTH_SHORT).show();
             }
         });


         MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);


    }
}