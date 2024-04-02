package cs410.uno;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Stack;

/* Represents a singular player. Has their hand, cards playable
 * in their hand, player number, if they took a turn or not, and
 * if they were skipped or not. Also contains most of the card playing
 * logistics and "AI". Takes use of Card, and accepts Stacks of Cards from Deck.
 */

public class Player {
    public ArrayList<Card> hand;
    public ArrayList<Card> playableHand;
    public int playerNumber;
    public boolean tookTurn;
    public boolean skipped;

    public Player(int number)
    {
        this.hand = new ArrayList<Card>();
        this.playableHand = new ArrayList<Card>();
        this.playerNumber = number;
    }

    /* Gets the player's hand. A player's hand has initialCards
     * amount of cards.
     */

    public void getHand(Stack<Card> fromDeck, int initialCards)
    {
        if (initialCards < 1)
        {
            System.out.println("If everyone starts with less than 1 card, everyone wins! Exiting...");
            System.exit(0);
        }
        for (int i = 0; i < initialCards; i++)
        {
            if (fromDeck.isEmpty())
            {
                System.out.println("Not enough cards to start the game! Exiting...");
            }
            this.hand.add(fromDeck.pop());
        }
        System.out.println("Player " + (this.playerNumber + 1) + " got their hand.");
    }

    /* Checks if the player has Uno. A player has Uno
     * if they only have one card in their hand.
     */
    public boolean hasUno()
    {
        return this.hand.size() == 1;
    }

    /* Checks if the player won. A player won
     * if they have no cards in their hand.
     */
    public boolean winner()
    {
        return this.hand.isEmpty();
    }

    /* Prints all cards in a player's hand.
     */
    public void printCards()
    {
        for (Card card : hand) {
            System.out.print("[" + card.color() + " " + card.type() + "], ");
        }
        System.out.println("");
    }

    /* Code to be executed if a player was skipped. A player
     * can be skipped by either a draw2 or skip card. Contains
     * the player's penalty for being hit by these two cards.
     */

    public void isSkipped(Stack<Card> discardPile, Stack<Card> drawPile)
    {
        if (Objects.equals(discardPile.peek().type(), "draw2") && discardPile.size() > 1)
        {
            System.out.println("They draw two.");
            this.hand.add(drawPile.pop());
            if (drawPile.isEmpty())
            {
                return;
            }
            this.hand.add(drawPile.pop());
            this.skipped = true;
            return;
        }
        if (Objects.equals(discardPile.peek().type(), "skip") && discardPile.size() > 1)
        {
            System.out.println("They were skipped.");
            this.skipped = true;
        }
    }

    /* Checks if the reverse card was played.
     */

    public boolean reversePlayed(Card reverse)
    {
        return Objects.equals(reverse.type(), "reverse");
    }

    /* The main card playing method. At the end of this method, there are
     * possibilities:
     *
     * The player played a card.
     * The player could not play a card, and draws from the draw pile.
     * The player could not play a card, draws from the draw pile, and plays that card.
     * The player was skipped.
     *
     * This method also keeps track of whether the player took a turn - the player took a turn
     * if they were not skipped. The main body of this method will only execute if this player was not
     * skipped by a draw2 or a skip card.
     */
    public void playCard(Stack<Card> discardPile, Stack<Card> drawPile, String color)
    {
        if (!this.skipped)
        {
            Random rand = new Random();
            int randint;
            this.tookTurn = false;
            if (Objects.equals(discardPile.peek().type(), "wild"))
            {
                for (Card card : hand) {
                    if (card.playableWild(color) || card.isWild())
                    {
                        this.playableHand.add(card);
                    }
                }
                if (this.playableHand.isEmpty())
                {
                    this.hand.add(drawPile.pop());
                    System.out.println("They are unable to play a card this turn, so they draw one.");
                    if (this.hand.get(this.hand.size() - 1).playableWild(color) || this.hand.get(this.hand.size() - 1).isWild())
                    {
                        discardPile.push(this.hand.get(this.hand.size() - 1));
                        System.out.println("They play a " + discardPile.peek().color() + " " + discardPile.peek().type() + " card, after drawing it from the deck.");
                        this.hand.remove(this.hand.size() - 1);
                        this.tookTurn = true;
                    }
                    return;
                }
                randint = rand.nextInt(this.playableHand.size());
                discardPile.push(this.playableHand.get(randint));
                System.out.println("They play a " + discardPile.peek().color() + " " + discardPile.peek().type() + " card.");
                hand.remove(this.playableHand.get(randint));
                this.playableHand.clear();
                this.tookTurn = true;
                return;
            }
            for (Card card : hand)
            {
                if (card.playableCard(discardPile.peek()) || card.isWild())
                {
                    this.playableHand.add(card);
                }
            }
            if (this.playableHand.isEmpty())
            {
                this.hand.add(drawPile.pop());
                System.out.println("They are unable to play a card this turn, so they draw one.");
                if (this.hand.get(this.hand.size() - 1).playableCard(discardPile.peek()) || this.hand.get(this.hand.size() - 1).isWild())
                {
                    discardPile.push(this.hand.get(this.hand.size() - 1));
                    System.out.println("They play a " + discardPile.peek().color() + " " + discardPile.peek().type() + " card, after drawing it from the deck.");
                    this.hand.remove(this.hand.size() - 1);
                    this.tookTurn = true;
                }
                return;
            }
            randint = rand.nextInt(this.playableHand.size());
            discardPile.push(this.playableHand.get(randint));
            System.out.println("They play a " + discardPile.peek().color() + " " + discardPile.peek().type() + " card.");
            hand.remove(this.playableHand.get(randint));
            this.playableHand.clear();
            this.tookTurn = true;
        }
        else
        {
            this.tookTurn = false;
            this.skipped = false;
        }
    }



}
