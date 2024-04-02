package cs410.uno;

import java.util.Objects;
import java.util.Random;

/* Represents the flow of the game, taking use of Player, Deck, and Card.
 *  Automatically terminates the program once the game is over.
 *  For the "standard" Uno experience, the user should input
 *  these numbers for the following values:

 *  countPlayers: 2-10
 *  countInitialCardsPerPlayer = 7
 *  countDigitCardsPerColor = 2
 *  countSpecialCardsPerColor = 2
 *  countWildCards = 8
 */
public class GameState {

    /* After the startGame method ends, the game state should represent the
     * situation immediately before the first player takes their first turn.
     * That is, the players should be arranged, their initial hands have been dealt,
     * and the discard pile and draw pile have been created.
     */

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public Player[] players;
    public Deck playDeck;
    public int playerTurn = 0;
    public int turnCount = 0;
    public boolean firstTurn = true;
    public boolean reverseOrder = false;
    public boolean reverseEffect = false;
    public Random rand = new Random();
    public String currentColor;
    public String firstWildColor;

    /* Static Factory method to handle the arguments.
    */
    public static GameState startGame(int countPlayers,
                                      int countInitialCardsPerPlayer,
                                      int countDigitCardsPerColor,
                                      int countSpecialCardsPerColor,
                                      int countWildCards)
    {
        return new GameState(countPlayers, countInitialCardsPerPlayer, countDigitCardsPerColor, countSpecialCardsPerColor, countWildCards);
    }


    /* Sets the game up by dealing players their cards, simultaneously
     * doing deck management.
     */
    GameState(int countPlayers,
              int countInitialCardsPerPlayer,
              int countDigitCardsPerColor,
              int countSpecialCardsPerColor,
              int countWildCards) {

        this.players = new Player[countPlayers];
        this.playDeck = new Deck();
        this.playDeck.createDeck(countDigitCardsPerColor, countSpecialCardsPerColor, countWildCards);

        for (int i = 0; i < countPlayers; i++)
        {
            this.players[i] = new Player(i);
        }

        for (int i = 0; i < countPlayers; i++)
        {
            this.players[i].getHand(playDeck.unoDeck, countInitialCardsPerPlayer);
        }
    }



    /* Indicates whether the game is over.
     */
    boolean isGameOver(Player winner) {
        return winner.winner();
    }

