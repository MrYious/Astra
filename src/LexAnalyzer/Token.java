package LexAnalyzer;

public class Token {
    
    private String lexeme;              
    private TokenType token;
    private String description;

    Token(String lexeme, TokenType token, String description){
        this.lexeme = lexeme;
        this.token = token;
        this.description = description;
    }

    String getInformation(){
        return lexeme + "\t\t\t\t" + token + "\t\t\t\t" + description;
    }

}
