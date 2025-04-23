package com.example.node_edit

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.node_edit.State.CodeEditorState
import com.example.node_edit.ui.component.FileNode
import com.example.node_edit.ui.component.FileTreeCompose
import com.example.node_edit.ui.theme.NodeeditTheme
import io.github.rosemoe.sora.widget.CodeEditor

class Editor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NodeeditTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { p->

                    val drawerState = rememberDrawerState(DrawerValue.Open)
                    ModalNavigationDrawer(
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet{
                                val sampleNodes = listOf(
                                FileNode.Directory(
                                    name = "src",
                                    children = listOf(
                                        FileNode.Directory(
                                            name = "components",
                                            children = listOf(
                                                FileNode.Directory(
                                                    name = "Button",
                                                    children = listOf(
                                                        FileNode.File("Button.js"),
                                                        FileNode.File("Button.module.css"),
                                                        FileNode.File("Button.test.js"),
                                                        FileNode.File("README.md")
                                                    ),
                                                    isExpanded = true
                                                ),
                                                FileNode.Directory(
                                                    name = "Header",
                                                    children = listOf(
                                                        FileNode.File("Header.js"),
                                                        FileNode.File("Header.module.css"),
                                                        FileNode.File("Header.test.js"),
                                                        FileNode.File("README.md")
                                                    ),
                                                    isExpanded = true
                                                ),
                                                FileNode.File("index.js")
                                            ),
                                            isExpanded = true
                                        ),
                                        FileNode.Directory(
                                            name = "utils",
                                            children = listOf(
                                                FileNode.File("helpers.js"),
                                                FileNode.File("constants.js"),
                                                FileNode.File("config.js"),
                                                FileNode.File("index.js")
                                            ),
                                            isExpanded = true
                                        ),
                                        FileNode.Directory(
                                            name = "config",
                                            children = listOf(
                                                FileNode.File("app.config.js"),
                                                FileNode.File("env.config.js"),
                                                FileNode.File("index.js")
                                            ),
                                            isExpanded = true
                                        ),
                                        FileNode.File("main.js"),
                                        FileNode.File("index.js"),
                                        FileNode.File("app.js")
                                    ),
                                    isExpanded = true
                                ),
                                FileNode.Directory(
                                    name = "public",
                                    children = listOf(
                                        FileNode.File("favicon.ico"),
                                        FileNode.File("index.html"),
                                        FileNode.Directory(
                                            name = "assets",
                                            children = listOf(
                                                FileNode.File("logo.png"),
                                                FileNode.File("background.jpg"),
                                                FileNode.File("fonts.css")
                                            ),
                                            isExpanded = true
                                        )
                                    ),
                                    isExpanded = true
                                ),
                                FileNode.Directory(
                                    name = "tests",
                                    children = listOf(
                                        FileNode.File("setupTests.js"),
                                        FileNode.Directory(
                                            name = "integration",
                                            children = listOf(
                                                FileNode.File("app.test.js"),
                                                FileNode.File("components.test.js")
                                            ),
                                            isExpanded = true
                                        ),
                                        FileNode.Directory(
                                            name = "unit",
                                            children = listOf(
                                                FileNode.File("Button.test.js"),
                                                FileNode.File("Header.test.js"),
                                                FileNode.File("utils.test.js")
                                            ),
                                            isExpanded = true
                                        )
                                    ),
                                    isExpanded = true
                                ),
                                FileNode.File("package.json"),
                                FileNode.File("README.md"),
                                FileNode.File(".gitignore"),
                                FileNode.File("tsconfig.json")
                            )

                                FileTreeCompose(
                                    nodes = sampleNodes,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    ){
                        CodeEditor(modifier = Modifier.padding(p).fillMaxSize(),state = CodeEditorState())
                    }

                }
            }
        }
    }
}

@Composable
fun CodeEditor(
    modifier: Modifier = Modifier,
    state: CodeEditorState
) {
    val context = LocalContext.current
    val edit = remember {
        setCodeEditorFactory(context,state)
    }
    AndroidView(factory = { edit }, modifier = modifier, onRelease = {it.release()})
}

private fun setCodeEditorFactory(
    context: Context,
    state: CodeEditorState
): CodeEditor {
    val editor = CodeEditor(context)
    editor.apply {
        setText(state.content)
        // ...
    }
    state.editor = editor
    return editor
}