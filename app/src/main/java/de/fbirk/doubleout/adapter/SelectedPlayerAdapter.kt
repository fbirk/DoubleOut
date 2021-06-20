package de.fbirk.doubleout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.model.Player.Player
import de.fbirk.doubleout.ui.game.GameStartAddPlayerFragment

class SelectedPlayerAdapter(
    private val context: GameStartAddPlayerFragment,
    var playerList: ArrayList<Player> = arrayListOf(Player(0,0,0,0.0, "TestSpieler"))
) : RecyclerView.Adapter<SelectedPlayerAdapter.ItemViewHolder>() {


    var items: ArrayList<Player> = playerList
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById<TextView>(R.id.txt_selectedPlayer_name)

        fun bind(item: Player){
            name.text = item.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_game_start_selected_player_element, parent, false)

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