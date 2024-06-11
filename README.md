# Device Verification Bot

A Telegram bot that verifies users based on their device model before allowing them to join a chat group.

## Features

- Asks users what device they are using when they request to join the chat.
- Approves the join request if the user's response contains the word "pixel".
- Rejects the join request otherwise.

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Gradle build tool
- A Telegram bot token from [@BotFather](https://t.me/BotFather)

## Getting Started

### Clone the Repository

Clone this repository with the following command:

```
git clone https://github.com/Mahmud0808/TelegramJoinRequestVerifier.git
cd TelegramJoinRequestVerifier
```

### Build the Project

This project includes a GitHub Actions workflow for continuous integration. The workflow will build the project whenever code is pushed to the main branch or a pull request is made to the main branch.

If you want to build locally, ensure you have Gradle installed and build the project with the following command:

```
./gradlew build
```

### Running the Bot

You can run the bot locally using:

```
java -jar joinreqbot.jar YOUR_BOT_TOKEN
```

Replace YOUR_BOT_TOKEN with the actual token provided by BotFather.

### Usage
Enable `Approve new members` from your telegram group settings. Add the bot you created using BotFather as an admin. When a user requests to join the chat, the bot will send them a message asking about their device. If the user mentions "pixel" in their response, they will be approved to join the chat. Otherwise, their join request will be declined.

## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any bugs or enhancements.

## Acknowledgments

- [Telegram](https://core.telegram.org/bots/api) for bot API library.
- [ksugmbot](https://github.com/Kernel-SU/ksugmbot) for upstream.
