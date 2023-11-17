package entities;

import java.util.UUID;

public class Match {
    private UUID matchId;
    private double rateA;
    private double rateB;
    private String result;

    public Match(UUID matchId, double rateA, double rateB, String result) {
        this.matchId = matchId;
        this.rateA = rateA;
        this.rateB = rateB;
        this.result = result;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public double getRateA() {
        return rateA;
    }

    public void setRateA(double rateA) {
        this.rateA = rateA;
    }

    public double getRateB() {
        return rateB;
    }

    public void setRateB(double rateB) {
        this.rateB = rateB;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + matchId +
                ", rateA=" + rateA +
                ", rateB=" + rateB +
                ", result='" + result + '\'' +
                '}';
    }
}