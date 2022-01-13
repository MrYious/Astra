package LexAnalyzer;

import javax.sound.sampled.Line;

public class Machine {
    
    public enum State {
        INITIAL, Q1, Q2, Q3, Q4, Q5, Q6, Q7, INVALID        
    }

    
    State nextTransition( String lines ){
        switch(line){
            default:
                return State.INVALID;
        }
    }




}
    