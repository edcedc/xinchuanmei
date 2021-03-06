package com.hazz.kotlinmvp.api

import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.EncryptUtils
import com.blankj.utilcode.util.Utils
import com.yc.yyc.base.User
import com.yc.yyc.bean.BaseListBean
import com.yc.yyc.bean.BaseResponseBean
import com.yc.yyc.bean.DataBean
import com.yc.yyc.utils.Constants
import com.yc.yyc.utils.cache.ShareSessionIdCache
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.http.*
import java.util.*


/**
 * Created by xuhao on 2017/11/16.
 * Api 接口
 */

interface ApiService{

    //注册
    @POST("user/register")
    fun userRegister(
        @Query("phone") mobile : String,
        @Query("code") code : String,
        @Query("password") password : String
    ): Observable<BaseResponseBean<Object>>

    //找回密码
    @POST("user/retrievePwd")
    fun userRetrievePwd(
        @Query("phoneNum") mobile : String,
        @Query("code") code : String,
        @Query("password") password : String
    ): Observable<BaseResponseBean<Object>>

    //修改密码
    @POST("user/changePwd")
    fun userChangePwd(
        @Query("phone") mobile : String,
        @Query("code") code : String,
        @Query("password") password : String,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //是否关注用户
    @GET("star/ifFollowUser")
    fun starIfFollowUser(
        @Query("sheUserId") sheUserId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //关注用户
    @POST("star/saveStarFollow")
    fun starSaveStarFollow(
        @Query("sheUserId") sheUserId : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //获取文章列表
    @GET("article/getArticleList")
    fun articleGetArticleList(
        @Query("like") like : String?,
        @Query("category") category : String?,
        @Query("page") pageNumber : Int,
        @Query("type") type : Int?,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取我的文章收藏
    @GET("article/getArticleCollectList")
    fun articleGetArticleCollectList(
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取我收藏的星动态列表
    @GET("star/getStarCollectList")
    fun starGetStarCollectList(
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取星动态列表
    @GET("star/getStarList")
    fun starGetStarList(
        @Query("status") status : Int,
        @Query("like") like : String?,
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //保存编辑星动态
    @Multipart
    @POST("star/saveStar")
    fun starSaveStar(
        @PartMap map : HashMap<String, RequestBody>?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): @JvmSuppressWildcards Observable<BaseResponseBean<DataBean>>

    //我的浏览历史
    @GET("article/getHistoryList")
    fun articleGetHistoryList(
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取我关注的用户列表
    @GET("star/getStarFollowList")
    fun starGetStarFollowList(
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取文章评论列表
    @GET("article/getArticleDiscussesList")
    fun articleGetArticleDiscussesList(
        @Query("articleId") articleId : String?,
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取星动态评论列表
    @GET("star/getStarDiscussList")
    fun starGetStarDiscussList(
        @Query("starId") starId : String?,
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取公告列表
    @GET("notice/getNoticeList")
    fun noticeGetNoticeList(
        @Query("page") pageNumber : Int,
        @Query("size") pageSize : Int = Constants.pageSize,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<BaseListBean<DataBean>>>

    //获取创意轮播图(广告)
    @GET("promotion/getOriginalityListRandom")
    fun promotionGetOriginalityListRandom(): Observable<BaseResponseBean<ArrayList<DataBean>>>

    //获取最新五条公告
    @GET("notice/getFiveNewNotice")
    fun noticeGetFiveNewNotice(): Observable<BaseResponseBean<ArrayList<DataBean>>>

    //获取协议列表
    @GET("agree/getAgreeList")
    fun agreeGetAgreeList(): Observable<BaseResponseBean<ArrayList<DataBean>>>

    //获取热门标签
    @GET("article/getHotTabList")
    fun articleGetHotTabList(): Observable<BaseResponseBean<ArrayList<DataBean>>>

    //文章赞踩
    @POST("article/articlePraise")
    fun articleArticlePraise(
        @Query("articleId") articleId : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //星动态点赞
    @POST("star/saveStarPraise")
    fun starSaveStarPraise(
        @Query("starId") starId : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //保存编辑顶级评论
    @POST("article/saveArticleDiscuss")
    fun articleSaveArticleDiscuss(
        @Query("articleId") articleId : String?,
        @Query("content") content : String?,
        @Query("sheContent") sheContent : String?,
        @Query("sheUserId") sheUserId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<DataBean>>

    //获取随机一条创意
    @GET("promotion/getOriginalityRandom")
    fun promotionGetOriginalityRandom(
    ): Observable<BaseResponseBean<DataBean>>

    //保存编辑星动态评论
    @POST("star/saveStarDiscuss")
    fun starSaveStarDiscuss(
        @Query("starId") starId : String?,
        @Query("content") content : String?,
        @Query("sheContent") sheContent : String?,
        @Query("sheUserId") sheUserId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<DataBean>>

    //星动态评论点赞
    @POST("star/saveStarDispra")
    fun starSaveStarDispra(
        @Query("discussId") articleId : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //文章评论点赞
    @POST("article/saveArticleDispra")
    fun articleSaveArticleDispra(
        @Query("discussId") articleId : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //天行数据转换详情链接  今日头条
    @GET
    fun htmltextIndex(
        @Url url: String = "http://api.tianapi.com/blockchain/index",
        @Query("key") key :String,
        @Query("url") url1 :String
    ): Observable<Object>

    //天行数据转换详情链接
    @GET
    fun blockchainIndex(
        @Url url: String = "http://api.tianapi.com/blockchain/index",
        @Query("key") key :String,
        @Query("url") url1 :String
    ): Observable<Object>

    //删除一条评论(只是该条评论)
    @GET("article/delMyDiscuss")
    fun articleDelMyDiscuss(
        @Query("id") articleId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //删除一条星动态评论(只是该条评论)
    @GET("star/delMyStarDiscuss")
    fun starDelMyStarDiscuss(
        @Query("id") articleId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //保存编辑联系说明
    @POST("agree/saveElation")
    fun articleSaveElation(
        @Query("phone") phone : String?,
        @Query("content") content : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //举报星动态
    @POST("star/saveStarReport")
    fun starSaveStarReport(
        @Query("phone") phone : String?,
        @Query("content") content : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //保存编辑我的历史记录
    @GET("article/saveHistory")
    fun articlesaveHistory(
        @Query("articleId") articleId : String?,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //举报评论
    @POST("article/saveDiscussReport")
    fun articleSaveDiscussReport(
        @Query("discussId") discussId : String?,
        @Query("content") content : String? = "违规数据",
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //举报星动态
    @POST("star/saveStarReport")
    fun starSaveStarReport(
        @Query("starId") starId : String?,
        @Query("phone") phone : String?,
        @Query("content") content : String? = "违规数据",
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //文章收藏
    @POST("article/articleCollect")
    fun articleArticleCollect(
        @Query("ids") ids : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //星动态收藏
    @POST("star/starCollect")
    fun starStarCollect(
        @Query("id") ids : String?,
        @Query("status") status : Int,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<BaseResponseBean<Object>>

    //手机登录
    @POST("user/pLogin")
    fun userPLogin(
        @Query("phone") mobile : String,
        @Query("password") password : String
    ): Observable<ResponseBody>

    //验证码登录
    @POST("user/numLogin")
    fun userNumLogin(
        @Query("phone") mobile : String,
        @Query("code") code : String
    ): Observable<ResponseBody>

    //获取验证码 1注册 2找回 4修改 8绑定 16验证码登录 32实名认证
    @GET("user/getCode")
    fun userGetCode(
        @Query("type") type: Int,
        @Query("phone") phone: String
    ): Observable<BaseResponseBean<Object>>

    //更新个人信息
    @Multipart
    @POST("user/saveUserInfo")
    fun userUpdate(
        @PartMap map : HashMap<String, RequestBody>? = null,
        @Query("userId") userId : String? = User.getInstance().userId
    ): Observable<ResponseBody>


}