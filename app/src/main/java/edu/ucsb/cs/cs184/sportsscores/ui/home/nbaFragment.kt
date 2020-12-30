package edu.ucsb.cs.cs184.sportsscores.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import edu.ucsb.cs.cs184.sportsscores.R
import org.jsoup.Jsoup
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread


class nbaFragment : Fragment() {

    private lateinit var viewModel: nbaViewModel

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(nbaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_nba, container, false)
        imageView = root.findViewById(R.id.image)
        textView = root.findViewById(R.id.text)
        retrieveWebInfo()
        return root
    }

    private fun retrieveWebInfo() {
//        val url = URL("https://www.espn.com/nba/scoreboard/_/date/20201229")
//        val con = url.openConnection() as HttpURLConnection
//        val datas = con.inputStream.bufferedReader().readText()
//        val docParse = Jsoup.parse(datas)
//        val outputStreamWriter = OutputStreamWriter(requireContext().openFileOutput("nba1.txt", Context.MODE_APPEND))
//        outputStreamWriter.write(datas)
//        outputStreamWriter.close()
        val test = activity?.filesDir?.absolutePath
        val file = File(test + "/nba1.txt")
        val inputStream = FileInputStream(file)
        val reader = BufferedReader(InputStreamReader(inputStream))
        val htmlString = reader.readText()
        val doc = Jsoup.parse(htmlString)

        val imageElements = doc.select("img[src*=500]")
//      val imageElements = doc.getElementsByClass("teamlogo")
        val textElements = doc.getElementsByTag("h1")

        val imageUrl = imageElements[0].absUrl("src")

        textView.text = textElements[1].text()
        Picasso.get().load(imageUrl).into(imageView)
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    // might not need
}