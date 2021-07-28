package de.fbirk.doubleout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.ui.game.main.GameMainView

class ActivePlayerAdapter(
    private val context: GameMainView,
    var playerList: ArrayList<Player> = arrayListOf()
) : RecyclerView.Adapter<ActivePlayerAdapter.ItemViewHolder>() {


    var items: ArrayList<Player> = playerList
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById<TextView>(R.id.txt_gameOverview_playerName)
        val points: TextView = view.findViewById(R.id.txt_gameOverview_currentPoints)
        val avg: TextView = view.findViewById(R.id.txt_gameOverview_gameAvg)
        val position: TextView = view.findViewById(R.id.txt_gameOverview_position)

        fun bind(item: Player){
            name.text = item.name
            avg.text = item.avgPoints.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_game_main_player_overview_element, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        if (items != null) {
            return items.size
        }
        return 0
    }
}