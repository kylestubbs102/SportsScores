package edu.ucsb.cs.cs184.sportsscores.ui.home

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import edu.ucsb.cs.cs184.sportsscores.Game
import edu.ucsb.cs.cs184.sportsscores.GameAdapter
import edu.ucsb.cs.cs184.sportsscores.R
import org.jsoup.Jsoup
import org.w3c.dom.Text
import java.io.*
import java.sql.Time
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.OffsetTime
import java.time.Year
import java.util.*
import kotlin.concurrent.thread

class nbaFragment : Fragment() {

    private lateinit var viewModel: nbaViewModel

//    var imageURLArrayList = ArrayList<Pair<String, String>>()
//    var teamNameArrayList = ArrayList<Pair<String, String>>()
//    var scoresArrayList = ArrayList<Pair<String, String>>()
//    var timeArrayList = ArrayList<String>()

    private lateinit var titleTextView: TextView
    private lateinit var shimmerView: ShimmerFrameLayout

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
        shimmerView = root.findViewById(R.id.shimmerView)
        titleTextView = root.findViewById(R.id.dateTextView)
        listRecyclerView = root.findViewById(R.id.listRecyclerView)

        return root
    }

    private fun retrieveWebInfo() {
        shimmerView.visibility = View.VISIBLE
        shimmerView.startShimmer()
        thread {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH) + 1
            val yearURL = String.format("%04d", year)
            val dayURL = String.format("%02d", day)
            val monthURL = String.format("%02d", month)
            titleTextView.text = "Date: $month/$day/$year,"

            val date = "$yearURL-$monthURL-$dayURL"

            val url = "https://www.thescore.com/nba/events/date/$date"
            val doc = Jsoup.connect(url).get()

            //val imageElements = doc.select("a[name$=nba:scoreboard:team]")
            val imageElements = doc.getElementsByAttributeValueContaining("class", "teamLogo")
            val teamNameElements = doc.getElementsByAttributeValueContaining("class", "teamName")
            val scoresElements = doc.getElementsByAttributeValueContaining("class", "EventCard__score--")
            val preGameScoresElements = doc.getElementsByAttributeValueContaining("class", "EventCard__pregameScore")
            val timeElements = doc.getElementsByAttributeValueContaining("class", "clockColumn")

            titleTextView.text = "Date: $month/$day/$year, ${timeElements.size} games"

//            val sdf = SimpleTimeZone.getDefault()
//            val testTime = timeElements[0].text()
//            OffsetTime.of(testTime, sdf)
            //sdf.
            //

            //val time = Time()

            for (element in preGameScoresElements) {
                scoresElements.add(element)
            }

            viewModel.clearGames()
            for (index in 0 until imageElements.size / 2) {
                val homeTeamIndex = index * 2
                val awayTeamIndex = index * 2 + 1
                val homeTeamImageURL = imageElements[homeTeamIndex].child(0).child(0).absUrl("src")
                val awayTeamImageURL = imageElements[awayTeamIndex].child(0).child(0).absUrl("src")
                val imageURLPair = Pair(homeTeamImageURL, awayTeamImageURL)
                val homeTeamName = teamNameElements[homeTeamIndex].text()
                val awayTeamName = teamNameElements[awayTeamIndex].text()
                val teamNamePair = Pair(homeTeamName, awayTeamName)

                val time = timeElements[index].text()

                val homeTeamScore: String
                val awayTeamScore: String
                homeTeamScore = scoresElements[homeTeamIndex].text()
                awayTeamScore = scoresElements[awayTeamIndex].text()
                val teamScorePair = Pair(homeTeamScore, awayTeamScore)
                val game = Game(imageURLPair, teamNamePair, teamScorePair, time)
                viewModel.addGame(game)
            }

            activity?.runOnUiThread {
                gameAdapter = GameAdapter(requireContext(), viewModel.getGames()!!)
                linearLayoutManager = LinearLayoutManager(context)
                //add gridlayout here too when created

                listRecyclerView.layoutManager = linearLayoutManager
                listRecyclerView.adapter = gameAdapter
                listRecyclerView.setHasFixedSize(true)
                shimmerView.visibility = View.GONE
                shimmerView.stopShimmer()
            }

        }
}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retrieveWebInfo()
    }
}