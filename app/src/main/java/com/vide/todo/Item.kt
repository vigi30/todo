package com.vide.todo

import android.net.Uri

class Item {
    var itemDes:String? =null
    var selected:Boolean?  = null
    var itemImage:Uri? =null
    constructor(desc:String,selected:Boolean,img:Uri?){
        this.itemDes = desc
        this.selected = selected
        this.itemImage = img
    }
}