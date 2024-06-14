package vn.iostar.androidtest.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.button.MaterialButton
import vn.iostar.androidtest.R
import vn.iostar.androidtest.adapter.FullScreenImageAdapter
import vn.iostar.androidtest.model.ImageResult

class FullScreenImageActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var openSrcButton: MaterialButton
    private lateinit var images: ArrayList<ImageResult>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTransition()
        setContentView(R.layout.activity_image_full_screen)
        setupViewPager()
        setupOpenSrcButton()

    }

    private fun setupImageView(position: Int) {
        supportPostponeEnterTransition()
        val imageUrl = images[position].imageUrl
        val recyclerView = viewPager[0] as RecyclerView
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as FullScreenImageAdapter.ViewHolder
        val imageView = viewHolder.imageView
        imageView?.let {
            it.transitionName = "sharedImage"
            Glide.with(this)
                .load(imageUrl)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        supportStartPostponedEnterTransition()
                        return false
                    }
                })
                .into(it)
        }
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.viewPager)
        images = intent.getParcelableArrayListExtra<ImageResult>("images") ?: arrayListOf()
        val position = intent.getIntExtra("position", 0)
        val adapter = FullScreenImageAdapter(images)
        viewPager.apply {
            this.adapter = adapter
            setCurrentItem(position, false)
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setupImageView(position)
                }
            })
        }
    }

    private fun setupOpenSrcButton() {
        openSrcButton = findViewById(R.id.openSourceButton)
        openSrcButton.setOnClickListener {
            openUrlInCustomTab(images[viewPager.currentItem].link)
        }
    }

    private fun setupTransition() {
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        window.sharedElementEnterTransition =
            TransitionInflater.from(this).inflateTransition(R.transition.shared_image)
    }

    private fun openUrlInCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}