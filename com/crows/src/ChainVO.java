package com.crows.src;

import java.util.ArrayList;
import java.util.List;

public class ChainVO {
    private boolean isReturning;
    private int[] index;
    private List<Integer> chains;

    // constructor
    public ChainVO() {
        isReturning = false;
        index = new int[2];
        chains = new ArrayList<Integer>();
    }

    // methods
    public void setI(int i) {
        index[0] = i;
    }

    public void setJ(int j) {
        index[1] = j;
    }

    public int getI() {
        return index[0];
    }

    public int getJ() {
        return index[1];
    }

    public void addChain(int chain) {
        chains.add(chain);
    }

    public void setChainVO(int i, int j, List<Integer> chains) {
        setI(i);
        setJ(j);
        this.chains = chains;
    }

    // getters and setters
    public boolean isReturning() {
        return isReturning;
    }

    public void setReturning(boolean returning) {
        isReturning = returning;
    }

    public int[] getIndex() {
        return index;
    }

    public void setIndex(int[] index) {
        this.index = index;
    }

    public List<Integer> getChains() {
        return chains;
    }

    public void setChains(List<Integer> chains) {
        this.chains = chains;
    }

    @Override
    public String toString() {
        return "ChainVO{index: [" + index[0] + ", " + index[1]
                + "], chains: " + chains.toString() + "}";
    }
}
