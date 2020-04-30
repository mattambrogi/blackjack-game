package blackjack;

import java.util.*;

public class Blackjack implements BlackjackEngine {

	/**
	 * Constructor you must provide. Initializes the player's account to 200 and the
	 * initial bet to 5. Feel free to initialize any other fields. Keep in mind that
	 * the constructor does not define the deck(s) of cards.
	 * 
	 * @param randomGenerator
	 * @param numberOfDecks
	 */

	// Initializing variables
	int playerAccount;
	int bet;
	Random randomGenerator;
	int numberOfDecks;
	ArrayList<Card> deck;
	// initialize game status stuff
	int gameStatus;
	int playerStatus;
	int dealerStatus;
	ArrayList<Card> playerCards;
	ArrayList<Card> dealerCards;
	int dealerCardsTotal;
	int playerCardsTotal;
	Card dealerArray[];
	Card playerArray[];
	int dealerTotal[];
	int playerTotal[];

	// constructor
	
	public Blackjack(Random randomGenerator, int numberOfDecks) {
		// randomly makes number of decks
		this.playerAccount = 200;
		this.bet = 5;
		this.randomGenerator = randomGenerator;
		this.numberOfDecks = numberOfDecks;
		deck = new ArrayList<>();
		playerCards = new ArrayList<>();
		dealerCards = new ArrayList<>();
		dealerCardsTotal = 0;
		int gameStatus;
	}

	// get decks
	public int getNumberOfDecks() {
		return numberOfDecks;
	}

	// where a deck is created and shuffled
	public void createAndShuffleGameDeck() {
		for (int i = 0; i < numberOfDecks; i++) {
			for (CardSuit suit : CardSuit.values()) {
				for (CardValue value : CardValue.values()) {
					Card card = new Card(value, suit);
					deck.add(card);
				}
			}
		}

		Collections.shuffle(deck, randomGenerator);
	}

	// return the deck
	// where do we use this?
	public Card[] getGameDeck() {
		Card deckArray[] = new Card[deck.size()];
		int pos = 0;
		for (Card card : deck) {
			deckArray[pos] = card;
			pos++;
		}
		return deckArray;
	}

	public void deal() {
		/*
		 * create new deck deal player up deal dealer down deal player up deal dealer up
		 * status in progress delete bet amount
		 */
		playerCards.clear();
		dealerCards.clear();
		deck.clear();
		createAndShuffleGameDeck();
		// deal player up, dealer down
		playerCards.add(deck.get(0));
		deck.remove(0);
		Card a = deck.get(0);
		a.setFaceDown();
		dealerCards.add(a);
		deck.remove(0);
		deck.get(0).setFaceUp();
		// dealer player up, dealer up
		playerCards.add(deck.get(0));
		deck.remove(0);
		dealerCards.add(deck.get(0));
		deck.remove(0);

		// set game status
		gameStatus = GAME_IN_PROGRESS;

		// delete game amount
		playerAccount = playerAccount - bet;

	}

	/*
	 * Dealer Stuff
	 */

	// return array of dealer cards
	public Card[] getDealerCards() {
		Card dealerArray[] = new Card[dealerCards.size()];
		int pos = 0;
		for (Card card : dealerCards) {
			dealerArray[pos] = card;
			pos++;
		}
		return dealerArray;
	}

	// return array of dealer Cards values
	public int[] getDealerCardsTotal() {
		/*
		 * if value less than 21 return array with value(s) null if value >21 if no ace,
		 * size of 1 if ace, size of 2. Smaller first
		 */

		dealerCardsTotal = 0;
		int dealerTotal[] = new int[1];
		boolean ace = false;
		for (Card card : dealerCards) {
			dealerCardsTotal += card.getValue().getIntValue();
			if (card.getValue() == CardValue.Ace) {
				ace = true;
			}
		}
		if (dealerCardsTotal > 21) {
			return null;
		}
		dealerTotal[0] = dealerCardsTotal;
		// if array is actually size 2, add alternative value to second place
		if (ace == true && dealerCardsTotal + 10 < 22) {
			dealerTotal = new int[2];
			dealerTotal[0] = dealerCardsTotal;
			dealerTotal[1] = dealerCardsTotal + 10;
		}
		return dealerTotal;

	}

	// evaluate dealer cards to know dealer status
	public int getDealerCardsEvaluation() {
		/*
		 * report less than 21 if <21 report but is >21 report blackjack if ace and
		 * value of 10 card report has21 if = 21 but not blackjack return corresponding
		 * integer value as gameStatus
		 */
		dealerArray = getDealerCards();
		int dealerTotal[] = getDealerCardsTotal();
		if (dealerTotal != null) {
			if (dealerTotal.length == 2) {
				if (dealerTotal[1] == 21) {
					if (dealerArray.length == 2) {
						return BLACKJACK; // blackjack
					}
					return HAS_21; // has 21
				}
				return LESS_THAN_21;
			}
			if (dealerTotal.length == 1) {
				if (dealerTotal[0] < 21) {
					return LESS_THAN_21; // under21
				}
				if (dealerTotal[0] == 21) {
					return HAS_21; // has 21
				}
			}

		}

		return BUST;

	}

	/*
	 * Player stuff
	 */

	// return array of player cards
	public Card[] getPlayerCards() {
		Card playerArray[] = new Card[playerCards.size()];
		int pos = 0;
		for (Card card : playerCards) {
			playerArray[pos] = card;
			pos++;
		}
		return playerArray;
	}

