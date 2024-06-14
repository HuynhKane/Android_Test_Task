package vn.iostar.androidtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.iostar.androidtest.R
import vn.iostar.androidtest.model.ImageResult

class FullScreenImageAdapter(private val images: List<ImageResult>) : RecyclerView.Adapter<FullScreenImageAdapter.ViewHolder>() {

        class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val imageView:ImageView = itemView.findViewById(R.id.fullScreenImageView)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.full_screen_image_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = images[position].imageUrl
        holder.imageView.transitionName = "sharedImage"
        Glide.with(holder.itemView.context).load(images[position].imageUrl).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return images.count()
    }
}