package LexAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {
    
    private boolean stringlock = false;
    private boolean commentlockS = false;
    private boolean commentlockM = false;
    private int index;
    private String s = "";

    private Dictionary data = new Dictionary();
    private ArrayList<String> lines;
    
    Lexer(ArrayList<String> lines){
        this.lines = lines;
    }

    //Main Function
    ArrayList<Token> execute(){
        ArrayList<Token> tokens = new ArrayList<>();
        ArrayList<String> lexemes = new ArrayList<>();

        int l = 0;                                                          //R
        for (String line : lines) {
            if(!line.isBlank() || stringlock || commentlockM){
                System.out.println("Line " + ++l + ": " + line);            //R
                lexemes.addAll(split(line));                
            }
        }
        if(!s.isEmpty() ){ 
            lexemes.add(s);
            s = "";
        }

        int t = 0;                                                  //R
        for (String string : lexemes) {                             //R
            System.out.println(++t + " " + string);                 //R
        }

        return tokens;
    }
    
    // Todo: FOR comments
    // Convert line string to lexemes    
    // ArrayList<String> split(String line){
    //     ArrayList<String> strings = new ArrayList<>();

    //     if(commentlockM && line.contains("*!")){
    //         s += line + "\n";
    //     }else if( line.contains("!** ") || 
    //         line.contains("\"")  || 
    //         line.contains("!*")  || 
    //         line.contains("*!")  ||
    //         stringlock
    //     ){  
    //         char c;

    //         for(int i = 0; i < line.length(); i++){
    //             c = line.charAt(i);

    //             if((commentlockS || stringlock) && (c != '*' && c != '"' )){
    //                 s += c ;
    //             }else if(Character.isWhitespace(c) && !s.isEmpty()){                    
    //                 strings.add(s);
    //                 s = "";
    //             }else {               
                    
    //                 if(c == '!' && line.charAt(i+1) == '*' ){
    //                     if(!s.isEmpty()){
    //                         strings.add(s);
    //                     }
    //                     if(line.charAt(i+2) == '*'){
    //                         s =  "!**";
    //                         commentlockM = true;
    //                         i = i+2;
    //                     }else{
    //                         s = "!*";
    //                         commentlockS = true;
    //                         i++;
    //                     }

    //                 }else if(c == '*' && line.charAt(i+1) == '!' ){
    //                     i++;
    //                     s += "*!";
    //                     strings.add(s);
    //                     s = "";
    //                     stringlock = true;
    //                 }else if(c == '\"'){
    //                     if(stringlock){
    //                         s += c;
    //                         strings.add(s);
    //                         s = "";
    //                         stringlock = false;
    //                     }else{
    //                         if(!s.isEmpty())
    //                             strings.add(s);                       
    //                         s = "" + c;
    //                         stringlock = true;
    //                     }
    //                 }
    //             }
                
    //         }
    //         if(!s.isEmpty() && !stringlock && !commentlockM){ 
    //             strings.add(s);
    //             s = "";
    //         }



    //     }else
    //         strings = new ArrayList<>(Arrays.asList(line.split(" ")));   

    //     return strings;
    // }


    //To-do: Lexemes to Tokens
    // ArrayList<Token> scan(String line){
    //     
    //     Token token;
    //     // Machine machine = new Machine();   


        
        
        
    //     System.out.println();
        

         
    //     return token;
    // }

    

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

    
}