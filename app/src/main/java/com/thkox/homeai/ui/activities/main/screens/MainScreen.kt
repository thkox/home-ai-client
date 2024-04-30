package com.thkox.homeai.ui.activities.main.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.thkox.homeai.ui.components.MessageBubble
import com.thkox.homeai.data.models.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Main Screen") },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                           imageVector = Icons.Default.Settings,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior)
        }
    ) {values ->
        Column (
            modifier = Modifier.padding(values)
        ) {
            MessageBubble(Message("Mario", false))
            MessageBubble(Message("Peach", true))
        }
    }
}