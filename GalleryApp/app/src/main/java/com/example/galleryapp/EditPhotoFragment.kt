package com.example.galleryapp

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import androidx.fragment.app.Fragment


class EditPhotoFragment : Fragment() {
    private lateinit var imageView2 : ImageView
    private lateinit var ratingBar2 : RatingBar
    private lateinit var descInput : EditText
    private lateinit var button : Button
    private lateinit var intent: Intent
    private lateinit var item :PhotoItem
    private lateinit var parentActivity: EditPhotoActivity
    private var container: ViewGroup? = null
    private var inflater: LayoutInflater? = null


    fun setParams(intent: Intent, parentActivity: EditPhotoActivity){
        item = intent.getParcelableExtra("item")!!
        this.parentActivity = parentActivity
        this.intent = intent
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.container = container
        this.inflater = inflater

        return initUI()
    }

    private fun initUI(): View {
        val orientation = resources.configuration.orientation
        val layoutFile = if (orientation == Configuration.ORIENTATION_LANDSCAPE)
            R.layout.fragment_edit_photo_land
        else
            R.layout.fragment_edit_photo

        container?.removeAllViewsInLayout()

        val view = inflater!!.inflate(layoutFile, container, false)
        imageView2 = view.findViewById(R.id.imageView2)
        ratingBar2 = view.findViewById(R.id.ratingBar2)
        descInput = view.findViewById(R.id.descInput)
        button = view.findViewById(R.id.button)
        return view
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val saveDesc = descInput.text.trim().toString()
        val saveRates = ratingBar2.rating

        val view = initUI()
        container?.addView(view)


        descInput.setText(saveDesc)
        ratingBar2.rating = saveRates
        val resId = resources.getIdentifier(item.src, "drawable", context?.packageName)
        imageView2.setImageResource(resId)
        button.setOnClickListener{
            listener()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val resId = resources.getIdentifier(item.src, "drawable", context?.packageName)
        imageView2.setImageResource(resId)
        ratingBar2.rating = item.rates
        descInput.setText(item.description)
        button.setOnClickListener{
            listener()
        }
    }

    private fun listener(){
        item.description = descInput.text.trim().toString()
        item.rates = ratingBar2.rating
        intent.putExtra("return item", item)
        parentActivity.sendIntentBack(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(intent: Intent, editPhotoActivity: EditPhotoActivity) =
            EditPhotoFragment().apply { setParams(intent, editPhotoActivity)}
    }
}