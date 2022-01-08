package LexAnalyzer;

public class Token {
    
    private String lexeme;
    private TokenType token;
    private String description;

    Token (String lexeme, TokenType token, String description){
        this.lexeme = lexeme;
        this.token = token;
        this.description = description;
    }

    void print(){
        System.out.print(
            lexeme + "\t" + token + "\t" + description
        );
    }

}
