package kotline.zachary.live

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotline.zachary.live.base.BaseMvpActivity
import kotline.zachary.live.bean.HomeEntity
import kotline.zachary.live.mvp.HomeContact
import kotline.zachary.live.mvp.HomePresenter
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseMvpActivity<HomeContact.Presenter,HomeContact.View>(),HomeContact.View{
    override fun loadDataFail(s: String) {
        text.setText(s)
    }

    override fun loadDataSuccess(s: HomeEntity) {
        text.setText(s.thumb+"\n"+s.name+"\n"+s.age)
    }

    override fun initLayout(): Int {
        return R.layout.activity_test
    }

    override fun initView() {

    }

    override fun initData() {
        getPresenter().loadData()
    }

    override fun createPresenter(): HomeContact.Presenter {
        return HomePresenter()
    }

    override fun createView(): HomeContact.View {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
