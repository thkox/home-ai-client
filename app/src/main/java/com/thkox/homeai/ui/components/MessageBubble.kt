package com.thkox.homeai.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thkox.homeai.data.models.Message

@Composable
fun MessageBubble(
    message: Message,
    backgroundColor: Color = if (message.isSender) Color.LightGray else Color.Cyan,
    contentColor: Color = Color.Black,
    cornerRadius: Float = 16f,
    horizontalPadding: Int = 16,
    verticalPadding: Int = 8
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = if (message.isSender) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                        topStart = cornerRadius.dp,
                        topEnd = cornerRadius.dp,
                        bottomStart = if (message.isSender) cornerRadius.dp else 0.dp,
                        bottomEnd = if (message.isSender) 0.dp else cornerRadius.dp),
            color = backgroundColor
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(
                    start = horizontalPadding.dp,
                    top = verticalPadding.dp,
                    end = horizontalPadding.dp,
                    bottom = verticalPadding.dp
                ),
                color = contentColor
            )
        }
    }
}



