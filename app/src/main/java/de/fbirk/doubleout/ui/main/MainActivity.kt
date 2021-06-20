package de.fbirk.doubleout.ui.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import de.fbirk.doubleout.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {



        return super.onCreateView(name, context, attrs)
    }
}