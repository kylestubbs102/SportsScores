package edu.ucsb.cs.cs184.sportsscores

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class GameAdapter(private var context: Context, private var games: ArrayList<Game>) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var homeTeamLogo: ImageView
        var awayTeamLogo: ImageView
        var homeTeamName: TextView
        var awayTeamName: TextView
        var homeTeamScore: TextView
        var awayTeamScore: TextView
        var timeText: TextView

        init {
            view.setOnClickListener(this) // might not need this
            homeTeamLogo = view.findViewById(R.id.homeTeamLogo)
            awayTeamLogo = view.findViewById(R.id.awayTeamLogo)
            homeTeamName = view.findViewById(R.id.homeTeamName)
            awayTeamName = view.findViewById(R.id.awayTeamName)
            homeTeamScore = view.findViewById(R.id.homeTeamScore)
            awayTeamScore = view.findViewById(R.id.awayTeamScore)
            timeText = view.findViewById(R.id.timeText)
        }

        override fun onClick(v: View?) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setLogos(holder, position)
        setNames(holder, position)
        setScores(holder, position)
        setTime(holder, position)
    }

    private fun setLogos(holder: ViewHolder, position: Int) {
        Picasso.get().load(games[position].logoURLs.first).into(holder.homeTeamLogo)
        Picasso.get().load(games[position].logoURLs.second).into(holder.awayTeamLogo)
    }

    private fun setNames(holder: ViewHolder, position: Int) {
        holder.homeTeamName.text = games[position].names.first
        holder.awayTeamName.text = games[position].names.second
    }

    private fun setScores(holder: ViewHolder, position: Int) {
        if (!games[position].scores.first.contains("-") && !games[position].scores.second.contains("-")) {
            holder.homeTeamScore.text = games[position].scores.first
            holder.awayTeamScore.text = games[position].scores.second
        }
    }

    private fun setTime(holder: ViewHolder, position: Int) {
        holder.timeText.text = games[position].time
    }

    override fun getItemCount(): Int {
        return games.size
    }

}