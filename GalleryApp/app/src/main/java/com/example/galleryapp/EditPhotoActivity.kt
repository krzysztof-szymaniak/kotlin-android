package com.example.galleryapp

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class EditPhotoActivity : AppCompatActivity() {
    private lateinit var fragment : EditPhotoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photo)
        if (savedInstanceState == null) {
            fragment = EditPhotoFragment.newInstance(intent, this)
            supportFragmentManager
                .beginTransaction()
                .add(R.id.edit_root, fragment)
                .commit()

        }
        else {
            fragment = supportFragmentManager.getFragment(savedInstanceState, "myeditfragment") as EditPhotoFragment
        }
    }
    fun sendIntentBack(intent: Intent){
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "myeditfragment", fragment)
    }
}