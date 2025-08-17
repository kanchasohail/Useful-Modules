package com.example.admobintegration

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

object AdmobIntegration : MyAdmob {
    private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-8168519756955207/3977321100"
    private var mInterstitialAd: InterstitialAd? = null

    override fun loadInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, INTERSTITIAL_AD_UNIT_ID, adRequest, object :
            InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                super.onAdFailedToLoad(adError)
                adError.toString().let { Log.d(ContentValues.TAG, it) }
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                super.onAdLoaded(interstitialAd)
                Log.d(ContentValues.TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })
    }

    override fun showInterstitialAd(context: Context, onDone: () -> Unit) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        Log.d(ContentValues.TAG, "Ad was clicked.")
                        onDone()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                        mInterstitialAd = null
                        onDone()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                        mInterstitialAd = null
                        onDone()
                    }

                    override fun onAdImpression() {
                        Log.d(ContentValues.TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                    }
                }
            mInterstitialAd!!.show(context as Activity)
        } else {
            onDone()
        }
    }
}