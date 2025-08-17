package com.example.admobintegration

import android.content.Context

internal interface MyAdmob {

    fun loadInterstitialAd(context: Context)

    fun showInterstitialAd(context: Context, onDone: () -> Unit)

}