package com.app.sehatin.ui.activities.objectDetection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.databinding.ItemOdtResultBinding
import org.tensorflow.lite.task.vision.detector.Detection

class DetectorResultAdapter(private val results: List<Detection>): RecyclerView.Adapter<DetectorResultAdapter.Holder>() {

    inner class Holder(private val binding: ItemOdtResultBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(result: Detection) = with(binding) {
            val category = result.categories.first()
            val score = "${category.score.times(100).toInt()}%"
            resultName.text = category.label
            resultScore.text = score
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemOdtResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int = results.size


}