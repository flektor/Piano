/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Synthesizer;

import synthesizer.Key;

public interface KeyListener {

    public void keyPressed(Key key);

    public void keyReleased(Key key);
    
    public void keyEntered(Key key);

    public void keyExited(Key key);
}
