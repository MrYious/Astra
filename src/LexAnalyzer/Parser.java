package LexAnalyzer;

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
    private boolean fix = false;
    private boolean con = true;

    //Try input for errors

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
        while(!s_tokens.empty()){            
            System.out.println("Current: " + current.lexeme + " | " + current.token);
            System.out.println("Statement: " + statement.isValid);
            stmt();
        }

        return statements;
    }

    //Productions

    void stmt(){
        Token token = current;
        if(con){
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("{")){
                consume();
                hold = true;
                stmt();
                stmt_p();
                if(con){
                    if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("}")){
                        consume();
                        endStatement();
                    }else{
                        errorRecover('}');
                    }                    
                    hold = false;
                }else{
                    con = true;
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
                    if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")") && con){
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
                        con = true;
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
                if(!ended && con){                
                    System.out.println("Entered 1 ");
                    if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";") ){
                        consume();
                        endStatement();
                    }else{
                        errorRecover(';');
                    }
                }else{                
                    System.out.println("Entered 2");
                    end = false;
                    con = true;
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
                        errorRecover(';');
                    }
                }else if (isIdentifier(current) && (next.token == TokenType.OPERATOR && next.lexeme.equals("="))){
                    errorRecover(token.lexeme);
                }else if ((current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("("))){
                    errorRecover(token.lexeme);
                }else{
                    System.out.println(current.lexeme + " " + current.token);
                    System.out.println(next.lexeme + " " + next.token);
                    errorRecover('=');
                }
            }else {
                errorRecover();
            }
        }
    }

    private void stmt_p() {
        if(!(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals("}")) && !s_tokens.empty() && con){
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

    //in EXP
    private void isO_stmt(Token token) {
        if(isVar(token) && (next.token == TokenType.DELIMETER_BRACKET && next.lexeme.equals(")"))){
            consume();
            consume();
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";")){
                consume();
                endStatement();
            }else{
                errorRecover(';');
            }
        }else{
            hold = true;
            exp(token);
            hold = false;
            if(con){
                if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")")){
                    consume();
                    if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(";")){
                        consume();
                        endStatement();
                    }else{            
                        errorRecover(';');
                    }
                }else{
                    errorRecover(')');
                }
            }else{                
                con = true; 
            }           
        }
    }

    void dec_stmt(Token token){
        if(con){
            if(isIdentifier(token)){            
                consume();
                ass_stmt(current);
                if(con){                
                    x(current);      
                }  
            }else{
                errorRecover(TokenType.INDENTIFIER);
                con = false;
            }
        }
    }

    private void x(Token token) {
        if(con){
            if(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(",")){
                if(current.token == TokenType.DELIMETER_BRACKET && next.lexeme.equals(";")){
                    errorRecover(TokenType.INDENTIFIER);
                }else{
                    consume();
                    dec_stmt(current);
                }
            }else if (!(token.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals(";"))){
                ended = true;
                errorRecover(';');
            }
        }else {
            con = true;
        }
        
    }

    void ass_stmt(Token token){
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

    void value(Token token){
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

    void exp(Token token){
        if(isVar(token)){
            consume();
            exp_p(current);
        }else if(current.token == TokenType.OPERATOR && token.lexeme.equals("!")){
            consume();
            exp(current);
        }else if(current.token == TokenType.DELIMETER_BRACKET && token.lexeme.equals("(")){
            consume();
            exp(current);
            if(current.token == TokenType.DELIMETER_BRACKET && current.lexeme.equals(")") && con){
                consume();
            }else{
                con = false;
                errorRecover(')');
            }
            exp_p(current);
        }else { 
            errorRecover(token.lexeme);
            con = false;
        }
        
    }

    void exp_p(Token token){
        if(token.token == TokenType.OPERATOR && con){
            consume();
            exp(current);            
        }
    }
   
    //FUNCTIONS

    void errorRecover(){
        statement.isValid = false;
        if(!fix){
            statement.message = "Invalid Syntax | Token";
        }
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void errorRecover(char ch){
        statement.isValid = false;
        if(!fix){
            statement.message = "Expected/Missing '" + ch + "'" ;
        }
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void errorRecover(String str){
        statement.isValid = false;
        if(!fix){
            statement.message = "Unknown Syntax \"" + str + "\"";
        }
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void errorRecover(TokenType type){
        statement.isValid = false;
        if(!fix){
            statement.message = "Expected " + type;
        }
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void errorRecover(String str, Boolean bool){
        statement.isValid = false;
        if(!fix){
            statement.message = "Invalid Token \"" + str + "\"";
        }
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void consume(){
        if(!end && con && s_tokens.empty()){
            statement.tokens.add(current);
            if(!s_tokens.empty()){            
                current = s_tokens.pop();
                while(current.token == TokenType.COMMENT && !s_tokens.empty()){
                    current = s_tokens.pop();
                }

                if(current.token == TokenType.INVALID){
                    errorRecover(current.lexeme, true);
                    fix = true;
                    con = false;
                }

                if(!s_tokens.empty())
                    next = s_tokens.peek();
            }else{
                end = true;
            }
        }
        
    }

    void endStatement(){
        if(statement.tokens.size() != 0 && !hold){   
            //System.out.println(current.lexeme);        
            //System.out.println("FSTMT: " + statement.isValid + " | " + statement.message + "\n"); 
            statement.line = statement.tokens.get(0).line;
            fix = false;
            statements.add(statement);
            statement = new Statement();
            stmt();
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
