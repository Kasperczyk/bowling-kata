package de.kasperczyk.bowling;

import java.util.List;

interface Game {

    void addRoll(int pins);
    List<Frame> getFrames();
    int getTotalScore();
    boolean isOver();
}
