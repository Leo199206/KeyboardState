package com.keyboard.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jlertele.keyboard.KeyboardStateListener
import com.jlertele.keyboard.addKeyboardListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), KeyboardStateListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addKeyboardListener(this)
    }

    /**
     * 键盘展开回调
     * @param keyboardHeight Int
     */
    override fun onKeyboardShow(keyboardHeight: Int) {
        tv_state.text = "键盘展开:${keyboardHeight}"
    }

    /**
     * 键盘关闭回调
     * @param keyboardHeight Int
     */
    override fun onKeyboardHide(keyboardHeight: Int) {
        tv_state.text = "键盘关闭:${keyboardHeight}"
    }
}