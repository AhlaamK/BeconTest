package adapters

import android.content.Context
import android.os.Build
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.MediaController
import android.widget.VideoView
import com.bumptech.glide.Glide
import model.Post_medias
import testbecon.com.beconapptest.R

class ViewPagerAdapter(internal var context: Context, internal var itemList: List<Post_medias>) : PagerAdapter() {
    internal var mLayoutInflater: LayoutInflater
    private var pos = 0
    lateinit var mediacntroler: MediaController
    init {
        mLayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val holder = ViewHolder()
        val itemView = mLayoutInflater.inflate(R.layout.adapter_view_pager, container, false)
        holder.itemImage = itemView.findViewById(R.id.img_slider) as ImageView
        holder.itemVideo=itemView.findViewById(R.id.videoView) as VideoView

/*        MediaController mediaController = new MediaController(context);
mediaController.setAnchorView(holder.itemVideo);
videoView.setMediaController(mediaController);
videoView.setVideoURI(Uri.parse(videoUrl));
videoView.start();*/
        //notifyDataSetChanged()

        if (pos > this.itemList.size-1)
            pos = 0

        holder.postmedia = this.itemList[pos]
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(holder.postmedia.media.endsWith(".mp4")){
                holder.itemVideo.setVideoPath(holder.postmedia.media);
                holder.itemVideo.start()
                mediacntroler= MediaController(context)
                mediacntroler.setAnchorView(holder.itemVideo)
                holder.itemVideo.setMediaController(mediacntroler)
            }else{
            Glide.with(context).load(holder.postmedia.media).into(holder.itemImage)

        }
        }

        (container as ViewPager).addView(itemView)

        holder.itemImage.scaleType = ImageView.ScaleType.FIT_XY


        pos++
        return itemView
    }

    internal class ViewHolder {
        lateinit var postmedia: Post_medias
        lateinit var itemImage: ImageView
        lateinit var itemVideo: VideoView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1 as View
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View)
    }
}