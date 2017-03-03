package de.kasperczyk.bowling;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GameTest {

    private GameImpl game; // sut

    private static final int[] COMPLETE_GAME = new int[] {
            1, 4,     4, 5,
            6, 4,     5, 5,
            10,       0, 1,
            7, 3,     6, 4,
            10,       2, 8, 6
    };

    private static final int[] COMPLETE_GAME_WITH_TWO_STRIKES_IN_A_ROW_AND_TWO_ROLLS_IN_LAST_FRAME = new int[] {
            4, 5,     5, 3,
            6, 4,     10,
            7, 3,     0, 0,
            4, 6,     10,
            10,       7, 1
    };

    private static final int[] PERFECT_GAME = new int[] {10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10};

    @Before
    public void setup() {
        game = new GameImpl();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void gameInitializesFramesListWithTenFrames() {
        assertThat(game.getFrames().size(), is(10));
    }

    @Test
    public void firstFrameWith4And5HasScoreOf9() {
        addRolls(4, 5);
        assertThat(game.getTotalScore(), is(9));
        assertThat(game.getFrames().get(0).getScore(), is(9));
    }

    @Test
    public void thirdRollIsAddedToSecondFrame() {
        addRolls(4, 5, 3);
        assertThat(game.getTotalScore(), is(12));
        assertThat(game.getFrames().get(0).getScore(), is(9));
        assertThat(game.getFrames().get(1).getScore(), is(3));
    }

    @Test
    public void frameWithSpareIsAwardedFirstRollOfNextFrame() {
        addRolls(4, 6, 3, 1);
        assertThat(game.getTotalScore(), is(17));
        assertThat(game.getFrames().get(0).getScore(), is(13));
        assertThat(game.getFrames().get(1).getScore(), is(4));
    }

    @Test
    public void frameWithSpareIsAwardedFirstRollOfNextFrame_rollInQuestionIsZero() {
        addRolls(4, 6, 0, 3);
        assertThat(game.getTotalScore(), is(13));
        assertThat(game.getFrames().get(0).getScore(), is(10));
        assertThat(game.getFrames().get(1).getScore(), is(3));
    }

    @Test
    public void frameIsCompleteIfFirstRollStrikes() {
        addRolls(10, 2);
        assertThat(game.getFrames().get(0).isComplete(), is(true));
        assertThat(game.getFrames().get(1).getScore(), is(2));
    }

    @Test
    public void frameWithStrikeIsAwardedNextTwoRolls() {
        addRolls(10, 3, 5);
        assertThat(game.getTotalScore(), is(26));
        assertThat(game.getFrames().get(0).getScore(), is(18));
        assertThat(game.getFrames().get(1).getScore(), is(8));
    }

    @Test
    public void frameWithStrikeIsAwardedNextTwoRolls_withZeroInNextTwoRolls() {
        addRolls(10, 0, 5);
        assertThat(game.getTotalScore(), is(20));
        assertThat(game.getFrames().get(0).getScore(), is(15));
        assertThat(game.getFrames().get(1).getScore(), is(5));
    }

    @Test
    public void frameWith0And10IsASpareAndNotAStrike() {
        addRolls(0, 10, 2, 4);
        assertThat(game.getTotalScore(), is(18));
        assertThat(game.getFrames().get(0).getScore(), is(12));
    }

    @Test
    public void lastFrameAllowsThirdRollIfFirstTwoRollsAreSpare() {
        for (int i = 0; i < 9; i++) {
            addRolls(0, 0);
        }
        addRolls(8, 2, 6); // last frame
        assertThat(game.getTotalScore(), is(16));
        assertThat(game.getFrames().get(9).getScore(), is(16));
    }

    @Test
    public void firstFrameShouldHaveAScoreOf30IfThreeStrikesAreThrownInARow() {
        addRolls(10, 10, 10);
        assertThat(game.getFrames().get(0).getScore(), is(30));
    }

    // should cover all cases, let's test some games (valid input)

    @Test
    public void testCompleteGame() {
        addRolls(COMPLETE_GAME);
        assertThat(game.getTotalScore(), is(133));
    }

    @Test
    public void testCompleteGameWithTwoStrikesInARow() {
        addRolls(COMPLETE_GAME_WITH_TWO_STRIKES_IN_A_ROW_AND_TWO_ROLLS_IN_LAST_FRAME);
        assertThat(game.getTotalScore(), is(140));
    }

    @Test
    public void testPerfectGame() {
        addRolls(PERFECT_GAME);
        assertThat(game.getTotalScore(), is(300));
    }

    // required functionality for valid input achieved, now let's prevent cheating

    @Test
    public void twoRollsInAFrameCannotScoreMoreThanTen() {
        thrown.expect(IllegalArgumentException.class);
        addRolls(8, 3);
    }

    @Test
    public void rollScoresAtLeastZero() {
        thrown.expect(IllegalArgumentException.class);
        addRolls(-1);
    }

    @Test
    public void rollScoresAtMostTen() {
        thrown.expect(IllegalArgumentException.class);
        addRolls(11);
    }

    @Test
    public void gameEndsWhenTenFramesAreComplete_threeRollsInLastFrame() {
        addRolls(COMPLETE_GAME);
        thrown.expect(IllegalArgumentException.class);
        addRolls(5);
    }

    @Test
    public void gameEndsWhenTenFramesAreComplete_withPerfectGame() {
        addRolls(PERFECT_GAME);
        thrown.expect(IllegalArgumentException.class);
        addRolls(5);
    }

    @Test
    public void gameEndsWhenTenFramesAreComplete_twoRollsInLastFrame() {
        addRolls(COMPLETE_GAME_WITH_TWO_STRIKES_IN_A_ROW_AND_TWO_ROLLS_IN_LAST_FRAME);
        thrown.expect(IllegalArgumentException.class);
        addRolls(5);
    }

    private void addRolls(int ... rolls) {
        for (int roll : rolls) {
            game.addRoll(roll);
        }
    }
}
