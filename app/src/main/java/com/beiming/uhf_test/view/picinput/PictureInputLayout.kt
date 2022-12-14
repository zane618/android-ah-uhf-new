package com.beiming.uhf_test.view.picinput

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beiming.uhf_test.R
import com.beiming.uhf_test.bean.pic.PhotoBean
import com.beiming.uhf_test.dialog.GuideDlg
import com.beiming.uhf_test.utils.*
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import com.luck.picture.lib.engine.CompressFileEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener
import com.luck.picture.lib.interfaces.OnResultCallbackListener
import com.luck.picture.lib.utils.DensityUtil
import top.zibin.luban.Luban
import top.zibin.luban.OnNewCompressListener
import java.io.File


/**
 * created by zhangshi on 2022/8/23.
 */
class PictureInputLayout(context: Context, attributeSet: AttributeSet? = null) :
    LinearLayout(context, attributeSet) {

    private lateinit var listener: iPicSelect
    private val chooseMode = SelectMimeType.ofImage()

    private lateinit var rvFirst: RecyclerView
    private lateinit var adapterFirst: PicAdapter
    private var dataFirst = java.util.ArrayList<LocalMedia>()

    private lateinit var biaoweiRv: RecyclerView
    private lateinit var biaoweiAdapter: PicAdapter
    private var biaoweiData = java.util.ArrayList<LocalMedia>()

    private lateinit var singleRv: RecyclerView
    private lateinit var singleAdapter: PicAdapter
    private var singleData = java.util.ArrayList<LocalMedia>()

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_picture_input, this)
        initRvFirst()
        initBiaoweiRv()
        initSingleRv()
        initClick()


    }

    /**
     * ???????????????
     */
    private class ImageFileCompressEngine : CompressFileEngine {
        override fun onStartCompress(
            context: Context,
            source: ArrayList<Uri>,
            call: OnKeyValueResultCallbackListener
        ) {
            Luban.with(context).load(source).ignoreBy(100).setRenameListener { filePath ->
                val indexOf = filePath.lastIndexOf(".")
                val postfix = if (indexOf != -1) filePath.substring(indexOf) else ".jpg"

                val file = File(filePath)
                file.name
//                DateUtils.getCreateFileName("CMP_") + postfix
            }.setCompressListener(object : OnNewCompressListener {
                override fun onStart() {}
                override fun onSuccess(source: String, compressFile: File) {
                    if (call != null) {
                        LogPrintUtil.zhangshi("?????????" + compressFile.absolutePath)
                        call.onCallback(source, compressFile.absolutePath)
                    }
                }

                override fun onError(source: String, e: Throwable) {
                    if (call != null) {
                        call.onCallback(source, null)
                    }
                }
            }).setTargetDir(ConstantUtil.IMAGE_STR + TimeUtils.getY_M_D_Time())
                .launch()
        }
    }

    private fun initRvFirst() {
        rvFirst = findViewById(R.id.rvFirst)
        val fullyGridLayoutManager = FullyGridLayoutManager(
            context,
            4, GridLayoutManager.VERTICAL, false
        )
        rvFirst.layoutManager = fullyGridLayoutManager
        rvFirst.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                DensityUtil.dip2px(context, 8F), false
            )
        )
        dataFirst.add(LocalMedia())
        adapterFirst = PicAdapter(dataFirst)
        rvFirst.adapter = adapterFirst

        adapterFirst.setOnItemClickListener { adapter, view, position ->
            if (position == (adapter.data.size - 1)) {
                // ????????????
                PictureSelector.create(context)
                    .openCamera(SelectMimeType.ofImage())
                    .setCompressEngine(ImageFileCompressEngine())
                    .setOutputCameraDir(ConstantUtil.IMAGE_STR + TimeUtils.getY_M_D_Time())
//                    .setSelectedData()
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {

                            result?.let {
                                for (medie in result) {
                                    dataFirst.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                adapterFirst.notifyDataSetChanged()
                            }
                        }

                        override fun onCancel() {}
                    })

                //????????????
                /*PictureSelector.create(context)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.let {
                                for (medie in result) {
                                    dataFirst.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                adapterFirst.notifyDataSetChanged()
                            }
                        }
                        override fun onCancel() {}
                    })*/

            } else {
                val list = java.util.ArrayList<LocalMedia>()
                for (i in 0 until dataFirst.size - 1) {
                    list.add(dataFirst[i])
                }
                PictureSelector.create(context)
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        override fun onPreviewDelete(position: Int) {}
                        override fun onLongPressDownload(media: LocalMedia): Boolean {
                            return false
                        }
                    }).startActivityPreview(
                        position, false,
                        list
                    )
            }
        }
    }


    private fun initBiaoweiRv() {
        biaoweiRv = findViewById(R.id.rvBiaowei)
        val fullyGridLayoutManager = FullyGridLayoutManager(
            context,
            4, GridLayoutManager.VERTICAL, false
        )
        biaoweiRv.layoutManager = fullyGridLayoutManager
        biaoweiRv.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                DensityUtil.dip2px(context, 8F), false
            )
        )
        biaoweiData.add(LocalMedia())
        biaoweiAdapter = PicAdapter(biaoweiData)
        biaoweiRv.adapter = biaoweiAdapter

        biaoweiAdapter.setOnItemClickListener { adapter, view, position ->
            if (position == (adapter.data.size - 1)) {
                // ????????????
                PictureSelector.create(context)
                    .openCamera(SelectMimeType.ofImage())
                    .setCompressEngine(ImageFileCompressEngine())
                    .setOutputCameraDir(ConstantUtil.IMAGE_STR + TimeUtils.getY_M_D_Time())
//                    .setSelectedData()
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.let {
                                for (medie in result) {
                                    biaoweiData.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                biaoweiAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onCancel() {}
                    })

                //????????????
                /*PictureSelector.create(context)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.let {
                                for (medie in result) {
                                    biaoweiData.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                biaoweiAdapter.notifyDataSetChanged()
                            }
                        }
                        override fun onCancel() {}
                    })*/

            } else {
                val list = java.util.ArrayList<LocalMedia>()
                for (i in 0 until biaoweiData.size - 1) {
                    list.add(biaoweiData[i])
                }
                PictureSelector.create(context)
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        override fun onPreviewDelete(position: Int) {}
                        override fun onLongPressDownload(media: LocalMedia): Boolean {
                            return false
                        }
                    }).startActivityPreview(
                        position, false,
                        list
                    )
            }
        }
    }

    private fun initSingleRv() {
        singleRv = findViewById(R.id.rvSingle)
        val fullyGridLayoutManager = FullyGridLayoutManager(
            context,
            4, GridLayoutManager.VERTICAL, false
        )
        singleRv.layoutManager = fullyGridLayoutManager
        singleRv.addItemDecoration(
            GridSpacingItemDecoration(
                4,
                DensityUtil.dip2px(context, 8F), false
            )
        )
        singleData.add(LocalMedia())
        singleAdapter = PicAdapter(singleData)
        singleRv.adapter = singleAdapter

        singleAdapter.setOnItemClickListener { adapter, view, position ->
            if (position == (adapter.data.size - 1)) {
                // ????????????
                PictureSelector.create(context)
                    .openCamera(SelectMimeType.ofImage())
                    .setCompressEngine(ImageFileCompressEngine())
                    .setOutputCameraDir(ConstantUtil.IMAGE_STR + TimeUtils.getY_M_D_Time())
//                    .setSelectedData()
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.let {
                                for (medie in result) {
                                    singleData.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                singleAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onCancel() {}
                    })

                //????????????
                /*PictureSelector.create(context)
                    .openGallery(SelectMimeType.ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .forResult(object : OnResultCallbackListener<LocalMedia?> {
                        override fun onResult(result: ArrayList<LocalMedia?>?) {
                            result?.let {
                                for (medie in result) {
                                    singleData.add(0, medie!!)
                                    LogPrintUtil.zhangshi("medie:" + medie.realPath)
                                }
                                singleAdapter.notifyDataSetChanged()
                            }
                        }
                        override fun onCancel() {}
                    })*/

            } else {
                val list = java.util.ArrayList<LocalMedia>()
                for (i in 0 until singleData.size - 1) {
                    list.add(singleData[i])
                }
                PictureSelector.create(context)
                    .openPreview()
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setExternalPreviewEventListener(object : OnExternalPreviewEventListener {
                        override fun onPreviewDelete(position: Int) {}
                        override fun onLongPressDownload(media: LocalMedia): Boolean {
                            return false
                        }
                    }).startActivityPreview(
                        position, false,
                        list
                    )
            }
        }
    }


    private fun initClick() {
        //?????????
        findViewById<View>(R.id.tv_shili01).setOnClickListener {
            val guideDlg = GuideDlg(context, PicConst.TYPE_1)
            guideDlg.show()
        }
        //????????????
        findViewById<View>(R.id.tv_shili02).setOnClickListener {
            val guideDlg = GuideDlg(context, PicConst.TYPE_2)
            guideDlg.show()
        }
        //?????????
        findViewById<View>(R.id.tv_shili03).setOnClickListener {
            val guideDlg = GuideDlg(context, PicConst.TYPE_3)
            guideDlg.show()
        }

    }

    fun getPhotoBeans(): List<PhotoBean>? {
        if (dataFirst.size == 1) {
            ToastUtils.showToast("??????????????????????????????")
            return null
        } else if (biaoweiData.size == 1) {
            ToastUtils.showToast("???????????????????????????")
            return null
        } else if (singleData.size == 1) {
            ToastUtils.showToast("????????????????????????????????????")
            return null
        }
        val photobeans = mutableListOf<PhotoBean>()
        val time = TimeUtils.getTime()
        for (item in dataFirst) {
            if (TextUtils.isEmpty(item.realPath)) {
                break
            }
            val photo = PhotoBean(PicConst.TYPE_1)
            photo.measAssetBarCode = ""
            photo.createTime = time
            photo.imageSrc = item.realPath
            photo.picName = item.fileName
            photobeans.add(photo)
        }
        for (item in biaoweiData) {
            if (TextUtils.isEmpty(item.realPath)) {
                break
            }
            val photo = PhotoBean(PicConst.TYPE_2)
            photo.measAssetBarCode = ""
            photo.createTime = time
            photo.imageSrc = item.realPath
            photo.picName = item.fileName
            photobeans.add(photo)
        }
        for (item in singleData) {
            if (TextUtils.isEmpty(item.realPath)) {
                break
            }
            val photo = PhotoBean(PicConst.TYPE_3)
            photo.measAssetBarCode = ""
            photo.createTime = time
            photo.imageSrc = item.realPath
            photo.picName = item.fileName
            photobeans.add(photo)
        }
        return photobeans
    }


    fun setListener(iPicSelect: iPicSelect) {
        listener = iPicSelect
    }


}