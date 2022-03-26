import java.util.*;

public class BlackJack {
    public static void main(String[] args) {
        do{
//            initialize player
            Player player = new Player();
            ArrayList< Card > playerHand = player.getHand();
            int playerScore = 0;
            int playerTotalCards;
            int dealerTotalCards;
            String playerStatus = "";
            String dealerStatus = "";
            String winnerOfGame;
            Dealer dealer = new Dealer();
            Hand Hand = new Hand(player.getName(), dealer.getName());
            Deck deck = new Deck();
            ArrayList< Card > dealerHand = dealer.getHand();
            //Initialize string variable, representing the dealer's status
            deck.shuffleDeck();
            Card playerCard = deck.deal();
            player.addCard(playerCard);
            Card dealerCard = deck.deal();
            dealer.addCard(dealerCard);
            deck.initialDraw(player.getName(), playerCard);
            deck.initialDraw(dealer.getName(), dealerCard);
//          hit or stay for the player choice
            String playerChoice;
            playerChoice = Hand.hitOrStay();
            while(playerChoice.equals("hit")){
                player.addCard(deck.deal());
                playerScore = Hand.totalScore(playerHand);
                playerTotalCards = player.getNumOfCards();
                playerStatus = Hand.gameStatus(playerScore, playerTotalCards, player.getName());
                System.out.println(player);
                System.out.println(player.getName() + "'s score is: " + playerScore);
                System.out.println(player.getName() + "'s status is: " + playerStatus + "\n");
                if(playerStatus.equals("Continue")){
                    playerChoice = Hand.hitOrStay();
                }
                else{

                    System.out.println(player.getName() + ", cannot draw anymore because your " +
                            "status is: " + playerStatus + "\n");
                    break;//ends the player's turn by breaking out of while loop

                }//end else
            }//end while loop
            if(playerStatus.equals("Win")){
                System.out.println("Win! Congrats, " + player.getName() + "! You won!");
            }//end if
            //The player's status is not 'Win', so the dealer can take their turn
            else{
                //Calculate the dealer's total score
                int dealerScore = Hand.totalScore(dealerHand);
                //Loop while it's the dealer's turn (meaning, dealer's score is less than player's)
                while(dealer.dealerTurn(dealerScore, playerScore)){
                    //If the dealer has a better chance of winning by drawing, continue to draw cards
                    if(dealer.dealerAI(dealerScore, playerScore, playerChoice)){
                        //Calculate the dealer's current total score in the game
                        dealerScore = Hand.totalScore(dealerHand);
                        //Calculate the current number of cards in dealer's hand
                        dealerTotalCards = dealer.getNumOfCards();
                        //Check the dealer's status in the game
                        dealerStatus = Hand.gameStatus(dealerScore, dealerTotalCards, dealer.getName());
                        //If the dealer status is 'Continue', then the dealer can draw cards
                        if(dealerStatus.equals("Continue")){
                            //Add a random card to the dealer's hand
                            dealer.addCard(deck.deal());
                        }//end nested-if
                        //Else, the dealer cannot draw anymore due to their status
                        else{
                            break;//ends the dealer's turn by breaking out of the while loop
                        }//end nested-else
                    }//end if
                    //Else, the dealer's chances of winning are better without drawing, so stop drawing
                    else{
                        break;//ends the dealer's turn by breaking out of the while loop
                    }//end else
                }//end while loop
                //Inform the player that the dealer's turn is over
                System.out.println(dealer.getName() + "'s turn is now over." + "\n");
                //Determine the winner of the game
                winnerOfGame = Hand.isWin(playerStatus, dealerStatus, dealerScore, playerScore);
                //Display the scores of both players
                System.out.println(player.getName() + "'s final score is: " + playerScore);
                System.out.println(player + "\n");
                System.out.println(dealer.getName() + "'s final score is: " + dealerScore);
                System.out.println(dealer);
                //Display message that prints the winner of the game
                System.out.println("\n" + winnerOfGame + "\n");
            }//end else
        }while(playAgain());//If the player chooses to play again, continue to play game
    }//end main method
    //Method playAgain: Returns true if the player wants to play again, false otherwise
    public static boolean playAgain(){
        //Initialize Scanner object, called playAgainInput
        Scanner playAgainInput = new Scanner(System.in);
        //Declare string variable to store the player's answer
        String answer;
        //Prompt the player to type 'yes' to play again
        System.out.print("Enter the word 'yes' to play again, or anything else to end the game: ");
        answer = playAgainInput.next();
        return answer.equalsIgnoreCase("yes");
    }//end method playAgain
}//end of class BlackJack

