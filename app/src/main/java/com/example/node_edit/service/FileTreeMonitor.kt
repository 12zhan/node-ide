package com.example.node_edit.service

class FileTreeMonitor: BaseService() {
    override val name: String = "FileTreeMonitor"
    override val description: String = "用于监听项目打开的文件树目录中，某个文件出现的增删"
    override fun start() {}
    override fun stop() {
    }
}