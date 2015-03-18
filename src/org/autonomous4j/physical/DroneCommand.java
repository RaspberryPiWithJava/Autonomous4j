/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autonomous4j.physical;

/**
 *
 * @author mark
 */
public enum DroneCommand {
    FORWARD ("F:", "Forward"),
    BACK ("B:", "Backward"),
    LEFT ("L:", "Turn Left"),
    RIGHT ("R:", "Turn Right"),
    STOP ("S:", "Stop"),
    PINGF ("f:", "Ping Forward"),
    PINGL ("l:", "Ping Left"),
    PINGR ("r:", "Ping Right");

    private final String command;
    private final String description;
    
    DroneCommand(String command, String description) {
        this.command = command;
        this.description = description;
    }
    
    public String getCommand() {
        return command;
    }
    
    public String getDescription() {
        return description;
    }
}
