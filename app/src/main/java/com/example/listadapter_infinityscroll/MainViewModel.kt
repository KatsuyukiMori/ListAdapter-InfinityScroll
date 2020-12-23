package com.example.listadapter_infinityscroll

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _list = MutableLiveData<ArrayList<ItemCard>>()
    val list = _list as LiveData<ArrayList<ItemCard>>

    var page = 1

    companion object {
        const val LIMIT = 10
        private val TAG = this.javaClass.name + " mori"
    }

    fun init() {
        val mutableList = mutableListOf(ItemCard(0, "A"), ItemCard(1, "B"), ItemCard(2, "C"))
        _list.value = mutableList as ArrayList<ItemCard>
        page += 1
        Log.d(TAG, "list count: ${list.value?.size}")
    }

    fun fetchList() {
        Log.d(TAG, "fetchList, page: $page")
        val mutableList = when (page) {
            2 -> mutableListOf(ItemCard(3, "D"), ItemCard(4, "E"))
            3 -> mutableListOf(ItemCard(5, "F"), ItemCard(6, "G"), ItemCard(7, "H"))
            4 -> mutableListOf(ItemCard(8, "I"), ItemCard(9, "J"))
            else -> emptyList()
        }
        val tmp = _list.value!!.apply {
            addAll(mutableList as ArrayList<ItemCard>)
        }
        _list.value = tmp
        page += 1
        Log.d(TAG, "list count: ${list.value?.size}")
    }
}