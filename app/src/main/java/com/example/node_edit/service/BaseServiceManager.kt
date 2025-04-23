package com.example.node_edit.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BaseServiceManager : Service() {


    private var services = HashMap<String,BaseService>()
    private val binder = LocalBinder()

    inner class LocalBinder : Binder() {
        fun getService(): BaseServiceManager = this@BaseServiceManager
    }

    override fun onBind(intent: Intent): IBinder {
//        开始注册

        //项目文件树监听器服务
        val serviceArray = arrayOf(
            FileTreeMonitor()
        )

        serviceArray.map {
            services.put(it.name,it)
        }

        return binder
    }

    override fun onCreate() {
        super.onCreate()
        services.map {
            it.value.start()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}

abstract class BaseService{
    abstract val name:String
    abstract val description:String
    abstract fun start()
    abstract fun stop()
}