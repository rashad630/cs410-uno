package cs410.uno;

import java.util.Objects;

/* Card class for Uno.
 * Contains the card type (0-9, reverse, skip etc.) and color of the card.
 * Also has methods to check if this card is playable, if it's playable in a
 * wild card scenario, and if this card is a wild card.
 */
public class Card {

    private final String cardType;
    private final String cardColor;

    /* Constructor.
     */
    public Card(String type, String color)
    {
        this.cardType = type;
        this.cardColor = color;
    }

    /* Returns if this card is a playable card
     * given the top card in the discard pile.
     */
    public boolean playableCard(Card toCompare)
    {
        return Objects.equals(this.cardType, toCompare.cardType) || Objects.equals(this.cardColor, toCompare.cardColor);
    }

    /* Returns if this card is a playable card in a wild card scenario,
     * that is, a wild card is currently the top card in the discard pile.
     */
    public boolean playableWild(String color)
    {
        return Objects.equals(color, this.color());
    }

    /* Returns if this card is a wild card.
     */
    public boolean isWild()
    {
        return Objects.equals(this.cardType, "wild");
    }

    /* Returns this card's type.
     */
    public String type()
    {
        return this.cardType;
    }

    /* Returns this card's color.
     */
    public String color()
    {
        return this.cardColor;
    }

}
