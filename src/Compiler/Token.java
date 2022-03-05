package Compiler;

public class Token {
    
    public String lexeme;              
    public TokenType token;
    public String description;
    public int line;

    Token(String lexeme, TokenType token, String description, int line){
        this.lexeme = lexeme;
        this.token = token;
        this.description = description;
        this.line = line;
    }

    String getInformation(){
        return "Line: " + line + "\t \t" + lexeme + "\t \t \t \t" + token + "\t \t \t " + description;
    }

}
