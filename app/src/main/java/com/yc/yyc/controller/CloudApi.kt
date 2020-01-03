package com.yc.yyc.controller

/**
 * 作者：yc on 2018/6/20.
 * 邮箱：501807647@qq.com
 * 版本：v1.0
 */

class CloudApi private constructor() {

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    companion object{

//        val url =
//            "10.0.1.141:8080"
//
//        val SERVLET_URL = "http://" +
//                url + "/forward/"

//        http://jj123.nat300.top/adv_chain/api/user/update

        private val url =
            "47.112.53.46:8080"

        val SERVLET_URL = "http://" +
                url + "/extend/"

        val SERVLET_IMG_URL = SERVLET_URL + "attach/showPic?attachId="

    }


}