record Card(int value) {
    //Create constructor and initialize instance variable, value
    //end of Card constructor

    //Accessor for the instance variable, value
    public int getValue() {
        return value;
    }//end of method getValue

    @Override //Override default toString method

    //Return a string of the Card's value
    public String toString() {
        return getValue() + "";
    }//end of method toString
}//end of class Card

class Dealer {
    //Declare string instance variable that represents the dealer's name
    private final String dealerName;
    //Declare instance variable that represents any arbitrary player's hand of cards
    private ArrayList < Card > hand;

    //Create initializing, parameterless constructor
    public Dealer(){
        //Explicitly call the parent class', PlayersHand, constructor

        //Initialize the player's hand of cards to an empty ArrayList
        Dealer.this.hand = new ArrayList<>();

        //Initialize the dealer's name
        dealerName = "Dealer";
    }//end of Dealer constructor
    //Accessor for instance variable, dealerName
    public String getName(){
        return dealerName;
    }//end method getName


    /*
     * Method dealerTurn: Determines whether the dealer should take or end their turn, by taking into
     * account the dealer and the player's score (accepted as parameters). Returns true if dealer
     * should take their turn and keep drawing, false otherwise.
     */
    public boolean dealerTurn(int dealerScore, int playerScore){

        //If the dealer's score is less than the player's, return true
        //end if
        //Else, the dealer's score is greater than or equal to player's, so return false
        //end else
        return dealerScore < playerScore;

    }//end method dealerTurn


    /*
     * Method dealerAI: A simple Artificial Intelligence that determines whether the dealer should
     * take their turn and keep drawing, or end their turn. Takes as parameters the player and dealer's
     * current score, and the choice of whether the player decided to 'Stay'. Based on the parameters,
     * return true if the dealer should continue their turn and draw again, false otherwise.
     */
    public boolean dealerAI(int dealerScore, int playerScore, String playerStays){

        //If the dealer's score is exactly 21, return false.
        if (dealerScore == 21){

            return false;
        }//end if

        //Else, if the player's score is greater than 21 (busted), return false
        else if (playerScore > 21){

            return false;
        }//end of else if

        //Else, if the player's score is less than the dealer's...
        else if(playerScore < dealerScore){

            //If the dealer's score is greater than or equal to 17, return false
            //end nested if
            return dealerScore < 17;

        }//end else if

        //Else, if the player chose to 'Stay'...
        else if (playerStays.equals("stay")){

            //If the dealer's score is greater than the player's score, return false
            //end if
            return true;
        }//end of else if

        //If none of the above are true, it's in the dealer's advantage to keep drawing, so return true
        return true;

    }//end method dealerAI



    @Override//Override the parent class', PlayersHand, toString method

    //Method toString: return a string representation of the Dealer's hand
    public String toString(){

        return getName() + "'s card hand is: " + (getHand() + "");
    }//end method toString


    //Accessor for instance variable, hand
    public ArrayList< Card > getHand(){
        return hand;
    }//end method getHand

    //Add method to add cards to the players hand
    public void addCard(Card card){

        hand.add(card);
    }//end method addCard

    //Access method that returns the total number of cards in the player's hand
    public int getNumOfCards(){

        //Initialize the total number of cards in hand to 0
        int totalCards = 0;

        //For each card in player's hand
        for (Card ignored : hand){

            //Increment the total number of cards by 1
            totalCards = totalCards + 1;

        }//end of for each loop

        return totalCards;

    }//end method getNumOfCards
}//end of class Dealer

class Deck {

    //Declare an ArrayList of Cards instance variable to represent the deck of cards
    private final ArrayList< Card > DeckOfCards;

    //Create initializing, parameterless constructor
    public Deck(){

        //Use a list and loop to fill the deck with the 52 cards (4 of each card value)

        //Initialize an integer array of all the possible card values in the deck
        int[] CardValuesList = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

        //Initialize instance variable DeckOfCards to an empty ArrayList of Cards
        DeckOfCards = new ArrayList<>();

        //For each integer element in the CardValuesList
        for(int cardNumber: CardValuesList){

            //Create a new Card object with the CardValuesList element's value
            Card currentCard = new Card(cardNumber);

            //Use a for loop to fill the deck with 4 cards of each value (total of 52 cards)
            for (int j = 1; j <= 4; j++){

                //Add the current Card object to the DeckOfCards
                DeckOfCards.add(currentCard);

            }//end nested for loop

        }//end for each loop

    }//end Deck constructor

    //Access method that returns the size of the DeckOfCards
    public int getDeckSize(){

        return DeckOfCards.size();
    }//end method getDeckSize


