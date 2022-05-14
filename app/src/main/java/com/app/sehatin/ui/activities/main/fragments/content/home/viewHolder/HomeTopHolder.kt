package com.app.sehatin.ui.activities.main.fragments.content.home.viewHolder

import android.content.Context
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeTopBinding
import com.app.sehatin.ui.activities.main.fragments.content.home.adapter.HeroAdapter
import com.app.sehatin.ui.sharedAdapter.ViewHolder

class HomeTopHolder(itemView: View): ViewHolder(itemView) {
    private val binding = ItemHomeTopBinding.bind(itemView)

    override fun bind(context: Context) = with(binding) {
        viewPager.adapter = HeroAdapter(listArticle)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        indicator.setViewPager(this.viewPager)
    }

    private var listArticle = mutableListOf(
        Article(
            title = "Ini 6 Cara Kembalikan Produktivitas Setelah Libur Lebaran",
            thumbnail = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/05/07/2566944117.jpg",
            createdAt = "6 Mei 2021",
            tags = listOf("Hidup Sehat", "Aktivitas")
        ),
        Article(
            title = "Ini 6 Cara Kembalikan Produktivitas Setelah Libur Lebaran",
            thumbnail = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/05/07/2566944117.jpg",
            createdAt = "6 Mei 2021",
            tags = listOf("Hidup Sehat")
        ),
        Article(
            title = "Ini 6 Cara Kembalikan Produktivitas Setelah Libur Lebaran",
            thumbnail = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/05/07/2566944117.jpg",
            createdAt = "6 Mei 2021",
            tags = listOf("Hidup Sehat")
        ),
        Article(
            title = "Ini 6 Cara Kembalikan Produktivitas Setelah Libur Lebaran",
            thumbnail = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/05/07/2566944117.jpg",
            createdAt = "6 Mei 2021",
            tags = listOf("Hidup Sehat")
        ),
        Article(
            title = "Ini 6 Cara Kembalikan Produktivitas Setelah Libur Lebaran",
            thumbnail = "https://assets.pikiran-rakyat.com/crop/0x0:0x0/x/photo/2020/05/07/2566944117.jpg",
            createdAt = "6 Mei 2021",
            tags = listOf("Hidup Sehat")
        )
    )

}