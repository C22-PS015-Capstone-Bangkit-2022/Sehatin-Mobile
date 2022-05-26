package com.app.sehatin.ui.activities.main.fragments.content.fragments.home.viewHolder

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.app.sehatin.data.Result
import com.app.sehatin.data.model.Article
import com.app.sehatin.databinding.ItemHomeTopBinding
import com.app.sehatin.ui.activities.main.fragments.content.ContentViewModel
import com.app.sehatin.ui.activities.main.fragments.content.fragments.home.adapter.HeroAdapter
import com.app.sehatin.ui.activities.main.fragments.content.adapter.ViewHolder

class HomeTopHolder(
    itemView: View,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(itemView) {

    private val binding = ItemHomeTopBinding.bind(itemView)
    private lateinit var viewModel: ContentViewModel

    override fun bind(context: Context, viewModel: ContentViewModel) {
        this.viewModel = viewModel
        initListener()
    }

    private fun initListener() {
        viewModel.getArticles(0, 5).observe(lifecycleOwner) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Error -> {

                }
                is Result.Success -> {
                    val article = it.data?.articles
                    if (article != null) {
                        showLoading(false)
                        setViewPager(listArticle)
                    }
                }
            }
        }
    }

    private fun setViewPager(articles: List<Article>) = with(binding) {
        viewPager.adapter = HeroAdapter(articles)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        indicator.setViewPager(this.viewPager)
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        if (isLoading) {
            contentGroup.visibility = View.INVISIBLE
            shimmerLoading.visibility = View.VISIBLE
        } else {
            contentGroup.visibility = View.VISIBLE
            shimmerLoading.visibility = View.GONE
        }
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