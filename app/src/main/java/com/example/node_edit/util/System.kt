package com.example.node_edit.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast


/**
 * # 文件夹选择器
 * 用于android打开系统文件夹选择器，适配高低版本的
 * @param context Activity上下文
 * @param code 请求码
 * @param onSuccess 成功事件，返回路径
 * @return 是否打开成功
 */
fun openFolderSelector(context:Context,code: Int,onSuccess:(String)->Unit) : Boolean{

    if(context !is Activity){
        throw IllegalArgumentException("Context 必须是activity")
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
            addFlags(
                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
        }
        context.startActivityForResult(intent, code)
    } else {
        // 处理低版本情况，例如使用第三方库或提示不支持
        Toast.makeText(context, "API < 21 不支持此功能", Toast.LENGTH_SHORT).show()
    }
    return false
}

// 获取 Uri 对应的文件路径
fun getRealPathFromUri(context: Context, uri: Uri): Any? {
    return when (uri.scheme) {
        "file" -> uri.path
        "content" -> {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)
                    it.getString(columnIndex)
                }
            }
        }
        else -> null
    }
}