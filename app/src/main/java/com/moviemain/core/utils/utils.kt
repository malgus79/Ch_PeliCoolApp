package com.moviemain.core.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.net.Uri
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy.ALL
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.moviemain.R
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter
import jp.wasabeef.recyclerview.animators.LandingAnimator

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun <T : View> T.showIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        show()
    } else {
        hide()
    }
}

inline fun <T : View> T.hideIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        hide()
    } else {
        show()
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun loadImage(context: Context, url: String, imageView: ImageView) {
    Glide.with(context)
        .load(url)
        .transition(withCrossFade())
        .diskCacheStrategy(ALL)
        .error(R.drawable.gradient)
        .centerCrop()
        .into(imageView)
}

fun View.goToHomepage(homepage: String) {
    val btnHomepage = this
    val btnHomepageAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)
    btnHomepage.setOnClickListener {
        btnHomepage.startAnimation(btnHomepageAnim)
        try {
            startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(homepage)), null)
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

fun View.goToWatchTrailer(key: String) {
    val btnWatchTrailer = this
    val btnHomepageAnim = AnimationUtils.loadAnimation(context, R.anim.btn_anim)

    btnWatchTrailer.setOnClickListener {
        btnWatchTrailer.startAnimation(btnHomepageAnim)
        try {
            startActivity(context, Intent(Intent.ACTION_VIEW, Uri.parse(key)), null)
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

fun TextView.setTextColorCompat(@ColorRes color: Int) {
    setTextColor(ContextCompat.getColor(context, color))
}

fun View.setBackgroundColorCompat(@ColorRes color: Int) {
    setBackgroundColor(ContextCompat.getColor(context, color))
}

fun Resources.isLandscapeOrientation(): Boolean {
    return this.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

fun hideElements(vararg elements: View) {
    for (element in elements) {
        element.hide()
    }
}

fun showElements(vararg elements: View) {
    for (element in elements) {
        element.show()
    }
}

fun SwipeRefreshLayout.hideRefresh() {
    isRefreshing = false
}

fun View.setRetryAction(delay: Long = 300, action: () -> Unit): AlphaAnimation {
    val anim = AlphaAnimation(1.0f, 0.5f)
    anim.duration = 100
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = 1
    this.setOnClickListener {
        this.startAnimation(anim)
        Handler().postDelayed({
            action()
        }, delay)
    }
    return anim
}

fun TextView.disableClick() {
    isClickable = false
}

fun TextView.enableClick() {
    isClickable = true
}

fun RecyclerView.setupRecyclerView(adapter: RecyclerView.Adapter<*>, columns: Int, itemAnimator: RecyclerView.ItemAnimator,vertical: Boolean) {
    this.apply {
        this.adapter = ScaleInAnimationAdapter(adapter)
        this.itemAnimator = itemAnimator.apply { addDuration = 500 }
        this.layoutManager = StaggeredGridLayoutManager(
            columns,
            if (vertical) StaggeredGridLayoutManager.VERTICAL else StaggeredGridLayoutManager.HORIZONTAL
        )
        setHasFixedSize(true)
        show()
    }
}

fun Activity.showConnectivitySnackbar(isConnected: Boolean) {
    val binding = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    val messageRes = if (isConnected) R.string.connection_restored else R.string.no_connection
    val duration = if (isConnected) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_INDEFINITE
    val backgroundColorRes = if (isConnected) R.color.green_dark_theme else R.color.red_theme
    val snackbar = Snackbar.make(findViewById(android.R.id.content), messageRes, duration)
        .setAnchorView(binding)
        .setBackgroundTint(ContextCompat.getColor(this, backgroundColorRes))
    if (!isConnected) {
        snackbar.setAction(getString(R.string.rule_out)) {}
    }
    snackbar.show()
}