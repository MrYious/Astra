package Compiler;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

public class Parser {
    private ArrayList <Statement> statements;
    private Statement statement;

    private Stack <Token> s_tokens;

    private Token current;
    private Token next;

    private boolean end = false;
    private boolean hold = false;
    private boolean ended = false;

    Parser(ArrayList<Token> tokens){
        this.s_tokens = new Stack<>();
        this.statement = new Statement();
        this.statements = new ArrayList<>();

        for ( ListIterator<Token> lit = tokens.listIterator( tokens.size() ); lit.hasPrevious(); )
            this.s_tokens.add(lit.previous());
    }
    
    public ArrayList<Statement> execute() {      
        current = s_tokens.pop();
        next = s_tokens.peek();        
        stmt();

        return statements;
    }
    
    //Productions
    private void stmt(){
        Token token = current;
        if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("{")){
            consume();
            hold = true;
            stmt();
            stmt_p();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("}")){                                    
                hold = false;
                consume();
                endStatement();
            }else{
                hold = false;
                errorRecover('}');
            }                 
        }else if(current.token == TokenType.KEYWORD && current.lexeme.equals("for")){
            consume();
            if(isIdentifier(current)){
                consume();
                if(current.token == TokenType.KEYWORD && current.lexeme.equals("in")){
                    consume();
                    if(current.token == TokenType.KEYWORD && current.lexeme.equals("range")){
                        consume();
                        if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("(")){
                            consume();
                            if(isVar(current)){
                                consume();
                                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                                    consume();
                                    if(current.token == TokenType.KEYWORD && current.lexeme.equals("do")){
                                        consume();
                                        stmt();
                                    }else{                                        
                                        errorRecover(current.lexeme);
                                    }
                                }else{
                                    errorRecover(')');
                                }
                            }else{                                
                                errorRecover(TokenType.INDENTIFIER);
                            }
                        }else{
                            errorRecover('(');
                        }
                    }else{                        
                        errorRecover(current.lexeme);
                    }
                }else{
                    errorRecover(current.lexeme);
                }
            }else{
                errorRecover(TokenType.INDENTIFIER);
            }
        }else if(current.token == TokenType.KEYWORD && current.lexeme.equals("if")){
            consume();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("(")){
                consume();
                exp(current);
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                    consume();
                    if(current.token == TokenType.KEYWORD && current.lexeme.equals("then")){
                        consume();
                        hold = true;                        
                        stmt();
                        else_s(current);                        
                    }else{
                        errorRecover(current.lexeme);
                    }
                }else{
                    errorRecover(')');
                }
            }else{
                errorRecover('(');
            }
        }else if(current.token == TokenType.KEYWORD && current.lexeme.equals("print")){
            consume();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("(")){
                consume();
                isO_stmt(current);
            }else{
                errorRecover('(');
            }
        }else if(isDatatype(current)){
            consume();
            dec_stmt(current);
            if(!ended){
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";") ){
                    consume();
                    endStatement();
                }else{
                     errorTerminator();
                }
            }else{                
                end = false;
            }
            
        }else if(isIdentifier(current)){
            consume(); 
            if(current.token == TokenType.OPERATOR && current.lexeme.equals("=")){
                consume();
                value(current);
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";")){
                    consume();
                    endStatement();
                }else{
                     errorTerminator();
                }
            }else if (isIdentifier(current) && (next.token == TokenType.OPERATOR && next.lexeme.equals("="))){
                errorRecover(token.lexeme);
            }else if ((current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("("))){
                errorRecover(token.lexeme);
            }else{
                errorRecover('=');
            }
        }else {            
            errorRecover();
        }
    }

    private void stmt_p() {
        if(!(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("}")) && !s_tokens.empty()){
            stmt();
            stmt_p();
        }
    }

    private void else_s(Token token) {
        if(token.token == TokenType.KEYWORD && token.lexeme.equals("else")){
            consume();
            stmt();
            hold = false;
            endStatement();
        }else{
            hold = false;
            endStatement();
        }
    }

    private void isO_stmt(Token token) {
        if(isVar(token) && (next.token == TokenType.DELIMETER_BRACKET && next.lexeme.equals(")"))){
            consume();
            consume();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";")){
                consume();
                endStatement();
            }else{
                errorTerminator();
            }
        }else if((current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")) && (next.token == TokenType.DELIMETER_BRACKET && next.lexeme.equals(";"))){
            consume();
            consume();            
            endStatement();
        }else{
            hold = true;
            exp(token);
            hold = false;
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                consume();
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";")){
                    consume();
                    endStatement();
                }else{            
                     errorTerminator();
                }
            }else{
                errorRecover(')');
            }   
        }
    }

    private void dec_stmt(Token token){
        if(isIdentifier(token)){            
            consume();
            ass_stmt(current);
            x(current);   
        }else{
            errorRecover(TokenType.INDENTIFIER);
        }
    }

    private void x(Token token) {
        if(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(",")){
            if(current.token == TokenType.DELIMETER_BRACKET && next.lexeme.equals(";")){
                errorRecover(TokenType.INDENTIFIER);
            }else{
                consume();
                dec_stmt(current);
            }
        }else if (!(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(";"))){
            ended = true;
             errorTerminator();
        }        
    }

    private void ass_stmt(Token token){
        if(token.token == TokenType.OPERATOR && token.lexeme.equals("=")){
            consume();
            value(current);
        }else if (!(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(",")) &&
                !(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(";"))
        ){  
            ended = true;
            errorRecover('=');
        }
    }

    private void value(Token token){
        if(token.token == TokenType.KEYWORD && token.lexeme.equals("scan")){
            consume();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("(")){
                consume();
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                    consume();
                }else{                
                    errorRecover(')');
                }                
            }else{                
                errorRecover('(');
            }
        }else{            
            exp(token);
        }
    }

    private void exp(Token token){
        if(isVar(token)){
            consume();            
            exp_p(current);
        }else if(current.token == TokenType.OPERATOR && token.lexeme.equals("!")){
            consume();
            exp(current);
        }else if(current.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals("(")){
            consume();
            exp(current);
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                consume();
            }else{
                errorRecover(')');
            }
            exp_p(current);
        }else { 
            errorRecover(token.lexeme);
        }
        
    }

    private void exp_p(Token token){
        if(token.token == TokenType.OPERATOR){
            consume();
            exp(current);            
        }
    }
   
    //ERROR METHODS

    void errorRecover(){
        statement.isValid = false;
        statement.message = "Invalid Syntax | Token";
        while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty()){
            consume();
        }
        consume();
        endStatement();
    }

    void errorRecover(char ch){
        statement.isValid = false;
        statement.message = "Expected/Missing '" + ch + "'" ;
        while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty()){
            consume();
        }
        consume();
        endStatement();
    }

    void errorRecover(String str){
        statement.isValid = false;
        statement.message = "Unknown Syntax \"" + str + "\"";
        while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty()){
            consume();
        }
        consume();
        endStatement();
    }

    void errorRecover(TokenType type){
        statement.isValid = false;
        statement.message = "Expected " + type;        
        while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty()){
            consume();
        }
        consume();
        endStatement();
    }

    void errorRecover(String str, Boolean bool){
        statement.isValid = false;
        statement.message = "Invalid Token \"" + str + "\"";        
        while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty()){
            consume();
        }
        consume();
        endStatement();
    }

    void errorTerminator(){
        statement.isValid = false;
        statement.message = "Expected/Missing ';'" ;
        endStatement();
    }

    void consume(){
        if(!end){
            statement.tokens.add(current);
            if(!s_tokens.empty()){            
                current = s_tokens.pop();
                while(current.token == TokenType.COMMENT && !s_tokens.empty()){
                    current = s_tokens.pop();
                }

                if(current.token == TokenType.INVALID){
                    if(hold){
                        hold = false;
                    }
                    errorRecover(current.lexeme, true);
                }

                if(!s_tokens.empty())
                    next = s_tokens.peek();
            }else{
                end = true;
            }
        }
        
    }

    void endStatement(){
        if((statement.tokens.size() != 0 && !hold) || !s_tokens.empty()){   //false-> true
            if(!hold && !current.lexeme.equals("else")){
                statement.line = statement.tokens.get(0).line;
                statements.add(statement);            
                statement = new Statement();   

                if(!s_tokens.empty()){                        
                    stmt();                
                    end = false;
                    hold = false;
                    ended = false;
                }
            }
        }
    }

    boolean isIdentifier(Token token){
        if(token.token == TokenType.INDENTIFIER){
            return true;
        }
        return false;
    }
    
    boolean isConstant(Token token){
        if(token.token == TokenType.CONSTANT){
            return true;
        }else if(token.lexeme.equals("\"") || token.lexeme.equals("'") ){            

            consume();
            consume();
            return true;    
        }
        return false;
    }
    
    boolean isComment(Token token){
        if(token.token == TokenType.COMMENT){
            return true;
        }
        return false;
    }
    
    boolean isInvalid(Token token){
        if(token.token == TokenType.INVALID){
            return true;
        }
        return false;
    }
    
    boolean isDatatype(Token token){
        if(token.token == TokenType.DATATYPE){
            return true;
        }
        return false;
    }
    
    boolean isVar(Token token){
        
        if(isIdentifier(token) || isConstant(token)){
            return true;
        }
        return false;
    }
 
}
