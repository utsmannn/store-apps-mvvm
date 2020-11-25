package com.utsman.home.ui.holder

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.utsman.abstraction.ext.loadUrl
import com.utsman.abstraction.ext.toSumReadable
import com.utsman.data.model.dto.AppsSealedView
import com.utsman.home.databinding.ItemAppsBannerBinding

class AppsBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemAppsBannerBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun bind(item: AppsSealedView.AppsBannerView) = binding.run {
        txtTitle.text = item.name
        txtDownloads.text = "${item.downloads.toSumReadable()} Downloads"
        imgBanner.loadUrl(url = item.image, id = item.id.toString())
    }
}