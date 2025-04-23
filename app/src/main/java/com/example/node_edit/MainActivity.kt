@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.node_edit

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.node_edit.ui.component.TextIconButton
import com.example.node_edit.ui.theme.NodeeditTheme
import com.example.node_edit.util.getRealPathFromUri
import com.example.node_edit.util.openFolderSelector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NodeeditTheme {
                @OptIn(ExperimentalMaterial3Api::class)
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopAppBar(title = { Text("NodeIDE") }) }
                ) { innerPadding ->
                    MainContent(innerPadding)
                }
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        caller: ComponentCaller
    ) {
        super.onActivityResult(requestCode, resultCode, data, caller)
    }

}

@Preview
@Composable
fun MainContent(padding:PaddingValues = PaddingValues(0.dp)){

    var context =  LocalContext.current

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text("开始使用")
        Text("开始创建你的惊人项目", fontSize = 12.sp, color = Color(0xff808080))
        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .width(150.dp)
        ) {
            TextIconButton(modifier = Modifier.fillMaxWidth(), icon = Icons.Filled.Add, onClick = {
                context.startActivity(Intent(context,Editor::class.java))
            }) {
                Text("创建项目")
            }

            TextIconButton(modifier = Modifier.fillMaxWidth(),icon = ImageVector.vectorResource(R.drawable.folder), onClick = {
                openFolderSelector(context,999,{})
            }) {
                Text("打开现有项目")
            }

            TextIconButton(modifier = Modifier.fillMaxWidth(),icon = Icons.Filled.Settings, onClick = {
                //跳转到设置里面
                context.startActivity(Intent(context,Setting::class.java))
            }) {
                Text("配置")
            }
        }
    }
}