package de.fbirk.doubleout.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.github.vipulasri.timelineview.TimelineView
import de.fbirk.doubleout.R
import de.fbirk.doubleout.databinding.ItemTimelineBinding
import de.fbirk.doubleout.functions.formatDateTime
import de.fbirk.doubleout.model.TimeLineModel

class TimeLineAdapter(private val mFeedList: List<TimeLineModel>) :
    RecyclerView.Adapter<TimeLineAdapter.TimeLineViewHolder>() {

    private lateinit var mLayoutInflater: LayoutInflater

    override fun getItemViewType(position: Int): Int {
        return TimelineView.getTimeLineViewType(position, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {

        if (!::mLayoutInflater.isInitialized) {
            mLayoutInflater = LayoutInflater.from(parent.context)
        }

        return TimeLineViewHolder(
            mLayoutInflater.inflate(R.layout.item_timeline, parent, false),
            viewType
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {

        val timeLineModel = mFeedList[position]
        with(holder) {
            if (timeLineModel.date.isNotEmpty()) {
                binding.textTimelineDate.visibility = View.VISIBLE
                binding.textTimelineDate.text =
                    timeLineModel.date.formatDateTime("yyyy-MM-dd HH:mm", "dd-MM-yyyy")
            } else {
                binding.textTimelineDate.visibility = View.GONE
            }
            binding.textTimelineTitle.text = timeLineModel.title
            binding.textTimelineDescription.text = timeLineModel.description
        }

    }

    override fun getItemCount() = mFeedList.size

    inner class TimeLineViewHolder(itemView: View, viewType: Int) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ItemTimelineBinding.bind(itemView)
        private val timeline = binding.timeline

        init {
            timeline.initLine(viewType)
        }
    }
}