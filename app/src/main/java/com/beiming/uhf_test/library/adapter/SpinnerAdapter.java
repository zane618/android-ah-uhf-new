package com.beiming.uhf_test.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beiming.uhf_test.R;
import com.beiming.uhf_test.library.bean.LibSpnnerBean;

import java.util.List;

// 自定义 BaseAdapter
public class SpinnerAdapter extends BaseAdapter {

    private List<LibSpnnerBean> _myDataList;
    private Context _context;

    public SpinnerAdapter(List<LibSpnnerBean> myDataList, Context context) {
        this._myDataList = myDataList;
        this._context = context;
    }

    // 需要呈现的 item 的总数
    @Override
    public int getCount() {
        return _myDataList.size();
    }

    // 返回指定索引位置的 item 的对象
    @Override
    public Object getItem(int position) {
        return _myDataList.get(position);
    }

    // 返回指定索引位置的 item 的 id
    @Override
    public long getItemId(int position) {
        return position;
    }


    // 每构造一个 item 就会调用一次 getView() 来获取这个 item 的 view
    // 数据量不大的话就可以像上面那样写，数量大的话则可以像下面这样写
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(_context).inflate(R.layout.spinner_box_kind, parent, false);

            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder); // 将 holder 保存到 convertView 中
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(_myDataList.get(position).getAssectName());

        return convertView;
    }

    class ViewHolder {
        TextView tv;
    }
}