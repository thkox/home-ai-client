package com.thkox.homeai.presentation.ui.activities.main.screens

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thkox.homeai.R
import com.thkox.homeai.presentation.ui.components.MainTopAppBar
import com.thkox.homeai.presentation.ui.theme.HomeAITheme

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    navigateToMain: () -> Unit
) {
    AboutContent(
        modifier = modifier,
        navigateToMain = navigateToMain
    )
}


@Composable
fun AboutContent(
    modifier: Modifier = Modifier,
    navigateToMain: (() -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current
    val githubHandle = "thkox"
    val githubUrl = "https://github.com/$githubHandle"

    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.created_by))
        pushStringAnnotation(tag = "URL", annotation = githubUrl)
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(githubHandle)
        }
        pop()
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                text = stringResource(R.string.about),
                isSecondScreen = true,
                onClickNavigationIcon = navigateToMain ?: {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            item {
                Text(
                    text = "About Home AI",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Text(
                    text = stringResource(R.string.home_ai_is_a_privacy_focused_application_designed_to_provide_users_with_personalized_interactions_using_large_language_models_llms_directly_from_their_mobile_devices_by_leveraging_the_computing_power_of_a_local_computer_home_ai_ensures_your_data_stays_secure_and_private_without_relying_on_cloud_based_services),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.with_home_ai_you_can_ask_questions_receive_tailored_responses_and_even_add_your_own_files_to_create_a_more_customized_experience_the_app_seamlessly_connects_your_android_device_with_a_backend_running_on_a_local_server_ensuring_fast_and_secure_communication),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.key_features),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(stringResource(R.string.private_conversations_annotation))
                        }
                        append(stringResource(R.string.all_interactions_take_place_locally_on_your_own_infrastructure_ensuring_maximum_privacy))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(stringResource(R.string.multiple_input_methods))
                        }
                        append(stringResource(R.string.you_can_interact_with_home_ai_using_either_voice_commands_through_the_microphone_or_text_via_the_keyboard))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(stringResource(R.string.personalization))
                        }
                        append(stringResource(R.string.upload_your_own_files_to_receive_responses_tailored_to_your_specific_needs))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(stringResource(R.string.user_management))
                        }
                        append(stringResource(R.string.create_an_account_log_in_and_update_your_profile_details_anytime_you_can_also_view_continue_or_delete_previous_conversations))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                        ) {
                            append(stringResource(R.string.seamless_connectivity))
                        }
                        append(stringResource(R.string.the_backend_is_powered_by_fastapi_postgres_chromadb_and_integrates_advanced_llm_tools_via_ollama_and_langchain_for_intelligent_responses))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append(stringResource(R.string.complete_control))
                        }
                        append(stringResource(R.string.modify_or_delete_your_data_whenever_you_want_ensuring_your_information_is_always_under_your_control))
                    },
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.home_ai_brings_the_power_of_llms_to_your_fingertips_all_while_prioritizing_your_data_privacy_and_security),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = annotatedString,
                        modifier = Modifier.clickable {
                            annotatedString
                                .getStringAnnotations(
                                    tag = "URL",
                                    start = 0,
                                    end = annotatedString.length
                                )
                                .firstOrNull()
                                ?.let { annotation ->
                                    uriHandler.openUri(annotation.item)
                                }
                        },
                        style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }

}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun AboutScreenLightPreview() {
    HomeAITheme {
        AboutContent()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AboutScreenDarkPreview() {
    HomeAITheme {
        AboutContent()
    }
}