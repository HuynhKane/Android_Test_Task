package vn.iostar.androidtest.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import vn.iostar.androidtest.R
import vn.iostar.androidtest.adapter.ImageViewAdapter
import vn.iostar.androidtest.model.HomeFeed
import vn.iostar.androidtest.model.ImageResult
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    lateinit var image_rs : RecyclerView
    lateinit var imageViewAdapter: ImageViewAdapter
    lateinit var image_list:List<ImageResult>
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 2
    private val client = OkHttpClient.Builder().build()
    private val apiKey = "f834276ff3c27f2b75d3c7db94345948f1e4f350"
    private val url = "https://google.serper.dev/images"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchField = findViewById<EditText>(R.id.search_field)
        val searchButton = findViewById<ImageButton>(R.id.search_button)

        image_rs = findViewById(R.id.image_list)

        image_list = ArrayList()

        val layoutManager = GridLayoutManager(this,2)

        image_rs.layoutManager = layoutManager

        image_rs.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading && !isLastPage) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        currentPage++
                        search(searchField) // Call search function again to fetch more images
                    }
                }
            }
        })


        searchButton.setOnClickListener {

            if (searchField.text.toString().isNotEmpty()) {
                currentPage = 1
                search(searchField)
            } else {

                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun search(searchField: EditText) {
        val queryString = searchField.text.toString()
        val body = createJsonBody(queryString, currentPage)
        val request = createRequest(body)

        client.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = GsonBuilder().create()
                val homeFeed = gson.fromJson(body, HomeFeed::class.java)
                runOnUiThread{
                    if (currentPage == 1 || image_rs.adapter == null) {
                        image_rs.adapter = ImageViewAdapter(homeFeed)
                    } else {
                        (image_rs.adapter as ImageViewAdapter).addImages(homeFeed.images)
                    }
                }
            }
        })
    }

    private fun createRequest(body: RequestBody): Request {
        return Request.Builder()
            .url(url)
            .post(body)
            .addHeader("X-API-KEY", apiKey)
            .addHeader("Content-Type", "application/json")
            .build()
    }

    private fun createJsonBody(queryString: String, page: Int) : RequestBody{

        val jsonBody = "{\"q\":\"$queryString\",\"num\":100, \"page\":$currentPage}"
        val mediaType = "application/json".toMediaType()
        return RequestBody.create(mediaType, jsonBody)

    }
}



