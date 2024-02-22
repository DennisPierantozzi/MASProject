## Getting Started

This project is part of the course of Multiagent System (Master in Artificial Intelligence at UDC).
Authors of the code:
- Dennis Pierantozzi
- Francesco Maria Mosca

## Introduction
The program implements 3 agents that use different policies to play a TileWorld based game. 
The report file contains the game and code explanation.

## Folder Structure

The workspace contains two folders, where:

- `src`: the folder with all the source code
- `bin`: the folder contains the compiled classes

## Run the program

### Run the main class
To run the main class you need to open the project in your favorite IDE and run the main.java file.
In the code you can specify the commitment for every agent, adding more or delete somes.

### Run from command line
To run the program from command line you can use this command.
java --enable-preview -cp "bin;your_path_to_jade\\jade.jar" jade.Boot -gui -agents simulator:SimulatorAgent random:RandomAgent(10)

This command specify a simulator agent and a random agent with commitment of 10.
*Remember*: you always need to specify a simulator agent to run correctly the simulations.

### Run the script file
In the directory of the project you can find the DemoToGo.bat file.
This script run the same command as specified above and the same simulation described will start.