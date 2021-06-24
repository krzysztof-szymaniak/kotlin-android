package com.example.galleryapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private lateinit var fragment : ItemListFragment
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            fragment.receiveIntentfromActivity(result.data)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            fragment = ItemListFragment.newInstance()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root_layout, fragment)
                    .commit()

        }
        else {
            fragment = supportFragmentManager.getFragment(savedInstanceState, "myfragment") as ItemListFragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, "myfragment", fragment)
    }

    fun startEditActivity(item: PhotoItem, pos: Int) {
        val intent = Intent(this, EditPhotoActivity::class.java)
        intent.putExtra("item", item)
        intent.putExtra("pos", pos)
        resultLauncher.launch(intent)
    }

}