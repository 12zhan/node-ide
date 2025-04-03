package com.example.node_edit

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.node_edit.ui.theme.NodeeditTheme
import com.example.node_edit.util.NodeVersionType
import com.example.node_edit.util.searchNodeVersion
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val activityName = "设置"

class Setting : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NodeeditTheme {
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState());
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = { settingTopBar(scrollBehavior) }
                ){ p ->
                    Box(modifier = Modifier.padding(p)){
                        Column{
                            baseEnvConfig()
                        }
                    }

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun settingTopBar(scrollBehavior : TopAppBarScrollBehavior){
    TopAppBar(
        title = { Text(activityName) },
        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun baseEnvConfig(){

    var bottomSheetState = remember { mutableStateOf(false) }

    Text("基础环境配置", modifier = Modifier.padding(start = 15.dp),fontSize = 12.sp)

    // node环境
    ListItem(
        headlineContent = { Text("Node环境") },
        supportingContent = { Text("请下载Node运行环境", fontSize = 12.sp, color = Color(0xff808080)) },
        trailingContent = {
            Button(onClick = {
                bottomSheetState.value = true
            }) { Text("配置") }
        }
    )

    // 分割线
    HorizontalDivider()

    NodeDownloadBottomSheet(bottomSheetState)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NodeDownloadBottomSheet(baseState : MutableState<Boolean>){

    val context = LocalContext.current

    if(baseState.value){
        ModalBottomSheet(
            onDismissRequest = {baseState.value = false}
        ) {
            var text by remember { mutableStateOf("") }
            // 使用 mutableStateListOf 来创建一个可变状态列表
            val data: SnapshotStateList<NodeVersionType> = remember { mutableStateListOf() }
            val coroutineScope = rememberCoroutineScope()
            Row (modifier = Modifier
                .padding(horizontal = 10.dp)
                .height(100.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)){
                OutlinedTextField(modifier = Modifier.weight(1f),value = text, onValueChange = {t->text = t}, label = { Text("请输入node版本搜索") }, maxLines = 1, shape = RoundedCornerShape(16.dp))
                Button(modifier = Modifier.width(80.dp),onClick = {
                    //异步使用okhttp请去数据
                    Thread{
                        searchNodeVersion(0).forEach { d->data.add(d) }
                    }.start()

                }) { Text("搜索") }
            }
            LazyColumn{
                items(items = data){d->
                    ListItem(
                        headlineContent = { Text(d.version) },
                        supportingContent = { Text(d.date, fontSize = 12.sp, color = Color(0xff808080)) },
                        trailingContent = {
                            Button(onClick = {
                                Toast.makeText(context, d.download, Toast.LENGTH_SHORT).show()
                            }) { Text("下载") }
                        }
                    )
                }
            }
        }
    }
}