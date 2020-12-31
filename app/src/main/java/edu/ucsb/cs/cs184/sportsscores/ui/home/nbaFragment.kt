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

    var imageURLArrayList = ArrayList<String>()
    var teamNameArrayList = ArrayList<String>()

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
        thread {
//            val url = URL("https://www.thescore.com/nba/events/date/2020-12-29")
//            val con = url.openConnection() as HttpURLConnection
//            val datas = con.inputStream.bufferedReader().readText()
//            val docParse = Jsoup.parse(datas)
//            val outputStreamWriter = OutputStreamWriter(requireContext().openFileOutput("nba4.txt", Context.MODE_APPEND))
//            outputStreamWriter.write(datas)
//            outputStreamWriter.close()
            val test = activity?.filesDir?.absolutePath
            val file = File(test + "/nba4.txt")
            val inputStream = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(inputStream))
            val htmlString = reader.readText()
            val doc = Jsoup.parse(htmlString)

            //val imageElements = doc.select("a[name$=nba:scoreboard:team]")
            val imageElements = doc.getElementsByAttributeValueContaining("class", "teamLogo")
            val teamNameElements = doc.getElementsByAttributeValueContaining("class", "teamName")
            for (index in 0 until imageElements.size) {
                imageURLArrayList.add(imageElements[index].child(0).child(0).absUrl("src"))
                teamNameArrayList.add(teamNameElements[index].text())
            } // create in ViewModel and use boolean to only initialize on startup


            activity?.runOnUiThread {
                textView.text = teamNameArrayList[0]
                Picasso.get().load(imageURLArrayList[0]).into(imageView)
            }

            //work on loading teamnames and logos, also work on recyclerview
        }
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    // might not need
}