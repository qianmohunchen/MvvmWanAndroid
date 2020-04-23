package cn.segi.wanandroid.demo.ui.home.adapter

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import cn.segi.wanandroid.R
import cn.segi.wanandroid.demo.base.utils.ColorUtil
import cn.segi.wanandroid.demo.ui.common.data.Article
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.article_item.view.*

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 21:09
 */
open class ArticleAdapter(layoutId: Int, listData: MutableList<Article>?) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutId, listData) {
    override fun convert(viewHolder: BaseViewHolder, article: Article?) {
        viewHolder?.let { holder ->
            holder.itemView.article_material_card.rippleColor =
                ColorUtil.getOneColorStateList(mContext)
            holder.itemView.article_material_card.strokeColor = ColorUtil.getColor(mContext)
            article?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.setText(R.id.item_home_author, handleAuthor(it))
                        .setText(R.id.item_home_content, handleTitle(it))
                        .setText(R.id.item_home_date, it.niceDate)
                        .setText(R.id.item_article_type, handleCategory(it))
                        .setImageResource(R.id.item_list_collect, isCollect(it))
                        .addOnClickListener(R.id.item_list_collect)
                        .setVisible(R.id.item_home_new, it.fresh)
                        .setVisible(R.id.item_home_top_article, it.top)
                        .setGone(R.id.item_home_top_article, it.top)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun handleTitle(article: Article): String {
        article.let {
            return Html.fromHtml(it.title, Html.FROM_HTML_MODE_COMPACT).toString()
        }
    }

    private fun handleAuthor(article: Article): String {
        article.let {
            return when {
                it.author.isNullOrEmpty() and it.shareUser.isNullOrEmpty() -> "匿名用户"
                it.author.isNullOrEmpty() -> "作者：${it.shareUser}" ?: ""
                it.shareUser.isNullOrEmpty() -> "作者：${it.author}" ?: ""
                else -> "作者：${it.author}"
            }
        }
    }

    private fun handleCategory(article: Article): String {
        article.let {
            return when {
                it.superChapterName.isNullOrEmpty() and it.chapterName.isNullOrEmpty() -> ""
                it.superChapterName.isNullOrEmpty() -> it.chapterName ?: ""
                it.chapterName.isNullOrEmpty() -> it.superChapterName ?: ""
                else -> "${it.superChapterName}:${it.chapterName}"
            }
        }
    }

    private fun isCollect(article: Article): Int =
        if (article.collect) R.drawable.collect_selector_icon else R.drawable.uncollect_selector_icon
}