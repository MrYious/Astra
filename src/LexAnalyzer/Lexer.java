package LexAnalyzer;

import java.util.ArrayList;


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
    ArrayList<Token> execute(){
        ArrayList<Token> tokens = new ArrayList<>();
        String lexeme = "";
        char c;
        String desc = "";
        for(int i = 0; i < lines.length();){
            c = lines.charAt(i);
            switch(curState){
                case INITIAL:   //INITIAL
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

                case Q1:        //Keywords, Datatype, Identifier, Boolean constant
                    if(Character.isWhitespace(c)){                        
                        if(data.keyword.containsKey(lexeme)){
                            desc = data.keyword.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.KEYWORD, desc));
                        }else if(data.datatype.containsKey(lexeme)){
                            desc = data.datatype.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.DATATYPE, desc));
                        }else if(data.bool.containsKey(lexeme)){
                            desc = data.bool.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                        }else {
                            desc = "Variable '" + lexeme + "'";
                            tokens.add(new Token(lexeme, TokenType.INDENTIFIER, desc));
                        }
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else if(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_' ){
                        lexeme += c;                        
                        curState = State.Q1;
                        i++;
                    }else{
                        if(data.validSymbols.contains(String.valueOf(c))){
                            if(data.keyword.containsKey(lexeme)){
                                desc = data.keyword.get(lexeme);
                                tokens.add(new Token(lexeme, TokenType.KEYWORD, desc));
                            }else if(data.datatype.containsKey(lexeme)){
                                desc = data.datatype.get(lexeme);
                                tokens.add(new Token(lexeme, TokenType.DATATYPE, desc));
                            }else if(data.bool.containsKey(lexeme)){
                                desc = data.bool.get(lexeme);
                                tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                            }else {
                                desc = "Variable '" + lexeme + "'";
                                tokens.add(new Token(lexeme, TokenType.INDENTIFIER, desc));
                            }
                            print(lexeme);
                            lexeme = "";
                            curState = State.INITIAL;
                        }else
                            curState = State.INVALID;
                    }
                    break;

                case Q2:        //Integer Constant                 
                    if(Character.isAlphabetic(c)){
                        curState = State.INVALID;                        
                    }else if(Character.isDigit(c)){
                        lexeme += c;                        
                        curState = State.Q2;
                        i++;                        
                    }else if(Character.isWhitespace(c)){
                        desc = "Integer Constant Value";
                        tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else {                    
                        if(c == '.'){
                            lexeme += c;                        
                            curState = State.Q8; 
                            i++;
                        }else if(data.validSymbols.contains(String.valueOf(c))){
                            desc = "Integer Constant Value";
                            tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                            print(lexeme);
                            lexeme = "";
                            curState = State.INITIAL;
                        }else{
                            desc = "Integer Constant Value";
                            tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                            print(lexeme);
                            lexeme = "";  
                            curState = State.INVALID;
                        }
                    }
                    break;

                case Q3:        //OPERATORS
                    String s = String.valueOf(c);
                    char s1 = lines.charAt(i+1);
                    char s2 = lines.charAt(i+2);

                    if( Character.isAlphabetic(c)   ||
                        Character.isDigit(c)        ||
                        Character.isWhitespace(c)
                    ){  
                        if(data.operator.containsKey(lexeme)){
                            desc = data.operator.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.OPERATOR, desc));
                        }else if(data.delimeter.containsKey(lexeme)){
                            desc = data.delimeter.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                        }                        
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL; 
                    }else {
                        if(data.validSymbols.contains(s) && lexeme.isEmpty()){
                            if( (s+s1).equals("==") ||
                                (s+s1).equals("<=") ||
                                (s+s1).equals(">=") ||
                                (s+s1).equals("<>")                      
                            ){  
                                lexeme = s + s1;
                                if(data.operator.containsKey(lexeme)){
                                    desc = data.operator.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.OPERATOR, desc));
                                }                                
                                i += 2;
                                print(lexeme);
                                lexeme = "";
                                curState = State.INITIAL;                         
                            }else if(c == '(' || c == ')' || c == '{' || c == '}'){
                                lexeme = s;
                                if(data.delimeter.containsKey(lexeme)){
                                    desc = data.delimeter.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                                }
                                i++;
                                print(lexeme);
                                lexeme = "";
                                curState = State.INITIAL;  
                            }else if(c == '"'){                                
                                lexeme = s;
                                if(data.delimeter.containsKey(lexeme)){
                                    desc = data.delimeter.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                                }
                                i++;
                                print(lexeme);
                                lexeme = "";
                                curState = State.Q7;  
                            }else if(c == '\''){
                                lexeme = s;
                                if(data.delimeter.containsKey(lexeme)){
                                    desc = data.delimeter.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                                }
                                i++;
                                print(lexeme);
                                lexeme = "";
                                curState = State.Q6;  
                            }else if((s+s1+s2).equals("!**")){
                                lexeme += s + s1 + s2;
                                if(data.comment.containsKey(lexeme)){
                                    desc = data.comment.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.COMMENT, desc));
                                }
                                i += 3;
                                print(lexeme);
                                lexeme = "";
                                curState = State.Q4; 
                            }else if((s+s1).equals("!*")){                                
                                lexeme += s + s1;
                                if(data.comment.containsKey(lexeme)){
                                    desc = data.comment.get(lexeme);
                                    tokens.add(new Token(lexeme, TokenType.COMMENT, desc));
                                }
                                i += 2;
                                print(lexeme);
                                lexeme = "";
                                curState = State.Q5;  
                            }else{
                                lexeme += c;                        
                                curState = State.Q3;
                                i++;
                            }
                        }else{
                            curState = State.INVALID;
                        }
                    }                     
                    break;

                case Q4:        //Comment - MultiLine                    
                    if( c == '*' && lines.charAt(i+1) == '!'){
                        desc = "Comment Contents";
                        tokens.add(new Token(lexeme, TokenType.COMMENT, desc));
                        print(lexeme);
                        lexeme = c + "" + lines.charAt(i+1);
                        if(data.comment.containsKey(lexeme)){
                            desc = data.comment.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.COMMENT, desc));
                        }  
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i += 2;
                    }else{
                        lexeme += c;                        
                        curState = State.Q4;
                        i++;
                    }

                    break;

                case Q5:        //Comment - SingleLine
                    if( c == '\n'){
                        desc = "Comment Contents";
                        tokens.add(new Token(lexeme, TokenType.COMMENT, desc));
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else{
                        lexeme += c;                        
                        curState = State.Q5;
                        i++;
                    }
                    break;
                    
                case Q6:        //Character 
                    if( c == '\''){
                        desc = "Character Constant Value";
                        tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                        print(lexeme);
                        lexeme = String.valueOf(c);
                        if(data.delimeter.containsKey(lexeme)){
                            desc = data.delimeter.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                        }
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else{
                        lexeme += c;                        
                        curState = State.Q6;
                        i++;
                    }
                    break;
                case Q7:        //String 
                    if( c == '"'){
                        desc = "String Constant Value";
                        tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                        print(lexeme);
                        lexeme = String.valueOf(c);
                        if(data.delimeter.containsKey(lexeme)){
                            desc = data.delimeter.get(lexeme);
                            tokens.add(new Token(lexeme, TokenType.DELIMETER_BRACKET, desc));
                        }
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else{
                        lexeme += c;                        
                        curState = State.Q7;
                        i++;
                    }
                    break;

                case Q8:
                    if(Character.isAlphabetic(c)){
                        curState = State.INVALID;                        
                    }else if(Character.isDigit(c)){
                        lexeme += c;                        
                        curState = State.Q8;
                        i++;                        
                    }else if(Character.isWhitespace(c)){
                        desc = "Float Constant Value";
                        tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                        print(lexeme);
                        lexeme = "";
                        curState = State.INITIAL;                        
                        i++;
                    }else {
                        if(data.validSymbols.contains(String.valueOf(c))){
                            desc = "Float Constant Value";
                            tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                            print(lexeme);
                            lexeme = "";
                            curState = State.INITIAL;
                        }else{
                            desc = "Float Constant Value";
                            tokens.add(new Token(lexeme, TokenType.CONSTANT, desc));
                            print(lexeme);
                            lexeme = "";  
                            curState = State.INVALID;
                        }
                    }
                    break;

                case INVALID:
                    if(Character.isWhitespace(c)){
                        desc = "Unrecognized Token";
                        tokens.add(new Token(lexeme, TokenType.INVALID, desc));
                        print(lexeme + "Invalid");
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


    int i = 0;
    void print(String str){
        System.out.println( ++i + ": " + str);
    }

}