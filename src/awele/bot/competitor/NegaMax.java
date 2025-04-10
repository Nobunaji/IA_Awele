package awele.bot.competitor;

import awele.bot.CompetitorBot;
import awele.core.Board;
import awele.core.InvalidBotException;

import java.util.Arrays;
import java.util.HashMap;

public class NegaMax extends CompetitorBot {
  private static final int MAX_DEPTH = 10;
  private double[] decision;

  private static double LOW_FACTOR = 0.05;
  private static double MEDIUM_FACTOR = 0.1;
  private static double HIGH_FACTOR = 0.2;
  private static double EXTREME_FACTOR = 0.5;


  /**
   * @brief Constructeur
   * @throws InvalidBotException
   */
  public NegaMax() throws InvalidBotException {
    this.setBotName("NegaMax");
    this.addAuthor("Nobunaji");
  }

  @Override
  public void initialize() {} // Nothing to do

  @Override
  public void finish() {} // Nothing to do

  @Override
  public double[] getDecision(Board board) {
    double alpha = Double.NEGATIVE_INFINITY;
    double beta = Double.POSITIVE_INFINITY;

    this.decision = new double[Board.NB_HOLES];

    boolean[] validMoves = board.validMoves(board.getCurrentPlayer());

    for (int i = 0; i < Board.NB_HOLES; i++) {
      if (validMoves[i]) {
        try {
          double [] sim = new double [Board.NB_HOLES];
          sim[i] = 1;
          this.decision[i] = -negamax(board.playMoveSimulationBoard(board.getCurrentPlayer(), sim), 1, -beta, -alpha);
        }
        catch(InvalidBotException ignored) {}
      }
    }

    return this.decision;
  }

  private double negamax(Board board, int depth, double alpha, double beta) {
    if (depth == MAX_DEPTH || board.getNbSeeds() <= 6) {
      return evaluate(board);
    }

    double maxEval = Double.NEGATIVE_INFINITY;
    boolean[] validMoves = board.validMoves(board.getCurrentPlayer());

    for (int i = 0; i < Board.NB_HOLES; i++) {
      if (validMoves[i]) {
        try {
          double[] sim = new double[Board.NB_HOLES];
          sim[i] = 1;
          Board copy = board.playMoveSimulationBoard(board.getCurrentPlayer(), sim);
          double eval = -negamax(copy, depth + 1, -beta, -alpha);
          maxEval = Math.max(maxEval, eval);

          // Alpha-beta pruning
          alpha = Math.max(alpha, eval);
          if (alpha >= beta) {
            break; // Beta cut-off
          }
        } catch (InvalidBotException ignored) {}
      }
    }

    return maxEval;
  }

  private double[] createMoveArray(int move) {
    double[] moveArray = new double[Board.NB_HOLES];
    moveArray[move] = 1.0;
    return moveArray;
  }

  private double evaluate(Board board) {

    int currentPlayer = board.getCurrentPlayer();
    int opponentPlayer = Board.otherPlayer(currentPlayer);

    // Basic score difference
    double score = board.getScore(currentPlayer) - board.getScore(opponentPlayer);

    // Heuristic: Seed Repartition Between Players
    // This heuristic favors having more seeds in the player's holes compared to the opponent's holes.
    // The weight of 0.1 is chosen to give moderate importance to this factor.
    int playerSeeds = 0;
    int opponentSeeds = 0;
    for (int i = 0; i < Board.NB_HOLES; i++) {
      playerSeeds += board.getPlayerHoles()[i];
      opponentSeeds += board.getOpponentHoles()[i];
    }
    score += (playerSeeds - opponentSeeds) * MEDIUM_FACTOR;

    // This heuristic evaluates the potential for capturing opponent's seeds in the next move.
    // The weight of 0.2 is chosen to give higher importance to potential captures.
    int potentialCaptures = 0;
    for (int i = 0; i < Board.NB_HOLES; i++) {
      if (board.getPlayerHoles()[i] == 1) {
        potentialCaptures += board.getOpponentHoles()[(i+1) % Board.NB_HOLES];
      }
    }
    score += potentialCaptures * HIGH_FACTOR; // Weight the potential captures

    // Heuristic : Seed Distribution*
    // This heuristic favors moves that distribute seeds evenly across the player's holes.
    // The weight of 0.05 is chosen to give lower importance to seed distribution.
    int seedDistribution = 0;
    for (int i = 0; i < Board.NB_HOLES; i++) {
      seedDistribution += Math.abs(board.getPlayerHoles()[i] - board.getOpponentHoles()[i]);
    }
    score -= seedDistribution * LOW_FACTOR; // Weight the seed distribution

    // Heuristic : Endgame Considerations
    // This heuristic adjusts the evaluation based on the number of seeds left, favoring moves that lead to a favorable endgame.
    // The weight of 0.5 is chosen to give significant importance to endgame considerations.
    if (board.getNbSeeds() <= 12) {
      score += (board.getScore(currentPlayer) - board.getScore(opponentPlayer)) * EXTREME_FACTOR;
    }

    return score;
  }

  @Override
  public void learn() {} // Rien à apprendre
}