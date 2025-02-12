# JavaFX Chat Application

This is a simple JavaFX chat application that allows users to send and receive messages in real-time. The application consists of a client and a server, both implemented using JavaFX and Java sockets.

## Features

- Real-time messaging between clients.
- Simple and intuitive user interface.
- Styled with CSS for a modern look.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 11 or later.
- Maven build tool.

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/basheer421/java-chat.git
   cd java-chat/demo
   ```

2. Build the project using Maven:
   ```sh
   mvn clean install
   ```

### Running the Application

1. Start the chat server:
   ```sh
   mvn clean javafx:run@run-server
   ```

2. Run the chat client:
   ```sh
   mvn clean javafx:run
   ```
