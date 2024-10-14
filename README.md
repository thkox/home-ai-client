# Home AI

**Home AI** is an Android application that allows users to interact with Large Language Models (LLMs) locally, ensuring data privacy and security by avoiding cloud services. This repository contains the Android client code, which communicates with the [Home AI server](https://github.com/thkox/home-ai-server/) to process user queries and files.

## Features

- User authentication with JWT tokens.
- User-friendly interface for interacting with LLMs.
- Local data processing to ensure user privacy.
- Ability to upload and manage documents for enhanced responses.
- Voice-to-text support for inputting queries via the microphone.
- Conversation history management.

## Requirements

- Android device running SDK 33+ (Android 13 or higher).
- Minimum 8 GB RAM.
- At least 300 MB of free storage space.

## Technologies Used

- **Kotlin**: Primary language for Android development.
- **Jetpack Compose**: UI framework for building modern Android UIs.
- **Retrofit**: For sending HTTP requests to the Home AI server.
- **OkHttp**: For managing HTTP connections.
- **Kotlin Coroutines**: For managing asynchronous operations.
- **Dagger Hilt**: Dependency injection framework.

## Setup

1. After launching the app, you will be prompted to input the server's IP address.
   - For example, if your server is hosted locally on IP `192.168.3.98` with port `8000`, input `192.168.3.98:8000`.
2. Once connected, you can register a new user or log in using existing credentials.
3. Start conversations with LLMs, upload documents for analysis, or continue previous conversations.

## Usage

- **Conversations**: Begin new conversations with LLMs, continue past interactions, or delete old conversations.
- **File Upload**: Upload text documents (PDF, DOCX, CSV, etc.) to enhance LLM responses.
- **Voice Input**: Tap the microphone icon to send voice commands that will be transcribed to text.

## Future Enhancements

- Support for multi-modal responses (images, videos).
- Enhanced document processing capabilities.
- Personalization options based on user preferences.
