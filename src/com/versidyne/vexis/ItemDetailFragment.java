package com.versidyne.vexis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemDetailFragment extends Fragment {
	
    public static final String ARG_ITEM_ID = "item_id";
    
    ContentMap.DummyItem mItem;
    
    public ItemDetailFragment() {
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ContentMap.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
    	if (mItem != null) {
           /* if (mItem.id.contentEquals("3")) {
            	rootView = inflater.inflate(R.layout.device, container, false);
            } else {*/
            	((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.data);
            //}
        }
    	
        return rootView;
    }
    
}