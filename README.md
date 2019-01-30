# Go

<h3>Installation instructions</h3>

To install this application, in addition to cloning or downloading this repository, you also need to download the      following GUI repository: https://github.com/nedap/university-goGame.
 


Once both applications are downloaded, they need to be linked to ensure proper functioning of this go game (in Eclipse this is possible by navigating to the project properties -> Java Build Path -> Projects, then click ‘Add…’ and add the GUI repository to this repository).

<h3>Gameplay</h3>

This repository supports online and offline gameplay. To play offline the go.java file in the package ‘go’ can be executed.

To host a server execute the server.java file. The TUI will ask you for a port number. 

To connect as client with a server, execute the client.java file. The TUI will ask you to provide a username, the hostname of the server and the port you want to connect to. 

You can let the computer play for you by providing the name 'ComputerPlayer' to the TUI when you try to connect as client. 

