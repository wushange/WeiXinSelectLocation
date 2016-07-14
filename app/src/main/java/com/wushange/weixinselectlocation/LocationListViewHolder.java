package com.wushange.weixinselectlocation;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.tencent.lbssearch.object.result.Geo2AddressResultObject;


/**
 * Created by Mr.Jude on 2015/2/22.
 */
public class LocationListViewHolder extends BaseViewHolder<Geo2AddressResultObject.ReverseAddressResult.Poi> {
    private TextView mLocationName;
    private TextView mLocationAddr;


    public LocationListViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_select_location_view);
        mLocationName = $(R.id.location_name);
        mLocationAddr = $(R.id.location_addr);
    }

    @Override
    public void setData(final Geo2AddressResultObject.ReverseAddressResult.Poi tencentPoi) {
        mLocationName.setText("" + tencentPoi.title);
        mLocationAddr.setText("" + tencentPoi.address);
    }
}
