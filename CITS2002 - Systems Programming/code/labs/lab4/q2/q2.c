/*
 * Consider the following sequence:
 * 4D KC JS 10S ....
 * which represents the playing cards Four of Diamonds,
 * King of Clubs, Jack of Spades, Ten of Spades, ..., once they have been randomly shuffled.
 *
 * Firstly, decide how you can represent each of the 52 possible cards
 * using one (or more?) of the basic C datatypes.
 *
 * Next, for any distinct value of your representation, determine how you
 * could print its "meaning" - such as 4D, or KC.
 *
 * Next, use an array to represent each/all-possible cards - your deck of cards.
 * With reference to the online manual for the standard-C99 function rand(),
 * devise a scheme to shuffle your deck of cards,
 * and to then print all of them, one at a time.
 * Obviously, each card should only be printed once.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <ctype.h>
#include <time.h>

/*
 * Models a card
 */
struct Card
{
    // Number
    int num;

    // Characters
    char ch1;
    char ch2;
};

// The deck of cards
struct Card cards[52];
int cardNo = 0;

// The card suits
char suits[] = "HSCD";

/*
 * Print a card out
 */
void printCard(struct Card card)
{
    // Get values
    int num = card.num;
    char ch1 = card.ch1;
    char ch2 = card.ch2;

    // Print num
    if (num != -1) {
        printf("%i", num);
    }

    // Print chars
    if (ch1 != '0') {
        printf("%c", ch1);
    }

    if (ch2 != '0') {
        printf("%c", ch2);
    }
}

/*
 * Print deck
 */
void printDeck(void)
{
    // Print line for cards
    printf("\n ");

    // For all cards
    for (int i = 0; i < cardNo; i++) {

        // Print card
        printCard(cards[i]);

        // Print separator
        printf("-");

        // Print extra spaces
        if (i != 0 && i % 13 == 0) {
            printf("\n ");
        }
    }

    // Print space
    printf("\n");
}

/*
 * Print a suit of cards
 */
void printSuit(char sChar)
{
    // Line start
    printf("\n %c Suit = ", sChar);

    // For all cards
    int count = 0;
    for (int i = 0; i < 53; i++) {
        if (cards[i].ch1 == sChar || cards[i].ch2 == sChar) {
            printCard(cards[i]);

            count++;

            printf(" ");
        }
    }

    // Print card count
    printf(" (N:%i)", count);

}

/*
 * Returns true if the cards are the same
 */
bool areSameCard(struct Card c1, struct Card c2)
{
    // Check numbers
    bool numSame = (c1.num == c2.num);

    // Check chars
    bool c1Same = (c1.ch1 == c2.ch1);
    bool c2Same = (c1.ch2 == c2.ch2);

    // Return result of all checks
    return numSame && c1Same && c2Same;
}

/*
 * Return true if the card is the deck
 */
bool isInDeck(struct Card card)
{
    // For all cards in the deck
    for (int i = 0; i < cardNo; i++) {

        // If the current card is the same as the input
        if (areSameCard(cards[i], card)) {

            // Return true as it is in the deck already
            return true;
        }
    }

    // Otherwise return false
    return false;
}

/*
 * Get a random card
 */
struct Card getRanCard(void)
{
    // Make struct
    struct Card card;

    // Get number part of card (-1, or between 2 and 10)
    //  Seed the RNG wildly
    srand(time(NULL) * 10 * rand() * rand());
    //  Get random number between 0 and 10
    int ranNum = rand() % 11;
    // Turn 0s and 1s to -1s (these represent having no number)
    if (ranNum == 0 || ranNum == 1) {
        ranNum = -1;
    }
    //  Save number
    card.num = ranNum;

    // Get string part of card
    //  Add default values
    card.ch1 = '0';
    card.ch2 = '0';
    //  If the card has no number part
    if (card.num == -1) {

        // Add a random suit
        card.ch2 = "HSCD"[rand() % 4];

        // May an ace or royal card
        bool isAce = rand() % 2;

        // If it is an Ace
        if (isAce) {
            // Add A char
            card.ch1 = 'A';
        } else {
            // Else, if it is royal,
            // add a random royal char
            card.ch1 = "JQK"[rand() % 3];
        }

    } else {
        // Else If the card has a number part

        // Copy in a random suit char
        card.ch1 = suits[rand() % 4];
    }

    // Return the card
    return card;
}

/*
 * Add a card to the deck
 */
void addCard(void)
{
    // Generate a card
    struct Card card = getRanCard();

    // Check if its in the deck
    bool inDeck = isInDeck(card);

    // If its not
    if (!inDeck) {

        // Add the card
        cards[cardNo] = card;
        cardNo++;

    } else {

        // If a duplicate exists, try adding a different random card
        addCard();
    }
}

/*
 * Generates a deck of cards
 */
void generateDeck(void)
{
    // Space
    printf("\n");

    // Add a card to all positions
    while (cardNo != 52) {
        addCard();
    }

    // Print cards
    printf("\n Random Deck of %i cards:", cardNo);
    printf("\n");
    printDeck();

    // Print in suits
    printSuit(suits[0]);
    printSuit(suits[1]);
    printSuit(suits[2]);
    printSuit(suits[3]);

    // Make space
    printf("\n\n");
}

int main(void)
{
    // Info
    printf("\n++++ Lab4 - Q2 - Deck of Cards Generator \n");
    printf("\n Args: None \n");

    // Run deck generator
    generateDeck();

    // Exit
    exit(EXIT_SUCCESS);
}