	public int[] getPlayerCardsTotal() {
		// return array with value(or 2 poss values) of player cards

		playerCardsTotal = 0;
		int playerTotal[] = new int[1];
		boolean ace = false;
		for (Card card : playerCards) {
			playerCardsTotal += card.getValue().getIntValue();
			if (card.getValue() == CardValue.Ace) {
				ace = true;
			}
		}
		if (playerCardsTotal > 21) {
			return null;
		}
		playerTotal[0] = playerCardsTotal;
		// if array is actually size 2, add alternative value to second place
		if (ace == true && playerCardsTotal + 10 < 22) {
			playerTotal = new int[2];
			playerTotal[0] = playerCardsTotal;
			playerTotal[1] = playerCardsTotal + 10;
		}
		return playerTotal;
	}
	/*
	 * int ace = 0; int playerTotal[] = new int[1]; for(Card card: playerCards){
	 * playerCardsTotal += card.getValue().getIntValue(); if(card.getValue() ==
	 * CardValue.Ace) { ace = ace + 1;
	 */

	// possible issue here
	public int getPlayerCardsEvaluation() {
		int playerTotal[] = getPlayerCardsTotal();

		playerArray = getPlayerCards();
		if (playerTotal != null) {
			if (playerTotal.length == 2) {
				if (playerTotal[1] == 21) {
					if (playerArray.length == 2) { // only way to have blackjack
						return BLACKJACK; // blackjack
					}
					return HAS_21; // has 21
				}
				return LESS_THAN_21;
			}
			if (playerTotal.length == 1) {
				if (playerTotal[0] < 21) {
					return LESS_THAN_21; // under21
				}
				if (playerTotal[0] == 21) {
					return HAS_21; // has 21
				}
			}

		}
		return BUST;
	}

	/*
	 * Hit and Stand
	 */

	public void playerHit() {
		playerCards.add(deck.get(0));
		deck.remove(0);
		// do I first need to call other stuff that feeds evaluation
		getPlayerCards();
		getPlayerCardsTotal();
		int evaluation = getPlayerCardsEvaluation();
		gameStatus = GAME_IN_PROGRESS; // in progress
		if (evaluation == BUST) {
			gameStatus = DEALER_WON; // dealerWON
		}
	}

	public void playerStand() {
		// flip dealers facedown card
		// assign card to dealer till value is over 16 and less than 21
		// compare hands

		// to avoid null pointer
		Card dealerArray[] = getDealerCards();
		int dealerTotal[] = getDealerCardsTotal();
		// flip facedown card
		for (Card card : dealerArray) {
			if (card.isFacedUp() == false) {
				card.setFaceUp();
			}
		}

		// has to do with null

		
				while (dealerTotal != null && dealerTotal[dealerTotal.length - 1] < 16) { // what if already over 21?
					dealerCards.add(deck.get(0));
					deck.remove(0);
					dealerTotal = getDealerCardsTotal();
				}
			

		

		compareHands();
	}

	
	
	/*
	 * Getters and setters
	 */
	public int getGameStatus() {
		return gameStatus;
	}

	public void setBetAmount(int amount) {
		bet = amount;
	}

	public int getBetAmount() {
		return bet;
	}

	public void setAccountAmount(int amount) {
		playerAccount = amount;
	}

	public int getAccountAmount() {
		return playerAccount;
	}

	/*
	 * Compare hands method added to assist stand
	 */
	private void compareHands() {

		// do I need to call anything before this?
		dealerStatus = getDealerCardsEvaluation();
		playerStatus = getPlayerCardsEvaluation();

		// checking for bust
		if (playerStatus == BUST) {
			gameStatus = DEALER_WON;
		}
		if (dealerStatus == BUST && playerStatus != BUST) {
			gameStatus = PLAYER_WON;
		}

		// checking for has 21
		if (dealerStatus == HAS_21 && playerStatus != HAS_21) {
			gameStatus = DEALER_WON; // dealer win
		}
		if (dealerStatus != HAS_21 && playerStatus == HAS_21) {
			gameStatus = PLAYER_WON; // player win
		}
		if (dealerStatus == HAS_21 && playerStatus == HAS_21) {
			gameStatus = DRAW; // draw
		}

		// checking for blackjack
		// if player has blackjack does player automatically win?
		if (dealerStatus == BLACKJACK && playerStatus != BLACKJACK) {
			gameStatus = DEALER_WON; // dealer win
		}
		if (dealerStatus != BLACKJACK && playerStatus == BLACKJACK) {
			gameStatus = PLAYER_WON; // player win
		}
		if (dealerStatus == BLACKJACK && playerStatus == BLACKJACK) {
			gameStatus = DRAW; // draw
		}

		// under 21
		if (dealerStatus == LESS_THAN_21 && playerStatus == LESS_THAN_21) {

			playerTotal = getPlayerCardsTotal();
			dealerTotal = getDealerCardsTotal();

			int pTotal = playerTotal[0];
			int dTotal = dealerTotal[0];

			if (playerTotal.length == 2) {
				pTotal = playerTotal[1];
			}
			if (dealerTotal.length == 2) {
				dTotal = dealerTotal[1];
			}
			if (dTotal < pTotal) {
				gameStatus = PLAYER_WON; // player won
			}
			if (dTotal > pTotal) {
				gameStatus = DEALER_WON; // dealer won
			}
			if (dTotal == pTotal) {
				gameStatus = DRAW; // draw
			}
		}

		// returning bet
		if (gameStatus == DRAW) { // draw
			playerAccount = playerAccount + bet;
		}
		if (gameStatus == PLAYER_WON) { // player wins
			playerAccount = playerAccount + 2 * bet;
		}

	}// end of compare

//Testing class
	public static void main(String[] args) {
		Blackjack b = new Blackjack(new Random(), 2);
		b.dealerCards.add(new Card(CardValue.Ace, CardSuit.CLUBS));
		b.dealerCards.add(new Card(CardValue.Five, CardSuit.CLUBS));
		int[] myTotal = b.getPlayerCardsTotal();
	}

} // end of class