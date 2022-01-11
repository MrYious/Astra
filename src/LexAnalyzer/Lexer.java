package LexAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

/*  TO-DO list
    
    [] string lock
    [] comment lock

    // E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\input.ast
*/


public class Lexer {
    
    boolean stringlock = false;
    boolean commentlock = false;
    int index;

    Dictionary data = new Dictionary();

    private ArrayList<String> lines;

    Lexer(ArrayList<String> lines){
        this.lines = lines;
    }

    ArrayList<Token> execute(){
        ArrayList<Token> tokens = new ArrayList<>();
        //  BlankF  False   skip
        //  BlankF  True    read
        //  BlankT  True    read
        //  BlankT  True    read
        for (String line : lines) {
            if(!line.isBlank()){
                // tokens.add(scan(line));
                scan(line);                
            }else{
                if(stringlock){
                    scan(line); 
                }
            }
        }

        return tokens;
    }

    void scan(String line){
        //To-do
        // Token token;
        // Machine machine = new Machine();   

        // for (String string : lines.split"") {
            
        // }

        ArrayList<String> strings;
        System.out.println(line);
        strings = split(line);
        
        int i = 0;
        for (String string : strings) {
            System.out.println(++i + " " + string);
        }
        
        System.out.println();
        

         
        // return token;
    }

    

    void match(String line, String l){
        // Token token;
        // String lexeme;
        TokenType tokenType;
        // String description;        
        
        //For (5/7)
        System.out.print(l + "");

        if( data.operator.containsKey(l)){ 
            l = l.concat( String.valueOf(line.charAt(index + 1)));
            if(data.operator.containsKey(l)){
                index++;
                tokenType = TokenType.OPERATOR;
                System.out.println("\t\t M-Operator");
            }else{
                tokenType = TokenType.OPERATOR;
                System.out.println("\t\t Operator");
            }
        }else if(data.delimeter.containsKey(l)){
            tokenType = TokenType.DELIMETER_BRACKET;
            System.out.println("\t\t Delimeter");
        }else if(data.comment.containsKey(l)){
            tokenType = TokenType.COMMENT;
            System.out.println("\t\t Comment");
        }else{
            
        }
        

        // token = new Token(lexeme, tokenType, description);
        // return token;
    }

    // public enum State {
    //     START, Q1, Q2, Q3, Q4, Q5, Q6, Q7, INVALID
    // }
    
    
    boolean isValidCharacter(char c){
        if( data.validChars.contains(String.valueOf(c)) ||  
            Character.isLetter(c) || 
            Character.isWhitespace(c)
        ){                
            return true;
        }
        return false;
    }

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

    // Convert string to array of possible lexemes
    ArrayList<String> split(String line){
        ArrayList<String> strings = new ArrayList<>();

        if(line.matches("(.*)\"(.*)")){
            boolean spacelock = false;
            String s = "";
            char c;

            for(int i = 0; i < line.length(); i++){
                c = line.charAt(i);

                if(!Character.isWhitespace(c) || spacelock){
                    if(c == '\"'){
                        if(spacelock){
                            s += c;
                            strings.add(s);
                            s = "";
                            spacelock = false;
                        }else{
                            if(!s.isEmpty())
                                strings.add(s);                       
                            s = "" + c;
                            spacelock = true;
                        }                       

                    }else 
                        s += c;
                    

                }else{
                    strings.add(s);
                    s = "";
                }
            }
            
            if(!s.isEmpty()){ 
                strings.add(s);
            }

        }else
            strings = new ArrayList<>(Arrays.asList(line.split(" "))); 
                
        return strings;
    }
}


    