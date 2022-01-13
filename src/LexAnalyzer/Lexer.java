package LexAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.text.html.HTMLDocument.HTMLReader.CharacterAction;

public class Lexer {

    public enum State {
        INITIAL,    //Start
        Q1,         //Keywords, Datatype, Identifier, Boolean constant
        Q2,         //Integer Constant
        Q3,         //Operators

        Q4,         //Comment - MultiLine    
        Q5,         //Comment - Single-Line
        Q6,         //Character
        Q7,         //String
        Q8,         //Float Constant
        INVALID     //Unrecognized Token
    }


    private State curState = State.INITIAL;

    private Dictionary data = new Dictionary();
    private String lines;
    
    Lexer(String lines){
        this.lines = lines;
    }

    //Main Function
    ArrayList<Token> scan(){
        ArrayList<Token> tokens = new ArrayList<>();
        String lexeme = "";
        char c;
        for(int i = 0; i < lines.length();){
            c = lines.charAt(i);
            switch(curState){
                case INITIAL:
                    if(Character.isAlphabetic(c)){
                        curState = State.Q1;                        
                    }else if(Character.isDigit(c) ){
                        curState = State.Q2;
                    }else if(Character.isWhitespace(c)){
                        curState = State.INITIAL;
                        i++;
                    }else {
                        curState = State.Q3;
                    }
                    break;

                case Q1:
                    if(Character.isWhitespace(c)){
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else if(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' ){
                        lexeme += c;                        
                        curState = State.Q1;
                        i++;
                    }else{
                        //TO-DO
                        curState = State.INVALID;
                    }
                    break;

                case Q2:
                    
                    if(Character.isAlphabetic(c)){
                        curState = State.INVALID;                        
                    }else if(Character.isDigit(c)){
                        lexeme += c;                        
                        curState = State.Q2;
                        i++;                        
                    }else if(Character.isWhitespace(c)){
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else {
                        //TO-DO
                        if(c == '.'){
                            lexeme += c;                        
                            curState = State.Q8;
                            i++;
                        }
                        curState = State.INVALID;
                    }

                    break;

                case Q3:
                    String s = String.valueOf(c);
                    char s1 = lines.charAt(i+1);
                    char s2 = lines.charAt(i+2);

                    
                    if(data.validSymbols.contains(s)){
                        lexeme += s;                        
                        i++;
                        
                        if(Character.isWhitespace(s1)){
                            print(lexeme);
                            lexeme = "";
                            curState = State.INITIAL;  
                        }else if(Character.isAlphabetic(s1)){
                            print(lexeme);
                            lexeme = "";
                            curState = State.Q1;  
                        }else if(Character.isDigit(s1)){
                            print(lexeme);
                            lexeme = "";
                            curState = State.Q2;
                        }else{
                            if( (s+s1).equals("==") ||
                                (s+s1).equals("<=") ||
                                (s+s1).equals(">=") ||
                                (s+s1).equals("<>")                      
                            ){
                                lexeme += s1;
                                i++;
                                print(lexeme);
                                lexeme = "";
                                curState = State.INITIAL;                         
                            }else if((s+s1).equals("!*")){
                                lexeme += s1;
                                print(lexeme);
                                lexeme = "";
                                curState = State.INITIAL;  
                            }else if((s+s1+s2).equals("!**")){
                                lexeme += s1 + s2; 
                            }else {
                                curState = State.INVALID;
                            }                        
                        }

                    }else{
                        curState = State.INVALID;
                    }
                    

                    
                    break;
                case Q4:
                    
                    break;
                case Q5:
                    
                    break;
                case Q6:
                    
                    break;
                case Q7:
                    break;

                case Q8:
                    if(Character.isAlphabetic(c)){
                        curState = State.INVALID;                        
                    }else if(Character.isDigit(c)){
                        lexeme += c;                        
                        curState = State.Q8;
                        i++;                        
                    }else if(Character.isWhitespace(c)){
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else {
                        //TO-DO
                        curState = State.INVALID;
                    }
                    break;

                case INVALID:
                    if(Character.isWhitespace(c)){
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;
                        i++;
                    }else if(Character.isAlphabetic(c)){

                    }else if(Character.isDigit(c)){
                    }else if(!data.validSymbols.contains(String.valueOf(c)) ){
                        lexeme += c;
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;
                        i++;
                    }else{
                        lexeme += c;
                        i++;
                    }
                    break;    
            }
        
        }

        return tokens;
    }

    

    // void match(String line, String l){
    //     // Token token;
    //     // String lexeme;
    //     TokenType tokenType;
    //     // String description;        
        
    //     //For (5/7)
    //     System.out.print(l + "");

    //     if( data.operator.containsKey(l)){ 
    //         l = l.concat( String.valueOf(line.charAt(index + 1)));
    //         if(data.operator.containsKey(l)){
    //             index++;
    //             tokenType = TokenType.OPERATOR;
    //             System.out.println("\t\t M-Operator");
    //         }else{
    //             tokenType = TokenType.OPERATOR;
    //             System.out.println("\t\t Operator");
    //         }
    //     }else if(data.delimeter.containsKey(l)){
    //         tokenType = TokenType.DELIMETER_BRACKET;
    //         System.out.println("\t\t Delimeter");
    //     }else if(data.comment.containsKey(l)){
    //         tokenType = TokenType.COMMENT;
    //         System.out.println("\t\t Comment");
    //     }else{
            
    //     }
        

        // token = new Token(lexeme, tokenType, description);
    //     // return token;
    // }

    // Checks token match
    boolean isContains(String l, String [] data ){
        for(String x: data){
            if(x.startsWith(l)){
                System.out.println(" " + x);
                return true;
            }
        }

        return false;
    }

    void print(String str){
        System.out.println(str);
    }

}