package com.app.sehatin.ui.activities.main.fragments.paymentDoctor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.app.sehatin.data.model.PaymentMethod
import com.app.sehatin.databinding.ItemPaymentMethodBinding
import com.bumptech.glide.Glide

class PaymentMethodAdapter(private val onMethodSelected: (PaymentMethod) -> Unit): ListAdapter<PaymentMethod, PaymentMethodAdapter.Holder>(DIFF_CALLBACK) {
    private var selectedPosition = -1


    inner class Holder(private val binding : ItemPaymentMethodBinding): RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(paymentMethod: PaymentMethod) = with(binding) {
            radioBtn.isChecked = bindingAdapterPosition == selectedPosition
            Glide.with(this.root)
                .load(paymentMethod.imageUrl)
                .into(paymentImage)
            paymentName.text = paymentMethod.name
            this.root.setOnClickListener {
                selectedPosition = bindingAdapterPosition
                notifyDataSetChanged()
                onMethodSelected(paymentMethod)
            }
            radioBtn.isClickable = false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemPaymentMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<PaymentMethod> =
            object : DiffUtil.ItemCallback<PaymentMethod>() {
                override fun areItemsTheSame(oldUser: PaymentMethod, newUser: PaymentMethod): Boolean {
                    return oldUser.name == newUser.name
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: PaymentMethod, newUser: PaymentMethod): Boolean {
                    return oldUser == newUser
                }
            }
    }

}