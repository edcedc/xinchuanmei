package com.yc.yyc.ui.act

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import com.yc.yyc.R
import com.yc.yyc.adapter.ImageAdapter
import com.yc.yyc.base.BaseActivity
import com.yc.yyc.mvp.impl.ReleaseContract
import com.yc.yyc.mvp.presenter.ReleasePresenter
import com.yc.yyc.utils.PictureSelectorTool
import com.yc.yyc.weight.FullyGridLayoutManager
import kotlinx.android.synthetic.main.a_release.*
import java.util.ArrayList

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/3
 * Time: 17:06
 *  发布
 */
class ReleaseAct : BaseActivity(), ReleaseContract.View, View.OnClickListener {

    val localMediaList = ArrayList<LocalMedia>()
    var imageAdapter: ImageAdapter? = null
    val mPresenter by lazy { ReleasePresenter() }

    override fun getLayoutId(): Int = R.layout.a_release

    override fun initView() {
        setTitle1(getString(R.string.release))
        mPresenter?.init(this)
        btn_commit.setOnClickListener(this)
        imageAdapter = ImageAdapter(act) {
            PictureSelectorTool.PictureSelectorImage(act, localMediaList, PictureConfig.CHOOSE_REQUEST, false)
        }
        recyclerView.setLayoutManager(FullyGridLayoutManager(act, 3, GridLayoutManager.VERTICAL, false))
        recyclerView.setItemAnimator(DefaultItemAnimator())
        recyclerView.setAdapter(imageAdapter)
        imageAdapter?.setOnItemClickListener({ position, v ->
            val media = localMediaList[position]
            PictureSelectorTool.PictureMediaType(act, localMediaList, position)
        })
    }

    override fun initParms(bundle: Bundle) {
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_commit -> {
                mPresenter.onRelease(
                    if (cb_pub.isChecked == true) 2 else 1,
                    null,
                    et_content.text.toString(),
                    localMediaList
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    localMediaList.clear()
                    localMediaList.addAll(PictureSelector.obtainMultipleResult(data))
                    imageAdapter?.setList(localMediaList)
                    imageAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onSuccess() {
        finish()
    }

}