package com.vide.todo

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_items_selected.*
import kotlinx.android.synthetic.main.activity_items_selected_noimage.*

class FragmentNoimage(var itemnames:String): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("No image")
        return inflater.inflate(R.layout.activity_items_selected_noimage,container,false)
    }

    override fun onStart() {
        super.onStart()

        selectedText1.text= itemnames

//        selectedImage.setImageResource(android.R.color.transparent)

    }
}
