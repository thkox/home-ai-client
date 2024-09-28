package com.thkox.homeai.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thkox.homeai.R
import com.thkox.homeai.presentation.model.Message

@Composable
fun SenderNameAndTimestamp(
    isSenderMe: Boolean,
    senderName: String,
    timestamp: String
) {

    val horizontalArrangement = if (isSenderMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = horizontalArrangement
    ) {
        if (!isSenderMe) {
            Text(
                text = senderName,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .alignBy(LastBaseline)
                    .paddingFrom(LastBaseline, after = 8.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MessageDisplay(
    message: Message,
) {

    Text(
        text = message.text,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
    )
}


@Composable
fun MessageBubble(
    message: Message,
    isSenderMe: Boolean,
) {
    val backgroundBubbleColor = if (isSenderMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val shape = if (isSenderMe) {
        RoundedCornerShape(30.dp, 5.dp, 30.dp, 30.dp)
    } else {
        RoundedCornerShape(5.dp, 30.dp, 30.dp, 30.dp)
    }

    val horizontalArrangement = if (isSenderMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    val paddingModifier = if (isSenderMe) {
        Modifier.padding(start = 25.dp, end = 0.dp)
    } else {
        Modifier.padding(start = 0.dp, end = 35.dp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(paddingModifier),
        horizontalArrangement = horizontalArrangement
    ) {
        Surface(
            color = backgroundBubbleColor,
            shape = shape
        ) {
            MessageDisplay(
                message = message
            )
        }
    }
}

@Composable
fun SenderAndTextMessage(
    modifier: Modifier = Modifier,
    message: Message,
    isSenderMe: Boolean,
) {

    val horizontalArrangement = if (isSenderMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Column {
            SenderNameAndTimestamp(
                isSenderMe = isSenderMe,
                senderName = message.sender,
                timestamp = message.timestamp
            )
            MessageBubble(
                message = message,
                isSenderMe = isSenderMe,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Message(
    message: Message,
    isSenderMe: Boolean,
) {
    val spaceBetweenSenders = Modifier

    Row(modifier = spaceBetweenSenders) {
        if (!isSenderMe ) {
            Image(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .align(Alignment.Top),
                painter = painterResource(id = R.drawable.ai_icon),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            Spacer(modifier = Modifier.width(74.dp))
        }
        SenderAndTextMessage(
            message = message,
            isSenderMe = isSenderMe,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MessageFromUserPreview() {
    Message(
        message = Message(
            sender = "Home AI",
            text = "This is a message.",
            timestamp = "15:02 PM"
        ),
        isSenderMe = false,
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun MessageFromMePreview() {
    Message(
        message = Message(
            sender = "First Last",
            text = "This is another message.",
            timestamp = "16:52 PM"
        ),
        isSenderMe = true,
    )
}



