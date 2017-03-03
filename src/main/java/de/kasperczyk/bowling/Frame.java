package de.kasperczyk.bowling;

import java.util.ArrayList;
import java.util.List;

class Frame {

    private List<Integer> pinsRolled;
    private int score;
    private int noOfBonusRolls;
    private boolean isLastFrame;

    int getScore() {
        return score;
    }

    Frame(boolean isLastFrame) {
        pinsRolled = new ArrayList<Integer>();
        noOfBonusRolls = 0;
        this.isLastFrame = isLastFrame;
    }

    void addRoll(int pins) {
        validateRoll(pins);
        pinsRolled.add(pins);
        score += pins;
        validateScore();
    }

    boolean isComplete() {
        if (!isLastFrame) {
            return pinsRolled.size() == 2 || isStrike();
        } else {
            int throwsInLastFrame = score >= 10 ? 3 : 2;
            return pinsRolled.size() == throwsInLastFrame;
        }
    }

    private boolean isStrike() {
        return pinsRolled.size() == 1 && pinsRolled.get(0) == 10;
    }

    boolean isUnhandledSpare() {
        return pinsRolled.size() == 2 && score == 10 && noOfBonusRolls == 0;
    }

    boolean isUnhandledStrike() {
        return pinsRolled.size() == 1 && score >= 10 && noOfBonusRolls <= 1;
    }

    void addToScore(int pins) {
        score += pins;
        noOfBonusRolls++;
    }

    private void validateScore() {
        if (score > 10 && pinsRolled.size() == 2 && !isLastFrame) {
            throw new IllegalArgumentException("You cannot score more than ten pins per frame.");
        }
    }

    private void validateRoll(int pins) {
        if (pins < 0 || pins > 10) {
            throw new IllegalArgumentException("You can only throw between zero and ten pins.");
        }
    }
}
