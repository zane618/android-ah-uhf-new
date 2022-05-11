package com.beiming.uhf_test.adapter.pic;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.bean.pic.PhotoBean;
import com.beiming.uhf_test.utils.pic.BitmapUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 附件适配器
 * Created by wanghao on 2017/5/22.
 */

public class AttachmentAdapter extends BaseAdapter {
    private Context mContext;
    private List<PhotoBean> mAttachments = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private int mMax;
    private int isShowAdd;//判断是否显示加号
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();

    public AttachmentAdapter(Context context, List<PhotoBean> attachments, int max, int isShowAdd) {
        this.mContext = context;
        mAttachments.addAll(attachments);
        this.mMax = max;
        this.isShowAdd = isShowAdd;
        layoutInflater = LayoutInflater.from(mContext);
        initData(attachments);
    }

    private void initData(List<PhotoBean> attachments) {
        if (isShowAdd == 0) {
            if (mMax > attachments.size()) {
                PhotoBean photoBean = new PhotoBean();
                photoBean.setImageSrc("+");
                mAttachments.add(photoBean);
            }
        }
    }

    public void modifyData(List<PhotoBean> attachments) {
        mAttachments.clear();
        mAttachments.addAll(attachments);
        initData(mAttachments);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAttachments.size();
    }

    @Override
    public Object getItem(int i) {
        return mAttachments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (!viewMap.containsKey(i) || viewMap.get(i) == null) {
            holder = new ViewHolder();
            view = layoutInflater.inflate(R.layout.item_gridview, null);
            holder.image = (ImageView) view.findViewById(R.id.imageView1);
            view.setTag(holder);
            viewMap.put(i, view);
        } else {
            view = viewMap.get(i);
            holder = (ViewHolder) view.getTag();
        }
        holder.image.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
//            holder.image.setImageDrawable(new ColorDrawable(ContextCompat.getColor(mContext, R.color.transparent)));
        loadPic(holder.image, mAttachments.get(i).getImageSrc(), i);
        return view;
    }

    public class ViewHolder {
        public ImageView image;
    }

    private void loadPic(final ImageView imageView, String picPath, int i) {
        if ("+".equals(picPath)) {
            Glide.with(mContext).load(R.mipmap.add).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
        } else {
            BitmapUtils.displayImage(mContext, picPath, R.mipmap.icon_error, imageView);
//            }
        }
    }
}
