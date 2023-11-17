package entities;

public class Casino {
    private long balance; // Account balance values are long.

    public Casino() {
        this.balance = 0;
    }

    // Method to update the casino balance when a bet is settled
    public int settleBet(int betAmount, double rate, String matchResult, String betSide) {
        if (matchResult.equals(betSide)) {
            // Player won, casino loses the betAmount * rate
            int winnings = (int) Math.floor(betAmount * rate);
            balance -= winnings;
            return winnings;
        } else if (!matchResult.equals("DRAW")) {
            // Player lost, casino gains the betAmount
            balance += betAmount;
            return -(betAmount);
        }
        // If it's a draw, there is no impact on the casino balance (player's bet is refunded)
        return 0;
    }

    public void getCasinoBalanceBackToNormalAfterIllegalAction(Player player) {
        int casionWinOrLoss = 0;
        for (Bet bet : player.getBets()) {
            casionWinOrLoss += bet.getCasinoWinOrLoss();
        }
        balance = casionWinOrLoss + balance;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Casino{" +
                "balance=" + balance +
                '}';
    }
}