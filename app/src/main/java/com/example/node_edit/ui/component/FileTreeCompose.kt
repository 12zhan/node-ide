package com.example.node_edit.ui.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.node_edit.R

enum class ResourceType {
    FILE, DIRECTORY
}

sealed class FileNode(open val name: String) {
    data class Directory(
        override val name: String,
        val children: List<FileNode> = emptyList(),
        val isExpanded: Boolean = false
    ) : FileNode(name)

    data class File(override val name: String) : FileNode(name)
}

object FileIconController {
    private val baseIconMap = mapOf(
        "default_file" to R.drawable.basefolder,
        ".js" to R.drawable.javascript,
        ".html" to R.drawable.html
    )

    private val defaultFolder = R.drawable.folder

    fun getResourceIcon(suffix: String, type: ResourceType): Int {
        return when (type) {
            ResourceType.FILE -> baseIconMap[suffix] ?: baseIconMap["default_file"]!!
            ResourceType.DIRECTORY -> defaultFolder
        }
    }
}

@Composable
fun FileTreeComposeChild(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier
) {

    Box(modifier){
        Column(modifier.fillMaxWidth()) {
                // 使用 key 提高列表稳定性
                nodes.map { node->
                    when (node) {
                        is FileNode.Directory -> DirectoryCompose(
                            directory = node
                        )
                        is FileNode.File -> FileCompose(name = node.name)
                    }
                }
            }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
private fun DirectoryCompose(
    directory: FileNode.Directory
) {
    val isExpanded = remember { mutableStateOf(directory.isExpanded) }

    val iconRes = FileIconController.getResourceIcon(directory.name, ResourceType.DIRECTORY)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(horizontal = 12.dp)
                .clip(CircleShape)
                .clickable {
                    isExpanded.value = !isExpanded.value
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val rotation by animateFloatAsState(
                targetValue = if (isExpanded.value) 90f else 0f,
                animationSpec = tween(durationMillis = 300),
                label = "文件夹展开指标旋转动画"
            )

            Icon(
                painter = painterResource(id = R.drawable.ic_arrow),
                contentDescription = "Expand/Collapse",
                modifier = Modifier.size(20.dp).rotate(rotation),
            )

            AsyncImage(
                modifier = Modifier.size(28.dp),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(iconRes)
                    .build(),
                contentDescription = directory.name,
                placeholder = painterResource(R.drawable.basefile),
            )

            Text(
                text = directory.name,
                modifier = Modifier.weight(1f)
            )
        }
        if(isExpanded.value) Box(modifier = Modifier.padding(start = 10.dp)){ FileTreeComposeChild(directory.children) }


}

@Composable
private fun FileCompose(name: String) {
    val suffix = remember(name) {
        name.substringAfterLast('.', "").let {
            if (it.isNotEmpty()) ".$it" else "default_file"
        }
    }
    val iconRes = remember(suffix) {
        FileIconController.getResourceIcon(suffix, ResourceType.FILE)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .clickable { /* Handle file click */ },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.size(20.dp))

        AsyncImage(
            modifier = Modifier.size(28.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconRes)
                .build(),
            contentDescription = name,
            placeholder = painterResource(R.drawable.basefile),
        )

        Text(
            text = name,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FileTreeCompose(
    nodes: List<FileNode>,
    modifier: Modifier = Modifier
){

    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }


    val dragGesture = remember {
        Modifier.pointerInput(Unit){
            detectDragGestures { change, dragAmount ->
                change.consume()
                offsetX += dragAmount.x
                offsetY += dragAmount.y
            }
        }
    }

    Box(
        modifier=
        modifier.clipToBounds()
            .then(dragGesture)
    ){
        Box(
            modifier.offset {
                IntOffset(offsetX.toInt(),offsetY.toInt())
            }
        ){
            FileTreeComposeChild(nodes = nodes,modifier = modifier)
        }

    }

}

@Preview(showBackground = true)
@Composable
fun FileTreePreview() {
    val sampleNodes = listOf(
        FileNode.Directory(
            name = "src",
            children = listOf(
                FileNode.File("main.js"),
                FileNode.File("utils.html"),
                FileNode.Directory(
                    isExpanded = true,
                    name = "components",
                    children = listOf(
                        FileNode.File("Button.js"),
                        FileNode.File("Header.html")
                    )
                )
            )
        ),
        FileNode.File("package.json")
    )

    FileTreeComposeChild(
        nodes = sampleNodes,
        modifier = Modifier
            .fillMaxSize()
    )
}