package com.yc.yyc;

import com.google.gson.Gson;
import com.yc.yyc.bean.DataBean;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android Studio.
 * User: ${edison}
 * Date: 2020/1/5
 * Time: 16:02
 */
public class a {

    String[] str = {"推荐", "热榜"};

    private void a(){
        List<DataBean> list = new ArrayList<>();

        for (int i = 0;i < str.length;i++){
            String s = str[i];
        }

        JSONObject bean = new JSONObject();
        JSONArray newslist = bean.optJSONArray("newslist");
        JSONObject object = newslist.optJSONObject(0);


    }

}
