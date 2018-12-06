package kotline.zachary.live

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.chad.library.adapter.base.BaseQuickAdapter
import com.sunfusheng.marqueeview.MarqueeView
import com.youth.banner.Banner
import kotline.zachary.live.adapter.HomeAdapter
import kotline.zachary.live.bean.HomeEntity
import kotline.zachary.live.utils.GlideImageLoader
import kotlinx.android.synthetic.main.fg_home.*

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Zhou│Alt │         Space         │ Alt│ Li │Feng│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * Author: Zachary46
 * Time: 2018/12/5
 *
 */
class HomeFragment : Fragment(){
    var list:ArrayList<HomeEntity> = ArrayList()
    var i: Int? = null
    var headerView: View? = null
    private lateinit var mRecyclerView: RecyclerView
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View = inflater.inflate(R.layout.fg_home,null)
        initView(v)
        return v
    }

    private fun initView(v: View) {
        mRecyclerView = v.findViewById(R.id.rv)
        headerView = layoutInflater.inflate(R.layout.view_header,null)
        initData()
    }

    private fun initData() {
        for (i in 1..20) {
            var homeEntity = HomeEntity(
                "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1544070168104&di=170d339180f9b9a4271010c3879e1a58&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170523%2F13fe8ccf2c034abb8b077d211e613980_th.jpg",
                "夜来香",
                "110"
            )
            list.add(homeEntity)
            Log.i("test", "" + i)
        }
        val manager = GridLayoutManager(activity!!, 3)
        mRecyclerView?.layoutManager = manager
        val homeAdapter = HomeAdapter(R.layout.item_home, list)
        mRecyclerView?.adapter = homeAdapter

        homeAdapter.setOnItemClickListener { adapter, view, position ->
            Toast.makeText(activity,""+position,Toast.LENGTH_SHORT).show()
        }

        //轮播图
        var mBanner :Banner = headerView?.findViewById(R.id.banner) as Banner
        var images : ArrayList<String> = ArrayList()
        for (j in 0..list.size-14){
            images.add(list.get(j).img)
        }
        mBanner.setImages(images).setImageLoader(GlideImageLoader()).start()
        mBanner.setOnBannerListener { p->
            Toast.makeText(activity,"banner:"+p,Toast.LENGTH_SHORT).show()
        }

        //跑马灯
        var tvRunBar = headerView?.findViewById(R.id.tvRunBar) as TextView
        tvRunBar.setText("谢航三驴逼谢航三驴逼谢航三驴逼谢航三驴逼谢航三驴逼谢航三驴逼谢航三驴逼")
        homeAdapter.addHeaderView(headerView)
    }

    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}