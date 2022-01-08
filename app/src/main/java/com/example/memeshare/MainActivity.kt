package com.example.memeshare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.MySingleton.MySingleton.Companion.getInstance
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    var currentImageUrl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadMeme()

    }



   private fun loadMeme(){
       val progress = findViewById<ProgressBar>(R.id.loading)
       val memeImageView: ImageView = findViewById(R.id.memeImageView)
       progress.visibility = View.VISIBLE
        // Instantiate the RequestQueue.
        //val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
       val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
           { response ->
               currentImageUrl = response.getString("url")

               Glide.with(this).load(currentImageUrl).listener(object:RequestListener<Drawable>{
                   override fun onLoadFailed(
                       e: GlideException?,
                       model: Any?,
                       target: Target<Drawable>?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progress.visibility = View.GONE
                       Toast.makeText(applicationContext,"failed to load meme, press next",Toast.LENGTH_LONG).show()
                       return false
                   }

                   override fun onResourceReady(
                       resource: Drawable?,
                       model: Any?,
                       target: Target<Drawable>?,
                       dataSource: DataSource?,
                       isFirstResource: Boolean
                   ): Boolean {
                       progress.visibility = View.GONE
                       return false
                   }

               }).into(memeImageView)
           },
           { error ->

           }
       )

// Add the request to the RequestQueue
       //queue.add(jsonObjectRequest)
  MySingleton.MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: android.view.View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey, Checkout this amazing meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: android.view.View) {
        loadMeme()
    }
}