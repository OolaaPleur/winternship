package entities;

import java.util.UUID;

public class Bet {
    private UUID betId;
    private UUID matchId;
    private int betAmount;
    private String side;
    private int casinoWinOrLoss;

    public Bet(UUID matchId, int betAmount, String side) {
        this.betId = UUID.randomUUID();
        this.matchId = matchId;
        this.betAmount = betAmount;
        this.side = side;
        casinoWinOrLoss = 0;
    }

    public UUID getBetId() {
        return betId;
    }

    public void setBetId(UUID betId) {
        this.betId = betId;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public int getCasinoWinOrLoss() {
        return casinoWinOrLoss;
    }

    public void setCasinoWinOrLoss(int casinoWinOrLoss) {
        this.casinoWinOrLoss = casinoWinOrLoss;
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betId=" + betId +
                ", matchId=" + matchId +
                ", betAmount=" + betAmount +
                ", side='" + side + '\'' +
                '}';
    }
}