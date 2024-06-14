package vn.iostar.androidtest.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import vn.iostar.androidtest.activity.FullScreenImageActivity
import vn.iostar.androidtest.model.HomeFeed
import vn.iostar.androidtest.R
import vn.iostar.androidtest.model.ImageResult

class ImageViewAdapter(
    private val homeFeed: HomeFeed,
) : RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ImageViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_image,
            parent, false
        )

        return ImageViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image  = homeFeed.images.get(position)
        holder.bind(image)
        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, FullScreenImageActivity::class.java)
            val imageViews = holder.itemView.findViewById<ImageView>(R.id.imageUrl)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                context as Activity,
                imageViews,
                ViewCompat.getTransitionName(imageViews)!!
            )
            intent.putParcelableArrayListExtra("images", ArrayList(homeFeed.images))
            intent.putExtra("position", position)
            context.startActivity(intent, options.toBundle())
        }

    }


    override fun getItemCount(): Int {
        println(homeFeed.images.count())
        return homeFeed.images.count()
    }

    fun addImages(newImages: ArrayList<ImageResult>) {
        val oldSize = homeFeed.images.size
        homeFeed.images.addAll(newImages)
        notifyItemRangeInserted(oldSize, newImages.size)
    }
    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title_image: TextView = itemView.findViewById(R.id.title)
        val url_image: ImageView = itemView.findViewById(R.id.imageUrl)
        val sourceView: TextView = itemView.findViewById(R.id.sourceView)

        fun bind(image: ImageResult) {
            Glide.with(itemView.context)
                .load(image.imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(url_image)

            title_image.text = image.title
            sourceView.text = image.source
            sourceView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(image.link))
                itemView.context.startActivity(intent)
            }

        }
    }
}