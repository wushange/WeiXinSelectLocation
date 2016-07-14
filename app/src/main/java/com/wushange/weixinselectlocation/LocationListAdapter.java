package com.wushange.weixinselectlocation;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;

/**
 * Created by Mr.Jude on 2015/7/18.
 */
public class LocationListAdapter extends RecyclerArrayAdapter<Geo2AddressResultObject.ReverseAddressResult.Poi> {
    public LocationListAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocationListViewHolder(parent);
    }
}
