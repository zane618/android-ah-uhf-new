package com.beiming.uhf_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.beiming.uhf_test.R;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.List;

/**
 * created by zhangshi on 2022/8/9.
 */
public class MainViewPagerAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private Context mContext;
    private String[] mNames;
    private List<Fragment> mFragments;

    public MainViewPagerAdapter(Context context, FragmentManager fragmentManager, List<Fragment> fragments, String[] names) {
        super(fragmentManager);
        this.mContext = context;
        this.mFragments = fragments;
        this.mNames = names;
    }

    @Override
    public int getCount() {
        return null == mFragments ? 0 : mFragments.size();
    }

    @Override
    public View getViewForTab(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.top_tab_item, viewGroup, false);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.tv);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(mNames[position]);

        return view;
    }

    @Override
    public Fragment getFragmentForPage(int i) {
        return mFragments.get(i);
    }

    private static class ViewHolder {
        TextView textView;
    }
}
