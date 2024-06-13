package com.example.donotlate.feature.chatroom.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.donotlate.R
import com.example.donotlate.databinding.ActivityChatRoomBinding

class ChatRoomActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityChatRoomBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setFragment(ChatRoomFragment())

    }

    fun setFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.chat_frame, fragment).commit()
    }
}
