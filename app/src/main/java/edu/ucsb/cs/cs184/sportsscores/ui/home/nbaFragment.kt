package edu.ucsb.cs.cs184.sportsscores.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.ucsb.cs.cs184.sportsscores.Game
import edu.ucsb.cs.cs184.sportsscores.GameAdapter
import edu.ucsb.cs.cs184.sportsscores.R
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class nbaFragment : Fragment() {

    private lateinit var viewModel: nbaViewModel

//    var imageURLArrayList = ArrayList<Pair<String, String>>()
//    var teamNameArrayList = ArrayList<Pair<String, String>>()
//    var scoresArrayList = ArrayList<Pair<String, String>>()
//    var timeArrayList = ArrayList<String>()

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
//            val test = activity?.filesDir?.absolutePath
//            val file = File(test + "/nba4.txt")
//            val inputStream = FileInputStream(file)
//            val reader = BufferedReader(InputStreamReader(inputStream))
//            val htmlString = reader.readText()
            //val doc = Jsoup.parse

            val calendar = Calendar.getInstance()
            val year = String.format("%04d", calendar.get(Calendar.YEAR))
            val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
            val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)

            val date = "$year-$month-$day"

            val url = "https://www.thescore.com/nba/events/date/$date"
            val doc = Jsoup.connect(url).get()

            //val imageElements = doc.select("a[name$=nba:scoreboard:team]")
            val imageElements = doc.getElementsByAttributeValueContaining("class", "teamLogo")
            val teamNameElements = doc.getElementsByAttributeValueContaining("class", "teamName")
            val scoresElements = doc.getElementsByAttributeValueContaining("class", "EventCard__score--")
            val preGameScoresElements = doc.getElementsByAttributeValueContaining("class", "EventCard__pregameScore")
            val timeElements = doc.getElementsByAttributeValueContaining("class", "clockColumn")
            for (index in 0 until imageElements.size / 2) {
                val homeTeamIndex = index * 2
                val awayTeamIndex = index * 2 + 1
                val homeTeamImageURL = imageElements[homeTeamIndex].child(0).child(0).absUrl("src")
                val awayTeamImageURL = imageElements[awayTeamIndex].child(0).child(0).absUrl("src")
                val imageURLPair = Pair(homeTeamImageURL, awayTeamImageURL)
//                imageURLArrayList.add(Pair(homeTeamImageURL, awayTeamImageURL))
                val homeTeamName = teamNameElements[homeTeamIndex].text()
                val awayTeamName = teamNameElements[awayTeamIndex].text()
                val teamNamePair = Pair(homeTeamName, awayTeamName)
//                teamNameArrayList.add(Pair(homeTeamName, awayTeamName))
                val homeTeamScore: String
                val awayTeamScore: String
                if (scoresElements.size > 0) {
                    homeTeamScore = scoresElements[homeTeamIndex].text()
                    awayTeamScore = scoresElements[awayTeamIndex].text()
                } else {
                    homeTeamScore = preGameScoresElements[homeTeamIndex].text()
                    awayTeamScore = preGameScoresElements[awayTeamIndex].text()
                }
                val teamScorePair = Pair(homeTeamScore, awayTeamScore)
//                scoresArrayList.add(Pair(homeTeamScore, awayTeamScore))
                val time = timeElements[index].text()
//                timeArrayList.add(timeElements[index].text())
                val game = Game(imageURLPair, teamNamePair, teamScorePair, time)
                viewModel.addGame(game)
            } // create in ViewModel and use boolean to only initialize on startup

            activity?.runOnUiThread {
                gameAdapter = GameAdapter(requireContext(), viewModel.getGames()!!)
                linearLayoutManager = LinearLayoutManager(context)
                //add gridlayout here too when created

                listRecyclerView.layoutManager = linearLayoutManager
                listRecyclerView.adapter = gameAdapter
                listRecyclerView.setHasFixedSize(true)
//                textView.text = teamNameArrayList[0]
//                Picasso.get().load(imageURLArrayList[0]).into(imageView)
            }

        }
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retrieveWebInfo()
        //linearLayoutManager = LinearLayoutManager(context)
        //listRecyclerView.layoutManager = linearLayoutManager
        //gameAdapter = GameAdapter(requireContext(), viewModel.getGames()!!)
        //listRecyclerView.adapter = gameAdapter
//        listRecyclerView.setHasFixedSize(true)
    }
}