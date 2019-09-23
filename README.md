# Chat Room
Complete the chat room application implementation using WebSocket.

## Background
WebSocket is a communication protocol that makes it possible to establish a two-way communication channel between a
server and a client.

## Instruction
### Implement the message model
Message model is the message payload that will be exchanged between the client and the server. Implement the Message
class in chat module. Make sure you cover all there basic actions.
1. ENTER
2. CHAT
3. LEAVE

### Complete WebSocketChatServer
Implement all TODOs inside WebSocketChatServer follow each method description.

### Run the application with command
mvn build; mvn spring-boot:run

Please take a look at the following instructions for the project:

File: src/main/java/edu/udacity/java/nano/WebSocketChatApplication.java: SprintBoot main file. No need to update.

File src/main/java/edu/udacity/java/nano/Message.java: message model. It needs to be implemented. Please reference the message model we had in websocket lesson.

File src/main/java/edu/udacity/java/nano/WebSocketChatServer.java: controller. It needs to be implemented.

onOpen: add on open connection to establish the connection.

onMessage: 1) Get username and session. 2) Send message to all.

sendMessageToAll: send messages to all receivers.

onClose: close the connection.

File src/main/java/edu/udacity/java/nano/WebSocketConfig.java: no change.

File src/main/resources/templates/chat.html

Check online number nums.

Add message container.

Add send button to send message.

File src/main/resources/templates/login.html: complete login function