package com.vide.todo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.InflateException
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_list.view.*
import kotlinx.android.synthetic.main.activity_list.view.itemName
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.zip.Inflater
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.typeOf


class MainActivity : AppCompatActivity() {

    var accessCode =123
    var imageAccessCode =456
    var cameraAccessCode =789
    var picturePath:Uri? = null
    lateinit var listofItems:ArrayList<Item>
    var adapter:ItemAdapter? =null
    val manager =supportFragmentManager
    var selectedItem:Boolean? = null
    var currentPhotoPath: String? =null
    var photoUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //load items
        listofItems=ArrayList<Item>()
        adapter =ItemAdapter(this, listofItems)
        viewList.adapter = adapter
////        showFragment()

    }

    inner class ItemAdapter : BaseAdapter{
        var listofItems:ArrayList<Item>? =null
        var context:Context? =null
        constructor(context: Context,listofItem :ArrayList<Item>?):super(){
            this.listofItems = listofItem
            this.context =context
        }
        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var item = listofItems!![p0]
            var inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView = inflator.inflate(R.layout.activity_list,null)
//            var myView = LayoutInflater.from(context).inflate(R.layout.activity_list, null)
            myView.itemName.text = item.itemDes
            return myView
        }

        override fun getItem(p0: Int): Any {
            return listofItems!![p0]

        }

        override fun getItemId(p0: Int): Long {
            return 0

        }

        override fun getCount(): Int {
            return listofItems!!.size
        }

    }
    fun textbox(view:View){
        var fragment:Fragment?=null

        val transaction= manager.beginTransaction()
        fragment = FragmentDel()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun addItem(view:View){
        println("AddItem function $picturePath")
        if(picturePath!=null){
            listofItems.add(Item(addItemTxt.text.toString(),false,picturePath!!))
            picturePath = null

            // write into the file
        }
        else{
            listofItems.add(Item(addItemTxt.text.toString(),false,null))

            // write  into the file
        }


        testImage.setImageResource(android.R.color.transparent)
        adapter!!.notifyDataSetChanged()
        addItemTxt.text.clear()
    }

    override fun onResume() {
        super.onResume()
        viewList.setOnItemLongClickListener(){ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->

            listofItems[i].selected = true
            println("index number $i and viewlist ${listofItems[i].itemDes}")
            view1.background =ContextCompat.getDrawable(applicationContext,R.drawable.background)
            println("i am done")
            true
        }

        viewList.setOnItemClickListener(){ adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->

            listofItems[i].selected = false
            viewList[i].background =ContextCompat.getDrawable(applicationContext,R.drawable.whitebackground)
            showFragment(view1.itemName.text.toString(),listofItems[i].itemImage)
        }


    }

    fun showFragment(itemname :String,itemimage:Uri?){
        try{
            var fragment:Fragment?=null
            println("yeie $itemname")
            val transaction= manager.beginTransaction()
            if(itemimage == null){
                fragment = FragmentNoimage(itemname)
               // itemimage=null
            }
            else {
                fragment = FragmentItem(itemname,itemimage)
            }

            transaction.replace(R.id.fragment_holder,fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        catch (ex:Exception){
            println("Hello")
        }

    }

    fun del(view:View){
        for (items in listofItems){
            if(items.selected==true){
                println(" inside if ${items.itemDes} and size${listofItems.size} ")
                try{
                    listofItems.remove(items)
                    println(" After if ${items.itemDes} and size${listofItems.size} ")

                }
                catch (ex:Exception){
                    println("${ex.message}")
                }


            }

        }
        adapter!!.notifyDataSetChanged()
        var fragment:Fragment?=null

        val transaction= manager.beginTransaction()
        fragment = FragmentDel()
        transaction.replace(R.id.fragment_holder,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
//

    }

    fun gallaryImage(view:View){

        checkPermission()
    }

    fun cameraImage(view: View){
//        cameracheckPermission()

        var photoFile:File? = null
        try{
            photoFile=createImageFile()
        }catch (ex:Exception){
            println("Error occured while opening file")
        }
        if(photoFile!=null){
            photoUri = FileProvider.getUriForFile(this,"com.vide.todo.provider",photoFile)
            val cameraintent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
            startActivityForResult(cameraintent,1)
            println("cameraImage funtion photoUri : $photoUri   photfile : $photoFile")
        }
    }

    fun loadImage(){

        var intent = Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, imageAccessCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==imageAccessCode  && data!=null && resultCode == RESULT_OK){
            val selectedImage = data.data
            val cursor = contentResolver.query(selectedImage!!,null,null,null,null)
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            var selectedfilename = cursor.getString(columnIndex)
            cursor.close()
//          testImage.setImageBitmap(BitmapFactory.decodeFile(picturePath))
            testImage.setImageURI(selectedImage)
//            addItemTxt.text.append(selectedfilename)
            println("image has been added $selectedfilename")
            picturePath = selectedImage
            Toast.makeText(this," \b $selectedfilename image has been added successfully",Toast.LENGTH_LONG).show()

                        }
        else if(requestCode ==1 ){


//            val imageBitmap = data.extras!!.get("data") as Bitmap
//            testImage.setImageBitmap(imageBitmap)
//            var selectedImage = intent.extras!!.get("photoURI") as Uri

            testImage.setImageURI(photoUri)
            picturePath = photoUri
            println("on activty result- cameraaccesscode $picturePath pphotouri :$photoUri")
            Toast.makeText(this," \b  Image has been added successfully",Toast.LENGTH_LONG).show()

        }
}

    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){


            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    Toast.makeText(this,"We need to access the gallery to upload the images",Toast.LENGTH_LONG).show()
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        accessCode)

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        accessCode)

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            } else {
                // Permission has already been granted
               loadImage()
            }
        }

    }



    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            accessCode-> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    loadImage()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,"WE can not get accessed to the gallary",Toast.LENGTH_LONG).show()
                }
                return
            }



            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }



    fun createImageFile(): File {
        // Create an image file name

        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        println("createImage current photopath $currentPhotoPath $storageDir")
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            println("createImage current photopath $currentPhotoPath $storageDir")
        }
    }



}
