package players;

import game.Card;
import game.HandRanks;
import game.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class BenPlayer extends Player {
    /**
     * Constructs a new Player with the specified name.
     * Initializes the player's hand, bank balance, and various status flags.
     *
     * @param name The name of the player.
     */
    public BenPlayer(String name) {
        super(name);
    }

    @Override
    protected void takePlayerTurn() {

        boolean onDraw = hasStrongDraw();

        if(shouldFold()) {
            fold();
        }
        else if(shouldCheck()) {
            check();
        }
        else if(shouldCall()) {
            call();
        }
        else if(shouldRaise()) {
            raise(getGameState().getTableMinBet());
        }
        else if(shouldAllIn()) {
            allIn();
        }

    }

    //@Override
    //protected void takePlayerTurn() {
    //    HandRanks currentHandRank = evalulatePlayerHand();
    //}

    @Override
    protected boolean shouldFold() {
        boolean hasDecentHand = evaluatePlayerHand().getValue() > HandRanks.TWO_PAIR.getValue();
        return getGameState().isActiveBet() && !hasDecentHand;
    }

    @Override
    protected boolean shouldCheck() {
        boolean hasPair = evaluatePlayerHand().getValue() > HandRanks.PAIR.getValue();
        return !getGameState().isActiveBet() || !getGameState().isActiveBet() && hasPair;
    }

    @Override
    protected boolean shouldCall() {
        //return getGameState().isActiveBet() && (getHandCards() > new HandRanks(3) || getGameState().getTableBet()*2);
        boolean hasDecentHand = evaluatePlayerHand().getValue() > HandRanks.TWO_PAIR.getValue();
        return hasStrongDraw() || getGameState().isActiveBet() && hasDecentHand || getGameState().isActiveBet() && getBank() > getGameState().getTableBet() * 2;
    }

    @Override
    protected boolean shouldRaise() {
        return hasStrongDraw() && getBank() > getGameState().getTableBet() * 2 || evaluatePlayerHand().getValue() > HandRanks.TWO_PAIR.getValue() || Math.random() < 0.2;
    }



    @Override
    protected boolean shouldAllIn() {
        return evaluatePlayerHand().getValue() == HandRanks.FULL_HOUSE.getValue() || evaluatePlayerHand().getValue() == HandRanks.STRAIGHT.getValue() || evaluatePlayerHand().getValue() == HandRanks.FLUSH.getValue() || evaluatePlayerHand().getValue() == HandRanks.STRAIGHT_FLUSH.getValue()|| evaluatePlayerHand().getValue() == HandRanks.FOUR_OF_A_KIND.getValue() || evaluatePlayerHand().getValue() == HandRanks.ROYAL_FLUSH.getValue();
    }


    private boolean hasStrongDraw() {
        //return evaluatePlayerHand().getValue() >
        ArrayList<Card> allCards = new ArrayList<>();
        for(Card card : getGameState().getTableCards()) {
            allCards.add(card);
        }
        for(Card card : getHandCards()) {
            allCards.add(card);
        }

        if(evaluatePlayerHand().getValue() >= HandRanks.PAIR.getValue()) {
            return true;
        }

        allCards.sort(Comparator.comparingInt(Card::getValue));

        // Check if there are 3 consecutive cards
        int consecutiveCount = 1;
        for (int i = 1; i < allCards.size(); i++) {
            // If the current card's rank is exactly 1 greater than the previous card's rank
            if (allCards.get(i).getValue() == allCards.get(i - 1).getValue() + 1) {
                consecutiveCount++;
                if (consecutiveCount >= 3) {
                    return true; // We found a sequence of 3 or more cards
                }
            } else {
                consecutiveCount = 1; // Reset if the sequence is broken
            }
        }

        // Return false if no sequence of 3 or more consecutive cards is found
        return false;
    }
}
