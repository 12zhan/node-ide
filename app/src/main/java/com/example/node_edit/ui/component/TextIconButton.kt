package com.example.node_edit.ui.component

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun TextIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    onClick:()->Unit,
    content: @Composable RowScope.() -> Unit
){
    TextButton(modifier = modifier, onClick = onClick) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            content()
        }
    }
}