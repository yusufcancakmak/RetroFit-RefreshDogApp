package com.yusufcancakmak.refleshdogapp

import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.yusufcancakmak.refleshdogapp.api.ApiRequest
import com.yusufcancakmak.refleshdogapp.api.BASE_URL
import com.yusufcancakmak.refleshdogapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private lateinit var binding: ActivityMainBinding
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        backgroundAnimation()
        makeApiRequest()

        binding.floatingActionButton.setOnClickListener {

            binding.floatingActionButton.animate().apply {
                rotationBy(360f)
                duration = 1000
            }.start()

            makeApiRequest()
            binding.ivRandomDog.visibility = View.GONE

        }
    }
    private fun backgroundAnimation(){
        val animationDrawable:AnimationDrawable = binding.rlLayout.background as AnimationDrawable

        animationDrawable.apply {
            setEnterFadeDuration(1000)
            setExitFadeDuration(3000)
            start()
        }
    }
    private fun makeApiRequest(){
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiRequest::class.java)

        GlobalScope.launch(Dispatchers.IO){
            try {
                val response = api.getRandomDog()
                Log.d("Main","Size: ${response.fileSizeBytes}")

                if (response.fileSizeBytes<400_000){
                    withContext(Dispatchers.Main){
                        binding.ProgressBar.visibility=View.VISIBLE
                        Glide.with(applicationContext).load(response.url).into(binding.ivRandomDog)
                        binding.ivRandomDog.visibility=View.VISIBLE
                    }
                }else{
                    makeApiRequest()
                }
            }catch (e:Exception){
                Log.e("Main","${e.message}")

            }
        }
    }
}