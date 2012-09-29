package com.zoostudio.zooslideshow;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {
	private ZooSlideView mSildeView;
	private ZooLikerView mLikerView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSildeView = (ZooSlideView) this.findViewById(R.id.zooSlideView1);
        mLikerView = (ZooLikerView) this.findViewById(R.id.zooLikerView1);
        
        ArrayList<String> datas = new ArrayList<String>();
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        datas.add("");
        
        
        ArrayList<LikerItem> likers = new ArrayList<LikerItem>();
        likers.add(new LikerItem("", "11"));
        likers.add(new LikerItem("", "12"));
        likers.add(new LikerItem("", "13"));
//        likers.add(new LikerItem("", "14"));
        
        mLikerView.setDatas(likers, 0);
        mSildeView.setDatas(datas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    
}
