package cs410.uno;

import java.util.Collections;
import java.util.Stack;

/* Represents a deck of cards for Uno to function. Has two separate
 * stacks, one for the draw pile and one for the discard pile. This class
 * also defines and creates the cards for uno, reshuffles the deck once the
 * draw pile is empty, and handles wild card scenarios. Takes use of Card.
 */

public class Deck {

    public Stack<Card> unoDeck;
    public Stack<Card> unoDiscard;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";



    public Deck()
    {
        unoDeck = new Stack<>();
        unoDiscard = new Stack<>();
    }


    /* This method creates the "traditional" Uno deck that most can find
     * in stores or online.
     */
    public void createDeck()
    {
        String[] colors = {"red", "blue", "green", "yellow"};
        for(int i = 0; i < 4; i++)
        {
            Card[] cards = createCards(colors[i]);
            for (int j = 0; j < 10; j++)
            {
                this.unoDeck.push(cards[j]);
                this.unoDeck.push(cards[j]);
            }
            for (int j = 10; j < 13; j++)
            {
                this.unoDeck.push(cards[j]);
                this.unoDeck.push(cards[j]);
            }
            this.unoDeck.push(cards[13]);
            this.unoDeck.push(cards[13]);
        }
        Collections.shuffle(this.unoDeck);
    }

    /* This method creates a deck of Uno cards that the user can edit to their choosing,
     * including editing how many digit cards per color, special cards per color, and
     * the number of wild cards in the deck.
     */

    public void createDeck(int digitCardsPerColor, int specialCardsPerColor, int wildCards)
    {
        if (digitCardsPerColor < 1)
        {
            System.out.println("There has to at least be SOME digit cards! Exiting...");
            System.exit(0);
        }
        String[] colors = {"red", "blue", "green", "yellow"};
        int tracker;
        for (int i = 0; i < 4; i++)
        {
            Card[] cards = createCards(colors[i]);
            tracker = 1;
            for (int j = 0; j < 10; j++)
            {
                if (j == 9 && tracker != digitCardsPerColor)
                {
                    tracker++;
                    j = 0;
                }
                this.unoDeck.push(cards[j]);
            }
            tracker = 1;
            if (specialCardsPerColor > 0)
            {
                for (int j = 10; j < 13; j++)
                {
                    if (j == 12 && tracker != specialCardsPerColor)
                    {
                        tracker++;
                        j = 10;
                    }
                    this.unoDeck.push(cards[j]);
                }
            }
        }
        if (wildCards > 0)
        {
            for (int i = 0; i < wildCards; i++)
            {
                this.unoDeck.push(new Card("wild", "all"));
            }
        }
        Collections.shuffle(this.unoDeck);
        System.out.println("Cards shuffled.");
    }

    /* "Dictionary" for the Uno deck using an array. 0-9 are the digits,
     * 10 is reverse, 11 is skip, 12 is draw 2, 13 is wild card. Used in
     * createDeck looping 4 times for each of the colors.
     */

    public Card[] createCards(String color)
    {
        Card[] cards = new Card[14];
        cards[0] = new Card("0", color);
        cards[1] = new Card("1", color);
        cards[2] = new Card("2", color);
        cards[3] = new Card("3", color);
        cards[4] = new Card("4", color);
        cards[5] = new Card("5", color);
        cards[6] = new Card("6", color);
        cards[7] = new Card("7", color);
        cards[8] = new Card("8", color);
        cards[9] = new Card("9", color);
        cards[10] = new Card("reverse", color);
        cards[11] = new Card("skip", color);
        cards[12] = new Card("draw2", color);
        cards[13] = new Card("wild", "all");
        return cards;
    }

    /* This method is used in taking one card from the draw pile
     * and placing it in the discard pile. Used in the start of the game and
     * when reshuffling the draw pile during the game.
     */
    public void firstDiscard()
    {
        this.unoDiscard.push(this.unoDeck.pop());
    }

    /* Shuffles the draw pile using the cards in the discard pile.
     * Discard pile is empty at the end of this method.
     */
    public void shuffleDeck()
    {
        Collections.shuffle(this.unoDiscard);
        this.unoDeck = this.unoDiscard;
        this.unoDiscard = new Stack<>();
        firstDiscard();
    }


    /* Method to be used when a player plays a wild card.
     * Takes the player number as an argument. Returns the color of whatever the
     * "AI" chose the color to be.
     */
    public String playWild(int player)
    {
        int wildcolor = (int)(Math.random() * 4);
        if (wildcolor == 0)
        {
            System.out.println(ANSI_RED + "Player " + (player + 1) + " has changed the color to Red!" + ANSI_RESET);
            System.out.println("");
            return "red";
        }
        if (wildcolor == 1)
        {
            System.out.println(ANSI_BLUE + "Player " + (player + 1) + " has changed the color to Blue!" + ANSI_RESET);
            System.out.println("");
            return "blue";
        }
        if (wildcolor == 2)
        {
            System.out.println(ANSI_GREEN + "Player " + (player + 1) + " has changed the color to Green!" + ANSI_RESET);
            System.out.println("");
            return "green";
        }
        if (wildcolor == 3)
        {
            System.out.println(ANSI_YELLOW + "Player " + (player + 1) + " has changed the color to Yellow!" + ANSI_RESET);
            System.out.println("");
            return "yellow";
        }
        return "???";
    }

}
