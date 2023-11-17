package entities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player {
    private UUID playerId;
    private long balance; // Account balance values are long.
    private List<Bet> bets;
    private int totalBetsPlaced;
    private int totalWins;
    private String illegitimatePlayerLastOperation;

    public Player(UUID playerId) {
        this.playerId = playerId;
        this.bets = new ArrayList<>();
        this.balance = 0;
        this.totalBetsPlaced = 0;
        this.totalWins = 0;
        this.illegitimatePlayerLastOperation = "";
    }

    public void handleBetPlacementAndSettlement(Bet bet, Match match) {
        // Placing bet, bet amount is deducted from player's balance.
        placeBet(bet);

        totalBetsPlaced++; // Increase the total number of bets placed

        // Determine the outcome of the bet and update the win count and balance accordingly
        if (match.getResult().equals(bet.getSide())) {
            // Player won the bet
            totalWins++; // Increase the win count
            double rate = bet.getSide().equals("A") ? match.getRateA() : match.getRateB();
            int winnings = (int) Math.floor(bet.getBetAmount() * rate) // Rounding down as written in task.
                    + bet.getBetAmount();
            balance += winnings; // Add winnings to the balance
        } else if (match.getResult().equals("DRAW")) {
            // The match is a draw, refund the bet amount
            balance += bet.getBetAmount();
        }
        // If the player lost the bet, no action needed since the bet amount was already deducted
    }

    public BigDecimal getWinRate() {
        if (totalBetsPlaced > 0) {
            BigDecimal wins = BigDecimal.valueOf(totalWins);
            BigDecimal bets = BigDecimal.valueOf(totalBetsPlaced);
            // Win rate is big decimal number with 2 decimal characters
            return wins.divide(bets, 2, RoundingMode.HALF_UP);
        } else {
            return BigDecimal.ZERO;
        }
    }



    public void deposit(long amount) {
        this.balance += amount;
    }

    public void withdraw(long amount) {
        this.balance -= amount;
    }

    public void placeBet(Bet bet) {
        this.bets.add(bet);
        this.balance -= bet.getBetAmount();
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public void setBets(List<Bet> bets) {
        this.bets = bets;
    }

    public int getTotalBetsPlaced() {
        return totalBetsPlaced;
    }

    public void setTotalBetsPlaced(int totalBetsPlaced) {
        this.totalBetsPlaced = totalBetsPlaced;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public String getIllegitimatePlayerLastOperation() {
        return illegitimatePlayerLastOperation;
    }

    public void setIllegitimatePlayerLastOperation(String illegitimatePlayerLastOperation) {
        this.illegitimatePlayerLastOperation = illegitimatePlayerLastOperation;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", balance=" + balance +
                //", bets=" + bets +
                '}';
    }
}