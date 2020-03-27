package com.xiaochen.base.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.xiaochen.base.R

/**
 * @author zlc
 * email : zlc921022@163.com
 * desc : 图片加载类
 */
object ImageUtil {
    /**
     * 正常加载图片
     * @param context
     * @param img
     * @param url
     */
    fun showImage(context: Context, img: ImageView, url: String) {

        val options = RequestOptions()
            .placeholder(1)
            .error(1)
        //               .diskCacheStrategy(DiskCacheStrategy.ALL); //磁盘缓存
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 加载正方形图片
     * @param context
     * @param img
     * @param url
     */
    fun showSquare(context: Context, img: ImageView, url: String) {
        val options = RequestOptions()
            .placeholder(1)
            .error(1)
            .centerCrop()
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 加载头像类圆形图片
     * @param context
     * @param img
     * @param url
     */
    fun showCircle(context: Context, img: ImageView, url: String) {
        val options = RequestOptions()
            .placeholder(1)
            .error(1)
            .circleCrop()
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 加载圆角图片
     * @param context
     * @param img
     * @param url
     */
    fun showRoundImage(context: Context, img: ImageView, url: String) {
        val options = RequestOptions()
            .placeholder(1)
            .error(1)
            .transform(RoundedCorners(10))
        Glide.with(context)
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 加载gif图片
     * @param context
     * @param img
     * @param url
     */
    fun showGif(context: Context, img: ImageView, url: String) {
        val options = RequestOptions()
            .placeholder(R.drawable.icon_pull)
            .error(R.drawable.icon_pull)
        Glide.with(context)
            .asGif()
            .load(url)
            .apply(options)
            .into(img)
    }

    /**
     * 加载gif图片
     * @param context
     * @param img
     * @param resId
     */
    fun showGif2(context: Context, img: ImageView, resId: Int) {
        val options = RequestOptions()
            .placeholder(R.drawable.icon_pull)
            .error(R.drawable.icon_pull)
        Glide.with(context)
            .asGif()
            .load(resId)
            .apply(options)
            .into(img)
    }

    /**
     * 加载本地图片
     * @param context
     * @param img
     * @param url
     */
    fun showLocalImage(context: Context, img: ImageView, url: String) {
        val options = RequestOptions()
            .placeholder(1)
            .error(1)
        Glide.with(context)
            .load(url)
            .thumbnail(0.1f)
            .apply(options)
            .into(img)
    }

}
