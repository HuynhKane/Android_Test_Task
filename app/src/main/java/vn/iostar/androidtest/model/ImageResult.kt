package vn.iostar.androidtest.model
import kotlinx.parcelize.Parcelize
import android.os.Parcel
import android.os.Parcelable
@Parcelize
data class ImageResult(
    val title: String,
    val imageUrl: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val thumbnailUrl: String,
    val thumbnailWidth: Int,
    val thumbnailHeight: Int,
    val source: String,
    val domain: String,
    val link: String,
    val googleUrl: String,
    val position: Int
) : Parcelable