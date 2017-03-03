package de.kasperczyk.bowling;

import java.util.ArrayList;
import java.util.List;

class GameImpl implements Game {

    private List<Frame> frames;
    private int currentFrameIndex;

    public List<Frame> getFrames() {
        return frames;
    }

    GameImpl() {
        frames = new ArrayList<Frame>();
        createEmptyFrames();
    }

    private void createEmptyFrames() {
        for (int i = 0; i < 9; i++) {
            frames.add(new Frame(false));
        }
        frames.add(new Frame(true));
    }

    public void addRoll(int pins) {
        if (isOver()) {
            throw new IllegalArgumentException("Game is already over.");
        }
        setCurrentFrameIndex();
        if (currentFrameIndex != 0) {
            Frame previousFrame = frames.get(currentFrameIndex - 1);
            handleSpare(pins, previousFrame);
            handleStrike(pins, previousFrame);
        }
        frames.get(currentFrameIndex).addRoll(pins);
    }

    private void handleSpare(int pins, Frame previousFrame) {
        if (previousFrame.isUnhandledSpare()) {
            previousFrame.addToScore(pins);
        }
        if (currentFrameIndex > 1) {
            Frame twoFramesBack = frames.get(currentFrameIndex - 2);
            if (twoFramesBack.isUnhandledStrike()) {
                twoFramesBack.addToScore(pins);
            }
        }
    }

    private void handleStrike(int pins, Frame previousFrame) {
        if (previousFrame.isUnhandledStrike()) {
            previousFrame.addToScore(pins);
        }
    }

    private void setCurrentFrameIndex() {
        for (Frame frame : frames) {
            if (!frame.isComplete()) {
                currentFrameIndex = frames.indexOf(frame);
                break;
            }
        }
    }

    public int getTotalScore() {
        int totalScore = 0;
        for (Frame frame : frames) {
            totalScore += frame.getScore();
        }
        return totalScore;
    }

    public boolean isOver() {
        boolean isOver = true;
        for (Frame frame : frames) {
            if (!frame.isComplete()) {
                isOver = false;
            }
        }
        return isOver;
    }
}
