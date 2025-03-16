package com.sohail.connectivityobserver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    private lateinit var connectivityObserver: ConnectivityObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        connectivityObserver = NetworkConnectivityObserver(applicationContext)

        //For non compose apps
        /**
        connectivityObserver.observe().onEach {
        println("Status is $it")
        }.launchIn(lifecycleScope)
         **/
        setContent {
            // A surface container using the 'background' color from the theme
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                val status by connectivityObserver.observe()
                    .collectAsState(initial = ConnectivityObserver.Status.Unavailable)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Network status: $status")
                }
            }
        }
    }
}