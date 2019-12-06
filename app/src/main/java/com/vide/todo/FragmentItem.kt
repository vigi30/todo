package com.vide.todo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_items_selected.*

class FragmentItem(var itemnames:String,var itemimage:Uri?):Fragment() {

//    var itemnames:String? =null
//    constructor(itemname:String):super(){
//        this.itemnames =itemname
//
//
//}
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        Log.d("Tag","Attach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    println("frag $itemnames")
        Log.d("Tag","oncreate")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Tag","onCreateView")

        println(" image")
        return inflater.inflate(R.layout.activity_items_selected,container,false)


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        selectedText.text= itemnames
//        selectedImage.setImageBitmap(BitmapFactory.decodeFile(itemimage))
        selectedImage.setImageURI(itemimage)
        Log.d("Tag","onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Tag","onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Tag","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("Tag","onStop")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Tag","onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("Tag","onDestroy")
    }

    override fun onDetach() {
            super.onDetach()
        Log.d("Tag","onDetach")
    }
}