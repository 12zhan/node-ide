package com.example.node_edit.util

import okhttp3.ConnectionPool
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okhttp3.Response
import org.json.JSONArray
import java.util.concurrent.TimeUnit


/**
 * HttpUtil封装，异步连接池模式
 */
object HttpUtil {
    /**
     * Http连接超时时间
     */
    private const val Min_CONNECT_TIMEOUT = 3000
    /**
     * Http 写入超时时间
     */
    private const val minWRITE_TIMEOUT = 3000
    /**
     * Http Read超时时间
     */
    private const val minREAD_TIMEOUT = 3000
    /**
     * Http Async Call Timeout
     */
    private const val minCall_TIMEOUT = 3000
    /**
     * Http连接池
     */
    private const val connectionPoolSize = 1000

    /**
     * 静态连接池对象
     */
    private val mConnectionPool = ConnectionPool(connectionPoolSize, 30, TimeUnit.MINUTES)

    /**
     * ContentType
     */
    private const val ContentType = "application/json;charset=utf-8"
    /**
     * AcceptType
     */
    private const val AcceptType = "application/json;charset=utf-8"
    /**
     * Content-Type
     */
    private val MediaType_ContentType = ContentType.toMediaTypeOrNull()

    /**
     * 获取Http Client对象
     * 降低连接时间
     * @return
     */
    fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Min_CONNECT_TIMEOUT.toLong(), TimeUnit.MILLISECONDS) //连接超时
            .readTimeout(minREAD_TIMEOUT.toLong(), TimeUnit.MILLISECONDS) //读取超时
            .writeTimeout(minWRITE_TIMEOUT.toLong(), TimeUnit.MILLISECONDS) //写超时
            .callTimeout(minCall_TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
            // okhttp默认使用的RealConnectionPool初始化线程数==2147483647，在服务端会导致大量线程TIMED_WAITING
            //ThreadPoolExecutor(0, 2147483647, 60L, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp ConnectionPool", true));
            .connectionPool(mConnectionPool)
            .build()
    }
}

data class NodeVersionType(val version: String, val date: String, val download : String)

fun searchNodeVersion(version:Int) : ArrayList<NodeVersionType>{
    val client:OkHttpClient = HttpUtil.getHttpClient()
    val request: Request = Builder().url("https://mirrors.tuna.tsinghua.edu.cn/nodejs-release/index.json")
        .build()
    var response: Response? = null
    response = client.newCall(request).execute()
    val ja = JSONArray(response.body!!.string())

    val r = ArrayList<NodeVersionType>()

    for (i in 0 until ja.length()){
        val item = ja.getJSONObject(i)
        val itemVersion = item.getString("version")
        val itemDate = item.getString("date")
        val itemDownload = "https://mirrors.tuna.tsinghua.edu.cn/nodejs-release/${itemVersion}/node-${itemVersion}-linux-arm-pi.tar.gz"
        r.add(NodeVersionType(itemVersion,itemDate,itemDownload))
    }

    return r
}