package de.kasperczyk.bowling;

public class BowlingTestRunner {

    public static void main(String[] args) {
        new BowlingTestRunner().run();
    }

    private void run() {
        int[] game = new int[] {1, 4, 4, 5, 6, 4, 5, 5, 10, 0, 1, 7, 3, 6, 4, 10, 2, 8, 6}; // -> 133
        int[] anotherGame = new int[] {4, 5, 5, 3, 6, 4, 10, 7, 3, 0, 0, 4, 6, 10, 10, 7, 1}; // -> 159
        int[] perfectGame = new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10,}; // -> 300
        playGame(game);
        playGame(anotherGame);
        playGame(perfectGame);
    }

    private void playGame(int[] rolls) {
        Game game = new GameImpl();
        for (Integer roll : rolls) {
            game.addRoll(roll);
            System.out.println("Rolled " + roll + " pins.");
        }
        System.out.println("Game over - Your score is " + game.getTotalScore());
    }
}
