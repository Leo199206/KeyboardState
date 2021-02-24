package com.jlertele.keyboard

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference

/**
 * <pre>
 * author : leo
 * time   : 2019/09/23
 * desc   : 键盘状态监听工具类
 * </pre>
 * @property activityRef Activity软引用
 * @property rootViewRef rootView软引用
 * @property rootViewCurrentHeight  rootView当前显示高度
 * @property stateListener 键盘状态会回调
 * @property keyboardHeight 键盘高度（大概值，不一定等于200）
 */
class KeyboardHelper internal constructor(
    activity: ComponentActivity,
    private var stateListener: KeyboardStateListener
) : ViewTreeObserver.OnGlobalLayoutListener, LifecycleObserver {
    private var activityRef: WeakReference<Activity>? = WeakReference(activity)
    private var rootViewRef: WeakReference<View>? = null
    private var rootViewCurrentHeight: Int = 0
    private var keyboardHeight = 200


    init {
        bindLifecycle(activity)
        addGlobalLayoutListener()
    }


    /**
     * 添加Activity生命周期监听，对资源进行释放，避免内存泄露
     */
    private fun bindLifecycle(activity: ComponentActivity) {
        activity.lifecycle.addObserver(this)
    }


    /**
     * 添加Layout布局变动监听
     */
    private fun addGlobalLayoutListener() {
        if (activityRef?.get() == null) {
            return
        }
        if (activityRef?.get()!!.window == null) {
            return
        }
        rootViewRef = WeakReference(activityRef?.get()!!.window.decorView)
        if (rootViewRef?.get() != null) {
            rootViewRef?.get()?.viewTreeObserver?.addOnGlobalLayoutListener(this)
        }
    }


    /**
     * RootView 布局变动监听
     */
    override fun onGlobalLayout() {
        //获取当前根视图在屏幕上显示的大小
        val visibleHeight = getRootViewVisibleHeight()
        if (rootViewCurrentHeight == 0) {
            rootViewCurrentHeight = visibleHeight
            return
        }
        //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
        if (rootViewCurrentHeight == visibleHeight) {
            return
        }
        //根视图显示高度变小超过200，可以看作软键盘显示了
        if (rootViewCurrentHeight - visibleHeight > keyboardHeight) {
            onKeyBoardShow(visibleHeight)
            return
        }
        //根视图显示高度变大超过200，可以看作软键盘隐藏了
        if (visibleHeight - rootViewCurrentHeight > keyboardHeight) {
            onKeyBoardHide(visibleHeight)
            return
        }
    }


    /**
     * RootView 已显示高度
     * @return
     */
    private fun getRootViewVisibleHeight(): Int {
        val rect = Rect()
        if (rootViewRef != null) {
            rootViewRef!!.get()?.getWindowVisibleDisplayFrame(rect)
        }
        return rect.height()
    }


    /**
     * 键盘显示
     * @param visibleHeight Int
     */
    private fun onKeyBoardShow(visibleHeight: Int) {
        var keyboardHeight = rootViewCurrentHeight - visibleHeight
        stateListener.onKeyboardShow(keyboardHeight)
        rootViewCurrentHeight = visibleHeight
    }

    /**
     * 键盘隐藏
     * @param visibleHeight Int
     */
    private fun onKeyBoardHide(visibleHeight: Int) {
        var keyboardHeight = visibleHeight - rootViewCurrentHeight
        stateListener.onKeyboardHide(keyboardHeight)
        rootViewCurrentHeight = visibleHeight
    }

    /**
     * 资源释放
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun release() {
        if (activityRef != null) {
            activityRef!!.clear()
            activityRef = null
        }
        if (rootViewRef != null) {
            rootViewRef!!.clear()
            rootViewRef = null
        }
    }
}


/**
 * 键盘状态监听
 * @param keyboardStateListener 键盘状态回调
 * @return
 */
fun ComponentActivity.addKeyboardListener(
    keyboardStateListener: KeyboardStateListener
): KeyboardHelper {
    return KeyboardHelper(activity = this, stateListener = keyboardStateListener)
}


/**
 * 获取 InputMethodManager
 * @param context Context
 * @return InputMethodManager
 */
private fun getInputMethodManager(context: Context): InputMethodManager {
    return context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
}


/**
 * 显示键盘
 * @param view
 */
fun showKeyboard(view: View) {
    getInputMethodManager(view.context).run {
        showSoftInput(view, 0)
    }
}


/**
 * 关闭软键盘
 *
 * @param context
 */
fun hideKeyboard(context: Context) {
    context
        .takeIf {
            context is Activity
        }
        ?.let {
            it as Activity
        }
        ?.takeIf {
            it.currentFocus != null
        }
        .also {
            hideKeyboard(it!!.currentFocus!!)
        }
}

/**
 * 关闭软键盘
 * @param view
 */
fun hideKeyboard(view: View) {
    view.windowToken
        ?.let {
            getInputMethodManager(view.context)
        }?.run {
            hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
}