    //Method shuffleDeck: uses the random method to randomly shuffle the Cards in the DeckOfCards
    public void shuffleDeck(){
        Random random = new Random();
        int index = random.nextInt(getDeckSize());

        //For each index of each element in the DeckOfCards...
        for(int i = 0; i < getDeckSize(); i++){

            Card randomCard = DeckOfCards.get(index);
            //Assign that random card's index to the variable, randomCardIndex

            //Remove the random Card from the DeckOfCards
            DeckOfCards.remove(randomCard);

            //Append that random Card to the end of the DeckOfCards ArrayList
            DeckOfCards.add(randomCard);

        }//end of for loop

    }//end method shuffleDeck


    /*
     * Method dealCard: deals random Cards from the DeckOfCards, by first, drawing a random card,
     * removing it, and then returning the card.
     */
    public Card deal(){
        Random random = new Random();
        int index = random.nextInt(getDeckSize());

        //Assign a random Card from the deck to the Card object, randomCard
        Card randomCard = DeckOfCards.get(index);

        //Assign that random card's index to the variable, randomCardIndex

        //Remove the random Card from the DeckOfCards
        DeckOfCards.remove(randomCard);

        return randomCard;
    }//end method dealCard

    //Accessor method for the instance variable, DeckOfCards
    public ArrayList< Card > getDeck(){

        return DeckOfCards;
    }//end method getDeck

    //Method initialDraw: Prints out the initial draw of a player
    public void initialDraw(String name, Card card){

        System.out.println("First card for " + name + ": " + card);
    }//end method initialiDraw


    @Override //Overrides the default toString method

    //Method toString, returns a string representation of the deck of cards
    public String toString(){

        return "Current deck of cards: " + "\n" + getDeck();
    }//end method toString

}//end of class DeckOfCards

class Hand {

    //Declare string instance variables that will represent the names of the player and dealer
    private final String playerName;
    private final String dealerName;

    //Create initializing constructor, with string parameters for playerName and dealerName
    public Hand(String playerName, String dealerName){

        //Initialize the player and dealer's names

        this.playerName = playerName;
        this.dealerName = dealerName;

    }//end constructor Hand


    //Accessor method for instance variable, playerName
    public String getPlayerName(){

        return playerName;
    }//end method getPlayerName

    //Accessor method for instance variable, dealerName
    public String getDealerName(){

        return dealerName;
    }//end method getDealerName


    /*
     * Method hitOrStay: Gives the human player the option of whether to 'Hit' or 'Stay'.
     * Returns string 'hit' if the player chooses 'Hit', string 'stay' if the player chooses 'Stay',
     * or null otherwise.
     */
    public String hitOrStay(){

        //Initialize Scanner object input
        Scanner input = new Scanner(System.in);

        //Declare string variable to store the player's answer
        String answer;

        //Prompt the player to type in 'Hit' or 'Stay' as their choice, and store value in answer
        System.out.print(playerName + ", please choose to ''Hit'' or ''Stay'': ");
        answer = input.next();

        //If the player's answer begins with an 'h' (regardless of case), return string 'hit'
        if ( answer.startsWith("H") || answer.startsWith("h") ){

            return "hit";
        }//end if

        //Else, if the player's answer begins with an 's' (regardless of case), return string 'stay'
        else if (answer.startsWith("S") || answer.startsWith("s")){

            return "stay";
        }//end else if

        //Else, return null
        else {
            return "null";
        }//end else

    }//end method hitOrStay


    /*
     * Method totalScore: totals the value of the cards in a player's hand. Checks to see if the
     * player's hand contains an Ace. If an Ace is present, and the player will bust, change the
     * Ace value from 11 to 1. Return the totaled value of the cards in hand.
     */
    public int totalScore(ArrayList< Card > hand){

        //Declare Card object that represents the current card in player's hand
        Card currentCard;

        //Initialize the card's value to 0
        int currentCardValue;

        //Initialize the total values of cards in hand to 0
        int total = 0;


        //For each card in the player's hand
        for (Card eachCard: hand){

            //Access the card's value and assign it to the variable, cardValue
            int cardValue = eachCard.getValue();

            //Check for an Ace

            //If the card's value is 1...
            if(cardValue == 1){

                //Increment the total by 11
                total = total + 11;
            }//end if

            //Else, if the card is not an Ace
            else{

                //Add the cardValue to the total normally
                total = total + cardValue;
            }//end else

        }//end for each loop

        //If the player's total is greater than 21 (which means they will bust), Check for an Ace
        if(total > 21){
            for (Card card : hand) {
                currentCard = card;
                //Assign the value of the current card to variable, currentCardValue
                currentCardValue = currentCard.getValue();
                //If the current card's value is '1', that means the card is an Ace
                if (currentCardValue == 1) {
                    //Change the Ace's value in the total from 11 to 1, by subtracting the total by 10
                    total = total - 10;
                    return total;
                }//end if
            }//end for loop
        }//end if

        return total;

    }//end method totalScore

