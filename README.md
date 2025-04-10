# Awalé AI Project

This project implements an AI-based system to play the traditional African board game **Awalé**. The project includes various bots that use different algorithms to make decisions during gameplay. The main focus is on creating competitive bots that can participate in tournaments and evaluate their performance against other bots.

## Project Structure

The project is organized as follows:

## Key Components

### Core Game Logic
- **[`Board`](src/awele/core/Board.java)**: Represents the state of the game board and handles game mechanics.
- **[`Awele`](src/awele/core/Awele.java)**: Manages a game between two players, including scoring and game flow.
- **[`InvalidBotException`](src/awele/core/InvalidBotException.java)**: Custom exception for invalid bot behavior.

### AI Bots
The project includes several bots with different strategies:
- **[`RandomBot`](src/awele/bot/demo/random/RandomBot.java)**: Plays moves randomly.
- **[`FirstBot`](src/awele/bot/demo/first/FirstBot.java)**: Always plays the leftmost valid move.
- **[`LastBot`](src/awele/bot/demo/last/LastBot.java)**: Always plays the rightmost valid move.
- **[`MinMaxBot`](src/awele/bot/demo/minmax/MinMaxBot.java)**: Uses the MinMax algorithm with alpha-beta pruning.
- **[`Knn1Bot`](src/awele/bot/demo/knn1/Knn1Bot.java)**: Uses a k-NN algorithm based on winning moves.
- **[`Knn2Bot`](src/awele/bot/demo/knn2/Knn2Bot.java)**: Uses a k-NN algorithm with additional data for both winning and losing moves.
- **[`NegaMax`](src/awele/bot/competitor/NegaMax.java)**: Implements the NegaMax algorithm with heuristics for advanced decision-making.

### Output and Logging
- **[`StandardOutput`](src/awele/output/StandardOutput.java)**: Outputs game information to the console.
- **[`LogFileOutput`](src/awele/output/LogFileOutput.java)**: Logs game results to a file.
- **[`OutputWriter`](src/awele/output/OutputWriter.java)**: Manages multiple output streams.

### Data Handling
- **[`AweleData`](src/awele/data/AweleData.java)**: Loads and manages game data for training.
- **[`AweleObservation`](src/awele/data/AweleObservation.java)**: Represents a single game observation.

### Main Entry Point
- **[`Main`](src/awele/run/Main.java)**: The main program that initializes bots, runs tournaments, and logs results.

## How to Run

1. **Setup**:
   - Ensure you have Java installed (JDK 22 or higher).
   - Add the required libraries (`javassist-3.21.0-GA.jar`, `reflections-0.9.12.jar`) to the `lib/` directory.

2. **Compile**:
   Use your preferred IDE (e.g., IntelliJ IDEA, VS Code) or compile via the command line:
   ```sh
   javac -cp "lib/*" -d out src/awele/run/Main.java
    ```

3. **Run**:
   ```sh
   java -cp "lib/*:out" awele.run.Main
   ```

## Features

- **Tournament Mode**: Bots compete against each other in a round-robin tournament.
- **Customizable Bots**: Extend the CompetitorBot class to create your own bot.
- **Performance Metrics**: Logs include decision times, memory usage, and scores for detailed analysis.

## Adding a New Bot

1. Create a new class in the src/awele/bot/competitor/ directory.
2. Extend the CompetitorBot class.
3. Implement the required methods:
     - initialize()
     - learn()
     - getDecision(Board board)
     - finish()

4. Add your bot to the tournament in Main.

## Authors

- **Alexandre Blansché**: Original project and framework.
- **Nobunaji**: Author of the NegaMax bot.

## License

This project is for educational purposes and is not licensed for commercial use.
