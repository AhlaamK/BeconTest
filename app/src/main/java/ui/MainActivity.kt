package ui

//import com.ms.square.android.expandabletextview.ExpandableTextView
//import testbecon.com.ExpandableTextView
import adapters.ViewPagerAdapter
import android.app.ProgressDialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.support.annotation.RequiresApi
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import at.blogc.android.views.ExpandableTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import connection.ApiClient
import model.MainModel
import model.Post_medias
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import testbecon.com.beconapptest.R
import testbecon.com.beconapptest.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var context: Context
    var postmedias:List<Post_medias> = emptyList()
    internal var firstCurrentItem = 0
    var active = 0
    internal var itemPosition = 0
    private lateinit var adapter: ViewPagerAdapter
    private lateinit var pdLoading:ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        InitializeViews()

        context = this

        pdLoading = ProgressDialog(context)

        // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        binding.expandableTextView.setAnimationDuration(750L);
        binding.expandableTextView.setExpandInterpolator( OvershootInterpolator())
        binding.expandableTextView.setCollapseInterpolator( OvershootInterpolator())

        val actionBar = supportActionBar
        getData()

        supportActionBar?.show()
        binding.buttonToggle.setOnClickListener {
            if (binding.expandableTextView.isExpanded())
            {
                binding.expandableTextView.collapse();
                binding.buttonToggle.setText(R.string.expand);
            }
            else
            {
                binding.expandableTextView.expand();
                binding.buttonToggle.setText(R.string.collapse);
            }

        }


        binding.expandableTextView.addOnExpandListener(object : ExpandableTextView.OnExpandListener {
            override fun onExpand(view: ExpandableTextView) {
                System.out.println( "ExpandableTextView expanded")
            }

            override fun onCollapse(view: ExpandableTextView) {
                System.out.println( "ExpandableTextView collapsed")
            }
        })


    }


     fun InitializeViews() {

          binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
          binding.mainViewModel = this
          binding.executePendingBindings()
          context = this
         binding.lytPageIndicator


      }


    private fun getData() {
        val call: Call<MainModel> = ApiClient.getClient.getData()
        pdLoading.setMessage("\tLoading...")
        pdLoading.show()
        call.enqueue(object : Callback<MainModel> {
            override fun onFailure(call: Call<MainModel>, t: Throwable) {
                System.out.println("t val  is ${t.toString()}")
                pdLoading.dismiss()
            }

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onResponse(call: Call<MainModel>?, response:  Response<MainModel>?) {

                pdLoading.dismiss()
                if (response != null) {
                    if(response.isSuccessful) {
                        var partList : List<Post_medias>? = response.body()?.post_medias
                        postmedias= response.body()!!.post_medias
                        val adapter = ViewPagerAdapter(context, postmedias!!)
                        // description bold
                        val decrpString =  response.body()!!.description

                        val descrArray = decrpString.split(" ").toTypedArray()

                        val styledString = SpannableString(decrpString)
                        var  descrArray2 = descrArray.filter({ str ->  str.startsWith("@") || str.startsWith("#") })

                        for(item in descrArray2) {
                            styledString.setSpan(StyleSpan(Typeface.BOLD),decrpString.indexOf(item), decrpString.indexOf(item) +item.length, 0)
                        }

                        binding.expandableTextView.setText(styledString)
//Loading member to actionbar
                        Glide.with(context).asDrawable().apply(RequestOptions()
                                .override(100, 100)
                                .circleCrop()).load(response.body()!!.member.resized_avatar).into(object : SimpleTarget<Drawable>() {
                            override fun onResourceReady(@NonNull resource: Drawable, @Nullable transition: Transition<in Drawable>) {
                                supportActionBar!!.setDisplayShowHomeEnabled(true)
                                supportActionBar!!.setIcon(resource)
                                supportActionBar!!.setDisplayShowTitleEnabled(false)


                            }


                        })

                        binding.viewPager.adapter = adapter
                        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

                            }

                            override fun onPageSelected(position: Int) {
                                if (itemPosition > position) {
                                    if (active == 0) {
                                        active = postmedias.size - 1
                                    } else {
                                        active--
                                    }
                                } else {
                                    if (active >= postmedias.size - 1) {
                                        active = 0
                                    } else {
                                        active++
                                    }
                                }

                                itemPosition = position
                                setPageIndicator(active)
                            }

                            override fun onPageScrollStateChanged(state: Int) {

                            }
                        })

                        for (i in postmedias.indices) {
                            val view = ImageView(context)
                            binding.lytPageIndicator.addView(view)
                        }

                        setPageIndicator(firstCurrentItem)


                    } else{

                    }

                }


            }

            fun setPageIndicator(position: Int) {
                var imageView: ImageView
                val lp =
                        LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                lp.setMargins(5, 0, 5, 0)
                for (i in 0 until binding.lytPageIndicator.getChildCount()) {
                    imageView = binding.lytPageIndicator.getChildAt(i) as ImageView
                    imageView.layoutParams = lp
                    if (position == i) {
                        imageView.setImageResource(R.drawable.active)
                    } else {
                        imageView.setImageResource(R.drawable.inactive)
                    }
                }
            }

        })



    }




}



















