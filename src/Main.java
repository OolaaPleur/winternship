import java.io.*;

public class Main {
    public static void main(String[] args) {
        BetProcessor betProcessor = new BetProcessor();
        try {
            betProcessor.readAndProcessMatchData("resources/match_data.txt");
            betProcessor.readAndProcessPlayerData("resources/player_data.txt");
            betProcessor.generateOutputFile("src/result.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
