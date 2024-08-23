package com.thkox.homeai.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.thkox.homeai.presentation.model.Message

@Composable
fun AuthorNameAndTimestamp(
    isAuthorMe: Boolean,
    authorName: String,
    timestamp: String
) {

    val horizontalArrangement = if (isAuthorMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row (
        modifier = Modifier.fillMaxWidth()
            .semantics(mergeDescendants = true) {},
        horizontalArrangement = horizontalArrangement
    ) {
        Text(
            text = authorName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = timestamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}



@Composable
fun ClickableMessage(
    modifier: Modifier = Modifier,
    message: Message,
    isAuthorMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Text(
        text = message.text,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        // on click
    )
}


@Composable
fun MessageBubble(
    message: Message,
    isAuthorMe: Boolean,
    authorClicked: (String) -> Unit
) {
    val backgroundBubbleColor = if (isAuthorMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    val shape = if(isAuthorMe) {
        RoundedCornerShape(20.dp, 4.dp, 20.dp, 20.dp)
    } else {
        RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)
    }

    val horizontalArrangement = if (isAuthorMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        Surface(
            color = backgroundBubbleColor,
            shape = shape
        ) {
            ClickableMessage(
                message = message,
                isAuthorMe = isAuthorMe,
                authorClicked = authorClicked
            )
        }

        // If there is an image, show it
        message.image?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = backgroundBubbleColor,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,  //painterResource(it),
                    modifier = Modifier.size(160.dp),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AuthorAndTextMessage(
    modifier: Modifier = Modifier,
    message: Message,
    isAuthorMe: Boolean,
    isFirstMessageByAuthor: Boolean, // for visual purposes to separate authors
    isLastMessageByAuthor: Boolean, // for visual purposes to separate authors
    authorClicked: (String) -> Unit, // for navigation
) {

    val horizontalArrangement = if (isAuthorMe) {
        Arrangement.End
    } else {
        Arrangement.Start
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        Column(
        ) {
            if (isLastMessageByAuthor) {
                AuthorNameAndTimestamp(
                    isAuthorMe = isAuthorMe,
                    authorName = message.author,
                    timestamp = message.timestamp
                )
            }
            MessageBubble(
                message = message,
                isAuthorMe = isAuthorMe,
                authorClicked = authorClicked
            )
            if (isFirstMessageByAuthor) {
                Spacer(modifier = Modifier.height(8.dp)) // Space between authors
            } else {
                Spacer(modifier = Modifier.height(4.dp)) // Space between messages
            }
        }
    }
}

@Composable
fun Message(
    onAuthorClick: (String) -> Unit,
    message: Message,
    isAuthorMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
) {
    val borderColor = if (isAuthorMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.tertiary
    }

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier

    Row(modifier = spaceBetweenAuthors) {
        if (!isAuthorMe && isLastMessageByAuthor) {
            // Avatar for non-author users only when they are the last author
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClick(message.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(3.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top),
                imageVector = Icons.Default.Build,//painterResource(id = message.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar for author or non-last non-author messages
            Spacer(modifier = Modifier.width(74.dp))
        }
        AuthorAndTextMessage(
            message = message,
            isAuthorMe = isAuthorMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            authorClicked = onAuthorClick,
            modifier = Modifier
                .padding(end = 16.dp) // Maintain end padding
                .weight(1f)
        )
    }
}


@Preview(
    showBackground = true
)
@Composable
private fun AuthorNameAndTimestampPreview() {
    AuthorNameAndTimestamp(
        isAuthorMe = true,
        authorName = "Author Name",
        timestamp = "15:02 PM"
    )
}


@Preview
@Composable
private fun ClickableMessagePreview() {
    ClickableMessage(
        message = Message(
            author = "Author Name",
            text = "This is a message",
            timestamp = "15:02 PM"
        ),
        isAuthorMe = false,
        authorClicked = {}
    )
}

@Preview
@Composable
private fun MessageBubblePreview() {
    MessageBubble(
        message = Message(
            author = "Author Name",
            text = "This is a message",
            timestamp = "15:02 PM"
        ),
        isAuthorMe = true,
        authorClicked = {}
    )
}

@Preview (
    showBackground = true
)
@Composable
private fun MessageFromUserPreview() {
    Message(
        message = Message(
            author = "Author Name",
            text = "This is a message.",
            timestamp = "15:02 PM"
        ),
        isAuthorMe = false,
        isFirstMessageByAuthor = false,
        isLastMessageByAuthor = true,
        onAuthorClick = {}
    )
}

@Preview (
    showBackground = true
)
@Composable
private fun MessageFromMePreview() {
    Message(
        message = Message(
            author = "Author Name",
            text = "This is another message.",
            timestamp = "16:52 PM"
        ),
        isAuthorMe = true,
        isFirstMessageByAuthor = false,
        isLastMessageByAuthor = true,
        onAuthorClick = {}
    )
}



