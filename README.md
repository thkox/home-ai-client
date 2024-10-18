# Home AI Client

<p align="center">
  <img src="./images/app_logo-cropped.png" alt="app logo" width="150" height="150">
</p>

**Home AI Client** is an Android application that allows users to interact with Large Language Models (LLMs) locally, ensuring data privacy and security by avoiding cloud services. This repository contains the Android client code, which communicates with the [Home AI server](https://github.com/thkox/home-ai-server/) to process user queries and files.

## Project Overview

This project is developed as part of a Bachelor's thesis for the 2024 academic year. The goal is to build a client application that interacts with a local server running LLMs, ensuring data processing is done locally to preserve privacy. Both the **thesis** and a **video presentation** of the project are available in this repository.


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

## Thesis and Video Presentation

Both the **thesis** and the **video presentation** explaining the project in detail are available within this repository:
- [Thesis](./docs/Thesis.pdf)
- [Video Presentation](./video/Presentation.mp4)

## Future Enhancements

- Support for multi-modal responses (images, videos).
- Enhanced document processing capabilities.
- Personalization options based on user preferences.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.