    /*
     * Method gameStatus: Determines whether a player's total status is a 'Bust', 'Win', 'Win'
     * (if it's the human player), or 'Continue'(which means the status is none of the above and
     * the game continues). Returns the string indicating the player's status.
     */
    public String gameStatus(int score, int totalCards, String name){

        //Loop until counter i is less than or equal to 5 (allows players to draw no more than 5 cards)
        for (int i = 1; i <= 5; i++){

            //Implement the rules of the BlackJack game to determine status

            //If the player's score is greater than or equal 21...
            if (score >= 21){
                //If the player's score is greater than 21, return string 'Bust'
                if(score > 21){
                    return "Bust";
                }//end if
                //Else, the score is exactly 21, return string 'Win'
                else{
                    return "Win";
                }//end of else
            }//end of nested-if
            //If the player's name is not dealer, then it's the human player
            if (!(name.equals("Dealer"))){
                //If the human player's total amount of cards is equal to 5, return 'Win'
                //This means the human player has also NOT 'Busted'
                if (totalCards == 5){
                    return "Win";
                }//end if
            }//end of if

        }//end of for loop

        //If none of the status' above, return string 'Continue'
        return "Continue";

    }//end of method gameStatus


    /*
     * Method isWin: Determines whether the dealer or player wins the game, depending on their
     * status and total scores (accepted as parameters). Returns a string indicating which player
     * has won, or whether it was a draw.
     */
    public String isWin(String playerStatus, String dealerStatus, int dealerScore, int playerScore){

        //If neither the player or dealer have busted...
        if(!(playerStatus.equals("Bust")) && !(dealerStatus.equals("Bust"))){

            //If the player's score is greater than dealer's, return string informing player of win
            if(playerScore > dealerScore){

                return "The winner is " + getPlayerName();
            }//end nested if

            //Else, if player's score is less than dealer's, return string informing player of loss
            else if (playerScore < dealerScore){

                return "The winner is " + getDealerName();
            }//end nested else if

            //Else, the player's score is equal to the dealer's, return string informing player of draw
            else{

                return "It's a draw!";
            }//end nested else

        }//end if

        //Else, if player has busted, return string informing player of loss
        else if(playerStatus.equals("Bust")){
            return getPlayerName() + " Busts! " + getDealerName() + " won!";
        }//end else if

        //Else, if dealer has busted, return string informing player of win
        else{
            return getDealerName() + " Busts! " + getPlayerName() + " won!";
        }//end else

    }//end method isWin


}//end of class Hand

class Player {

    //Declare string instance variable that represents the dealer's name
    private final String playerName;
    //Declare instance variable that represents any arbitrary player's hand of cards
    private ArrayList < Card > hand;

    //Create initializing, parameterless constructor
    public Player(){

        //Explicitly call the parent class', PlayersHand, constructor

        //Initialize the player's hand of cards to an empty ArrayList
        Player.this.hand = new ArrayList<>();

        //Initialize Scanner object, called playerInput
        Scanner playerInput = new Scanner(System.in);
        //Prompt the player to enter their name and assign value to instance variable, playerName
        System.out.print("Enter your name: ");
        playerName = playerInput.next();

    }//end constructor Player


    //Accessor for instance variable, playerName
    public String getName(){

        return playerName;
    }//end method getName


    @Override //Override the parent class', PlayersHand, toString method

    //Method toString: Returns a string representation of the player's hand
    public String toString(){

        return getName() + "'s card hand is: " + (getHand() + "");

    }//end method toString

    //Accessor for instance variable, hand
    public ArrayList< Card > getHand(){
        return hand;
    }//end method getHand

    //Add method to add cards to the players hand
    public void addCard(Card card){

        hand.add(card);
    }//end method addCard

    //Access method that returns the total number of cards in the player's hand
    public int getNumOfCards(){

        //Initialize the total number of cards in hand to 0
        int totalCards = 0;

        //For each card in player's hand
        for (Card ignored : hand){

            //Increment the total number of cards by 1
            totalCards = totalCards + 1;

        }//end of for each loop

        return totalCards;

    }//end method getNumOfCards
}//end of class Player

