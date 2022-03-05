package Compiler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Statement {           
    
    public ArrayList<Token> tokens = new ArrayList<>();
    public int line;
    public boolean isValid;
    public String message;

    Statement(){
        this.isValid = true;        
        this.message = ""; 
    }

    String getInformation(){
        String syntax = new String();
        for(Token token: tokens){
            syntax += token.lexeme + " ";
        }

        String validity;
        if(isValid){
            validity = "Valid"; 
        }else{
            validity = "Invalid"; 
        }
        DecimalFormat F = new DecimalFormat("00");
                       
        return "L" + F.format(line) + " \t\t\t" + syntax + "\t\t" + validity + "\t\t\t" + message;
    }
}
