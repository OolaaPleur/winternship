import entities.Bet;
import entities.Casino;
import entities.Match;
import entities.Player;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class BetProcessor {

    private final Map<UUID, Player> players = new HashMap<>(); // Hashmap for fast access and uniqueness of keys.
    private final Map<UUID, Match> matches = new HashMap<>();
    private final Casino casino = new Casino();

    public void readAndProcessMatchData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                UUID matchId = UUID.fromString(parts[0]);
                double rateA = Double.parseDouble(parts[1]);
                double rateB = Double.parseDouble(parts[2]);
                String result = parts[3];

                Match match = new Match(matchId, rateA, rateB, result);
                matches.put(matchId, match); // Add the match to the map
            }
        }
    }

    public void readAndProcessPlayerData(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLine(line);
            }
        }
    }


    private void processLine(String line) {
        String[] parts = line.split(",");
        UUID playerId = UUID.fromString(parts[0]);
        Player player = players.computeIfAbsent(playerId, Player::new);

        if (!player.getIllegitimatePlayerLastOperation().isEmpty()) {
            return;
        }

        String action = parts[1];
        switch (action) {
            case "DEPOSIT":
                handleDeposit(player, parts);
                break;
            case "BET":
                handleBet(player, parts);
                break;
            case "WITHDRAW":
                handleWithdraw(player, parts);
                break;
        }
    }

    private void handleDeposit(Player player, String[] parts) {
        int depositAmount = Integer.parseInt(parts[3]); // Coin numbers in transactions are int type.
        player.deposit(depositAmount);
    }

    private void handleBet(Player player, String[] parts) {
        UUID matchId = UUID.fromString(parts[2]);
        int betAmount = Integer.parseInt(parts[3]); // Coin numbers in transactions are int type.
        String side = parts[4];
        // Check if the player has enough balance to place the bet.
        if (player.getBalance() >= betAmount) {
            // Retrieve the match information.
            Match match = matches.get(matchId);
            if (match != null) {
                Bet bet = new Bet(matchId, betAmount, side);

                player.handleBetPlacementAndSettlement(bet, match);
                int casinoWinOrLoss = casino.settleBet(betAmount,
                        side.equals("A") ? match.getRateA() : match.getRateB(), match.getResult(), side);
                bet.setCasinoWinOrLoss(casinoWinOrLoss);
            }
        } else {
            player.setIllegitimatePlayerLastOperation(parts[0] + ' ' + parts[1] + ' ' + parts[2] + ' ' + parts[3] + ' ' + parts[4]);
            casino.getCasinoBalanceBackToNormalAfterIllegalAction(player);
        }
    }

    private void handleWithdraw(Player player, String[] parts) {
        int withdrawAmount = Integer.parseInt(parts[3]); // Coin numbers in transactions are int type.
        if (player.getBalance() >= withdrawAmount) {
            player.withdraw(withdrawAmount);
        } else {
            player.setIllegitimatePlayerLastOperation(parts[0] + ' ' + parts[1] + " null " + parts[3] + " null");
            casino.getCasinoBalanceBackToNormalAfterIllegalAction(player);
        }

    }

    public void generateOutputFile(String filename) throws IOException {
        List<Player> sortedPlayers = new ArrayList<>(players.values());
        sortedPlayers.sort(Comparator.comparing(Player::getPlayerId)); // Sorted by player ID

        StringBuilder legitimatePlayersBuilder = new StringBuilder();
        StringBuilder illegitimatePlayersBuilder = new StringBuilder();

        // Loop through players once, categorizing them into legitimate and illegitimate
        for (Player player : sortedPlayers) {
            if (player.getIllegitimatePlayerLastOperation().isEmpty()) {
                legitimatePlayersBuilder.append(String.format("%s %d %.2f%n",
                        player.getPlayerId().toString(),
                        player.getBalance(),
                        player.getWinRate()));
            } else {
                illegitimatePlayersBuilder.append(player.getIllegitimatePlayerLastOperation()).append(System.lineSeparator());
            }
        }

        // Write to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Paths.get(filename).toFile()))) {
            writeBuilderContent(writer, legitimatePlayersBuilder);
            writeBuilderContent(writer, illegitimatePlayersBuilder);
            writer.write(String.valueOf(casino.getBalance()));
        }
    }

    private void writeBuilderContent(BufferedWriter writer, StringBuilder builder) throws IOException {
        if (!builder.isEmpty()) {
            writer.write(builder.toString());
            writer.write(System.lineSeparator());
        } else {
            writer.write(System.lineSeparator());
            writer.write(System.lineSeparator());
        }
    }
}
