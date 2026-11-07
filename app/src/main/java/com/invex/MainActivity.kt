package com.invex

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.invex.supplement.SupplementSheetRoute
import com.invex.ui.theme.InvexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvexTheme {
                SupplementSheetRoute(
                    onBack = {
                        Toast.makeText(this@MainActivity, "Returning to Teacher Home", Toast.LENGTH_SHORT).show()
                    },
                )
            }
        }
    }
}
