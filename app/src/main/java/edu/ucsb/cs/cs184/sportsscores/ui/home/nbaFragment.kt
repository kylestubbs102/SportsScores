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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.ucsb.cs.cs184.sportsscores.GameAdapter
import edu.ucsb.cs.cs184.sportsscores.R
import org.jsoup.Jsoup
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class nbaFragment : Fragment() {

    private lateinit var viewModel: nbaViewModel

    var imageURLArrayList = ArrayList<Pair<String, String>>()
    var teamNameArrayList = ArrayList<Pair<String, String>>()
    var scoresArrayList = ArrayList<Pair<String, String>>()
    var timeArrayList = ArrayList<String>()

    private lateinit var gameAdapter: GameAdapter
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
                ViewModelProvider(this).get(nbaViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_nba, container, false)

        listRecyclerView = root.findViewById(R.id.listRecyclerView)
        linearLayoutManager = LinearLayoutManager(context)
        listRecyclerView.layoutManager = linearLayoutManager

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
            val scoresElements = doc.getElementsByAttributeValueContaining("class", "EventCard__score--")
            val timeElements = doc.getElementsByAttributeValueContaining("class", "clockColumn")
            for (index in 0 until imageElements.size / 2) {
                val homeTeamIndex = index * 2
                val awayTeamIndex = index * 2 + 1
                val homeTeamImageURL = imageElements[homeTeamIndex].child(0).child(0).absUrl("src")
                val awayTeamImageURL = imageElements[awayTeamIndex].child(0).child(0).absUrl("src")
                imageURLArrayList.add(Pair(homeTeamImageURL, awayTeamImageURL))
                val homeTeamName = teamNameElements[homeTeamIndex].text()
                val awayTeamName = teamNameElements[awayTeamIndex].text()
                teamNameArrayList.add(Pair(homeTeamName, awayTeamName))
                val homeTeamScore = scoresElements[homeTeamIndex].text()
                val awayTeamScore = scoresElements[awayTeamIndex].text()
                scoresArrayList.add(Pair(homeTeamScore, awayTeamScore))
                timeArrayList.add(timeElements[index].text())
            } // create in ViewModel and use boolean to only initialize on startup


            activity?.runOnUiThread {
//                textView.text = teamNameArrayList[0]
//                Picasso.get().load(imageURLArrayList[0]).into(imageView)
            }

        }
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //gameAdapter = GameAdapter(games)
        //change to viewModel later
    }
}