package players;

import game.HandRanks;
import game.Player;

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
        return false;
    }

    @Override
    protected boolean shouldCheck() {
        return !getGameState().isActiveBet();
    }

    @Override
    protected boolean shouldCall() {
        //return getGameState().isActiveBet() && (getHandCards() > new HandRanks(3) || getGameState().getTableBet()*2);
        boolean hasDecentHand = evaluatePlayerHand().getValue() > HandRanks.HIGH_CARD.getValue();
        return getGameState().isActiveBet() && hasDecentHand || getGameState().isActiveBet() && getBank() > getGameState().getTableBet() * 2;
    }

    @Override
    protected boolean shouldRaise() {
        if (!getGameState().isActiveBet()) {
            return getBank() > (getGameState().getTableBet() * 2);
        }
        return false;
    }

    //@Override
    //protected boolean shouldRaisePotSize() {
    //    if (Math.random() < 0.2) {
    //        return true;
    //    }
    //    return false;
    //}

    @Override
    protected boolean shouldAllIn() {
        return false;
    }
}