    /* The current player takes their turn, and if they play a special card
     * the corresponding effects are performed. When the method returns,
     * the next player is ready to take their turn.
     * If the game is already over, this method has no effect, as the program
     * will exit.
     */
    public void runOneTurn() {
        turnCount++;
        if (this.playDeck.unoDeck.isEmpty())
        {
            System.out.println("Draw pile is empty, shuffling discard pile...");
            this.playDeck.shuffleDeck();
            currentColor = playDeck.unoDiscard.peek().color();
            if (Objects.equals(this.playDeck.unoDiscard.peek().type(), "wild"))
            {
                int randcolor = rand.nextInt(4);
                if (randcolor == 0)
                {
                    this.currentColor = "red";
                    firstWildColor = ANSI_RED;
                }
                if (randcolor == 1)
                {
                    this.currentColor = "blue";
                    firstWildColor = ANSI_BLUE;
                }
                if (randcolor == 2)
                {
                    this.currentColor = "green";
                    firstWildColor = ANSI_GREEN;
                }
                if (randcolor == 3)
                {
                    this.currentColor = "yellow";
                    firstWildColor = ANSI_YELLOW;
                }
                System.out.println(firstWildColor + "The first card is a wild card. The color is " + this.currentColor + "." + ANSI_RESET);
                System.out.println("");
            }
            else
            {
                System.out.println("After shuffling, the new first card is a " +
                        playDeck.unoDiscard.peek().color() + " " + playDeck.unoDiscard.peek().type() + ".");
            }
            System.out.println("");
            System.out.println("");
        }
        if (firstTurn)
        {
            System.out.println("Time to play Uno!");
            this.playDeck.firstDiscard();
            if (Objects.equals(this.playDeck.unoDiscard.peek().type(), "wild"))
            {
                int randcolor = rand.nextInt(4);
                if (randcolor == 0)
                {
                    this.currentColor = "red";
                    firstWildColor = ANSI_RED;
                }
                if (randcolor == 1)
                {
                    this.currentColor = "blue";
                    firstWildColor = ANSI_BLUE;
                }
                if (randcolor == 2)
                {
                    this.currentColor = "green";
                    firstWildColor = ANSI_GREEN;
                }
                if (randcolor == 3)
                {
                    this.currentColor = "yellow";
                    firstWildColor = ANSI_YELLOW;
                }
                System.out.println(firstWildColor + "The first card is a wild card. The color is " + this.currentColor + "." + ANSI_RESET);
                System.out.println("");
            }
            else
            {
                System.out.println("The first card is a " + this.playDeck.unoDiscard.peek().color() + " " +
                        this.playDeck.unoDiscard.peek().type() + " card.");
            }
            System.out.println("---------------------------------------------------------------------------------");
            this.firstTurn = false;
        }
        System.out.println("It is now Player " + (this.playerTurn + 1) + "'s turn.");
        System.out.println("Their cards are:");
        players[playerTurn].printCards();

        if (Objects.equals(this.playDeck.unoDiscard.peek().type(), "skip") ||
                Objects.equals(this.playDeck.unoDiscard.peek().type(), "draw2"))
        {
            if (!reverseEffect && playerTurn == 0 && players[players.length - 1].tookTurn)
            {
                players[playerTurn].isSkipped(playDeck.unoDiscard, playDeck.unoDeck);
            }
            else if(!reverseEffect && playerTurn > 0 && players[playerTurn - 1].tookTurn)
            {
                players[playerTurn].isSkipped(playDeck.unoDiscard, playDeck.unoDeck);
            }
            else if (reverseEffect && playerTurn == players.length - 1 && players[0].tookTurn)
            {
                players[playerTurn].isSkipped(playDeck.unoDiscard, playDeck.unoDeck);
            }
            else if (reverseEffect && playerTurn < players.length - 1 && players[playerTurn + 1].tookTurn)
            {
                players[playerTurn].isSkipped(playDeck.unoDiscard, playDeck.unoDeck);
            }
        }
        if (reverseEffect)
        {
            System.out.println("The turn order is currently going in reverse.");
        }
        else
        {
            System.out.println("The turn order is currently normal.");
        }
        System.out.println("");
        this.players[this.playerTurn].playCard(this.playDeck.unoDiscard, this.playDeck.unoDeck, this.currentColor);
        reverseOrder = this.players[this.playerTurn].reversePlayed(this.playDeck.unoDiscard.peek());
        if (this.reverseOrder && players[playerTurn].tookTurn)
        {
            System.out.println("The turn order will now swap!");
            reverseEffect = !reverseEffect;
        }

        if (this.players[this.playerTurn].hasUno())
        {
            System.out.println("Player " + (this.playerTurn + 1) + " calls Uno!");
        }
        if (this.playDeck.unoDiscard.peek().isWild() && players[playerTurn].tookTurn)
        {
            currentColor = this.playDeck.playWild(players[playerTurn].playerNumber);
        }
        System.out.println("Player " + (this.playerTurn + 1) + " has " + players[playerTurn].hand.size() + " cards left.");
        System.out.println("");
        System.out.println("---------------------------------------------------------------------------------");

        if (isGameOver(this.players[this.playerTurn]))
        {
            System.out.println("");
            System.out.println("");
            System.out.println("");
            System.out.println("We have a winner! Player " + (this.playerTurn + 1) + " wins!!!");
            System.out.println("Total turn count: " + this.turnCount);
            System.exit(0);
        }
        if (playerTurn != players.length - 1 && !reverseEffect)
        {
            playerTurn++;
        }
        else if (playerTurn == players.length - 1 && !reverseEffect)
        {
            playerTurn = 0;
        }
        else if (playerTurn == 0 && reverseEffect)
        {
            playerTurn = players.length - 1;
        }
        else if (playerTurn != 0 && reverseEffect)
        {
            playerTurn--;
        }
    }
}
