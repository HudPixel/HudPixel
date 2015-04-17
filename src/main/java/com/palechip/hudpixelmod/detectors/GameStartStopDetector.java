package com.palechip.hudpixelmod.detectors;

import com.palechip.hudpixelmod.games.Game;

public class GameStartStopDetector {
    private GameDetector gameDetector;
    private boolean detectedStartBeforeGameDetection;

    public GameStartStopDetector(GameDetector gameDetector) {
        this.gameDetector = gameDetector;
    }
    public void onChatMessage(String textMessage, String formattedMessage) {
        // filter chat messages
        if(!this.isChatMessage(textMessage)) { 
            if(!this.gameDetector.getCurrentGame().equals(Game.NO_GAME)) {
                // check for starting
                if(!this.gameDetector.getCurrentGame().hasGameStarted()) {
                    if (textMessage.contains(this.gameDetector.getCurrentGame().getConfiguration().getStartMessage())) {
                        this.gameDetector.getCurrentGame().startGame();
                    }
                }
                // check for ending
                else {
                    // we don't want guild coins triggering the end message
                    if (!textMessage.toLowerCase().contains("guild coins") && textMessage.contains(this.gameDetector.getCurrentGame().getConfiguration().getEndMessage())) {
                        this.gameDetector.getCurrentGame().endGame();
                    }
                }
            }
        }
    }
    
    private boolean isChatMessage(String textMessage) {
        try {
            // normal chat message
            if(textMessage.split(" ")[0].endsWith(":") && !textMessage.split(" ")[0].endsWith("]:")) {
                return true;
            }
            // normal chat message when the player has a rank
            if(textMessage.split(" ")[1].endsWith(":") && !textMessage.split(" ")[1].endsWith("]:")) {
                return true;
            }
        } catch(Exception e) {
        }
        // other chat channels. ("staff" is just a guess)
        if(textMessage.toLowerCase().startsWith("guild") || textMessage.toLowerCase().startsWith("staff") || textMessage.toLowerCase().startsWith("party")) {
            return true;
        }
        return false;
    }
}
