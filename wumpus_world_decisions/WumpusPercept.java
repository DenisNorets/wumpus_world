package wumpus_world_decisions;

import aima.core.agent.Percept;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The agent has five sensors, each of which gives a single bit of information:
 * <ul>
 * <li>In the square containing the wumpus and in the directly (not diagonally)
 * adjacent squares, the agent will perceive a Stench.</li>
 * <li>In the squares directly adjacent to a pit, the agent will perceive a
 * Breeze.</li>
 * <li>In the square where the gold is, the agent will perceive a Glitter.</li>
 * <li>When an agent walks into a wall, it will perceive a Bump.</li>
 * <li>When the wumpus is killed, it emits a woeful Scream that can be perceived
 * anywhere in the cave.</li>
 * </ul>
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */

public class WumpusPercept implements Percept {
    private boolean stench;
    private boolean breeze;
    private boolean glitter;
    private boolean scream;
    private boolean bump;

    private String[] breatheVar = new String[]{"I feel breeze here.",  "It’s a cool breeze here."};
    private String[] stenchVar = new String[]{"I feel stench here.", "It’s a stench here."};
    private String[] glitterVar = new String[]{"I see glitter here.", "It’s a glitter here."};
    private String[] screamVar = new String[]{"I hear scream.", "It’s a scream here."};
    private String[] bumpVar = new String[]{"I heared bump."};

    public WumpusPercept setStench() {
        stench = true;
        return this;
    }

    public WumpusPercept setBreeze() {
        breeze = true;
        return this;
    }

    public WumpusPercept setGlitter() {
        glitter = true;
        return this;
    }

    public WumpusPercept setScream() {
        scream = true;
        return this;
    }
    
	public WumpusPercept setBump() {
		bump = true;
		return this;
	}

    public boolean isStench() {
        return stench;
    }

    public boolean isBreeze() {
        return breeze;
    }

    public boolean isGlitter() {
        return glitter;
    }

    public boolean isScream() {
        return scream;
    }
    
	public boolean isBump() {
		return bump;
	}

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        int ran = 0;
        if (breeze) {
            ran = (int)Math.floor(Math.random() * breatheVar.length);
            result.append(breatheVar[ran]); }
        if (stench) {
            ran = (int)Math.floor(Math.random() * stenchVar.length);
            result.append(stenchVar[ran]); }
        if (glitter) {
            ran = (int)Math.floor(Math.random() * glitterVar.length);
            result.append(glitterVar[ran]); }
        if (scream) {
            ran = (int)Math.floor(Math.random() * screamVar.length);
            result.append(screamVar[ran]); }
        if (bump) {
            ran = (int)Math.floor(Math.random() * bumpVar.length);
            result.append(bumpVar[ran]); }
        return result.toString();
    }
}