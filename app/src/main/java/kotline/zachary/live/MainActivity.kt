package kotline.zachary.live

import android.os.Bundle
import android.support.v4.app.FragmentTransaction
import android.view.KeyEvent
import android.widget.Toast
import com.flyco.tablayout.listener.CustomTabEntity
import com.flyco.tablayout.listener.OnTabSelectListener
import kotline.zachary.live.R.id.tab_layout
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList
import kotline.zachary.live.base.BaseActivity
import kotline.zachary.live.bean.TabEntity

class MainActivity : BaseActivity() {
    val instance by lazy { this }
    private var mIndex = 0
    private val mTitles = arrayOf("首页", "云播", "个人")
    // 未被选中的图标
    private val mIconNo = intArrayOf(
        R.mipmap.home_no,
        R.mipmap.cloud_no,
        R.mipmap.mine_no
    )
    // 被选中的图标
    private val mIconYes = intArrayOf(
        R.mipmap.home_yes,
        R.mipmap.cloud_yes,
        R.mipmap.mine_yes
    )
    private val homeFragment: HomeFragment? = null
    private val cloudFragment: CloudFragment? = null
    private val mineFragment: MineFragment? = null
    private val mTabEntities = ArrayList<CustomTabEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getTitleBar().setTitle("粉嫩生活").hideBackIcon()
    }

    override fun initView() {
        initTab()
        tab_layout.currentTab = mIndex
        switchFragment(mIndex)
    }

    override fun initData() {

    }

    override fun initLayout(): Int {
        return R.layout.activity_main
    }

    //初始化底部菜单
    private fun initTab() {//类似for循环添加TabEntity到List
        (0 until mTitles.size)
            .mapTo(mTabEntities) { TabEntity(mTitles[it], mIconYes[it], mIconNo[it]) }
        //为Tab赋值
        tab_layout.setTabData(mTabEntities)
        tab_layout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                //切换Fragment
                switchFragment(position)
            }

            override fun onTabReselect(position: Int) {

            }
        })
    }

    private fun switchFragment(position: Int) {
        var transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when(position){
            0 -> {
                getTitleBar().setTitle("粉嫩生活")
                homeFragment?.let {
                    transaction.show(it)
                } ?: HomeFragment.getInstance()?.let {
                    transaction.replace(R.id.fl_container, it)
                }
            }
            1 -> {
                getTitleBar().setTitle("云播")
                cloudFragment?.let {
                    transaction.show(it)
                } ?: CloudFragment.getInstance()?.let {
                    transaction.replace(R.id.fl_container, it)
                }
            }
            2 -> {
                getTitleBar().setTitle("我的")
                mineFragment?.let {
                    transaction.show(it)
                } ?: MineFragment.getInstance()?.let {
                    transaction.replace(R.id.fl_container, it)
                }
            }

            else ->{}
        }

        mIndex = position
        tab_layout.currentTab = mIndex
        transaction.commitAllowingStateLoss()
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        homeFragment?.let { transaction.hide(it) }
        cloudFragment?.let { transaction.hide(it) }
        mineFragment?.let { transaction.hide(it) }
    }

    private var mExitTime: Long = 0

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
