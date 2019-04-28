package com.example.wikiapicountersearch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val wikiApiServe by lazy {
        WikiApiService.create()
    }
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if(editText.text.toString().isNotEmpty()){
                beginSearch(editText.text.toString())
            }
        }
    }

    private fun beginSearch(srsearch: String) {
        disposable =
            wikiApiServe.hitCountCheck("query", "json", "search", srsearch)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result -> textView.text = "${result.query.searchinfo.totalhits} result found" },
                    { error -> Toast.makeText(this, error.message, Toast.LENGTH_LONG).show() }
                )
    }





    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }
}

