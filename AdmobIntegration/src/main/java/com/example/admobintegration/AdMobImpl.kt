package com.example.admobintegration

import android.Manifest
import android.content.Context
import androidx.annotation.RequiresPermission
import com.google.android.gms.ads.MobileAds

object AdMobImpl {

    fun initialiseAds(context: Context) {
        MobileAds.initialize(context)
    }

    fun loadInterstitialAd(context: Context) {
        AdmobIntegration.loadInterstitialAd(context)
    }

    fun showInterstitialAd(context: Context, onAdDismissed: () -> Unit) {
        AdmobIntegration.showInterstitialAd(context) {
            onAdDismissed()
        }
    }

}