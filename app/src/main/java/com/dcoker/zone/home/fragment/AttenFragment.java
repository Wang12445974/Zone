package com.dcoker.zone.home.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dcoker.zone.Activity.HomeActivity;
import com.dcoker.zone.R;
import com.dcoker.zone.adapter.MyrecycleAdapter;
import com.dcoker.zone.adapter.attenAdapter;
import com.dcoker.zone.config.NetConfig;
import com.dcoker.zone.entity.Article;
import com.dcoker.zone.entity.IndexData;
import com.dcoker.zone.entity.User;
import com.dcoker.zone.home.activity.FriendDetilActivity;
import com.dcoker.zone.listener.MyItemClickListener;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;

import butterknife.BindView;

/**
 * Created by Mr.Zhang on 2017/7/31.
 */

public class AttenFragment extends BaseFragment {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    User user;
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_atten;
    }

    @Override
    public void finishCreateView(Bundle state) {



        try {

            user =((HomeActivity)getActivity()).getUser();

        }catch (Exception e){

            user =((FriendDetilActivity)getActivity()).getUser();
        }
        if(isVisible()){
            lazyLoad();
        }

    }

    @Override
    public void lazyLoad() {



        OkGo.<String>get(NetConfig.INDEX)
                .cacheMode(CacheMode.REQUEST_FAILED_READ_CACHE)
                .cacheKey("AttenFragment")
                .params("uid",user.getId())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                        Gson gson = new Gson();
                        String s = response.body().toString();
                        IndexData indexdata =  gson.fromJson(/*Testdata.indexData*/s, IndexData.class);

                        if(indexdata.getData()!=null&&indexdata.getData().getState()==0){

                            showList(indexdata);
                        }else{

                        }
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void showList(final IndexData minedata){

//创建默认的线性LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recycleview.setLayoutManager(mLayoutManager);
//如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        recycleview.setHasFixedSize(true);

        attenAdapter mAdapter = new attenAdapter(minedata,activity);

        recycleview.setAdapter(mAdapter);
        mAdapter.setItemListener(new MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {

                    Intent intent = new Intent(getActivity(),FriendDetilActivity.class);
                    intent.putExtra("User",minedata.getData().getAttenList().get(postion).getUser());
                    startActivity(intent);
            }
        });

    }


}