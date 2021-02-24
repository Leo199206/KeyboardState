package com.jlertele.keyboard

/**
 * <pre>
 *     author : leo
 *     time   : 2020/05/15
 *     desc   : 键盘状态监听
 * </pre>
 */
interface KeyboardStateListener {
    /**
     * 键盘切换到显示状态
     * @param keyboardHeight Int 键盘高度
     */
    fun onKeyboardShow(keyboardHeight: Int)

    /**
     * 键盘切换到隐藏状态
     * @param keyboardHeight Int
     */
    fun onKeyboardHide(keyboardHeight: Int)
}