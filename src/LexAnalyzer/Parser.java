package LexAnalyzer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ListIterator;
import java.util.Set;
import java.util.Stack;

public class Parser {
    private ArrayList <Statement> statements;
    private Statement statement;

    private ArrayList <Token> tokens;    
    private Stack <Token> s_tokens;

    private Token current;
    private Token next;
    private boolean end = false;

    private boolean hold = false;
    private boolean ended = false;

    Parser(ArrayList<Token> tokens){
        this.tokens = tokens;
        this.s_tokens = new Stack();
        this.statement = new Statement();
        this.statements = new ArrayList<>();

        for ( ListIterator<Token> lit = tokens.listIterator( tokens.size() ); lit.hasPrevious(); )
            this.s_tokens.add(lit.previous());
    }

    public ArrayList<Statement> execute() {
        // for(Token token: tokens){
        //     System.out.println(token.lexeme);
        // }
        
        current = s_tokens.pop();
        next = s_tokens.peek();
        while(!s_tokens.empty()){
            stmt();
        }

        return statements;
    }

    //Productions

    void stmt(){
        if(current.lexeme.equals("{")){
            consume();
            hold = true;
            stmt();
            stmt_p();
            if(current.lexeme.equals("}")){
                hold = false;
                consume();
                endStatement();
            }else{
                hold = false;
                errorRecover("}");
            }
        }else if(current.lexeme.equals("for")){
            consume();
            if(isIdentifier(current)){
                consume();
                if(current.lexeme.equals("in")){
                    consume();
                    if(current.lexeme.equals("range")){
                        consume();
                        if(current.lexeme.equals("(")){
                            consume();
                            if(isVar(current)){
                                consume();
                                if(current.lexeme.equals(")")){
                                    consume();
                                    if(current.lexeme.equals("do")){
                                        consume();
                                        stmt();
                                    }else{                                        
                                        errorRecover(current.lexeme,true);
                                    }
                                }else{
                                    errorRecover(")");
                                }
                            }else{                                
                                errorRecover(current.lexeme,true);
                            }
                        }else{
                            errorRecover("(");
                        }
                    }else{                        
                        errorRecover(current.lexeme,true);
                    }
                }else{
                    errorRecover(current.lexeme,true);
                }
            }else{
                errorRecover("",false);
            }
        }else if(current.lexeme.equals("if")){
            consume();
            if(current.lexeme.equals("(")){
                consume();
                exp(current);
                if(current.lexeme.equals(")")){
                    consume();
                    if(current.lexeme.equals("then")){
                        consume();
                        hold = true;
                        stmt();
                        else_s(current);                        
                    }else{
                        errorRecover(current.lexeme, true);
                    }
                }else{
                    errorRecover(")");
                }
            }else{
                errorRecover("(");
            }
        }else if(current.lexeme.equals("print")){
            consume();
            if(current.lexeme.equals("(")){
                consume();
                isO_stmt(current);
            }else{
                errorRecover("(");
            }
        }else if(isDatatype(current)){
            consume();
            dec_stmt(current);
            if(!ended){
                if(current.lexeme.equals(";") ){
                    consume();
                    endStatement();
                }else{
                    System.out.println("hello");
                    errorRecover(";");
                }
            }else{
                end = false;
            }
            
        }else if(isIdentifier(current)){
            consume(); 
            if(current.lexeme.equals("=")){
                consume();
                value(current);
                if(current.lexeme.equals(";")){
                    consume();
                    endStatement();
                }else{
                    errorRecover(";");
                }
            }else{
                errorRecover("=");
            }
        }else {
            errorRecover("",false);
        }

        
    }

    private void stmt_p() {
        if(!current.lexeme.equals("}") && !s_tokens.empty()){
            stmt();
            stmt_p();
        }
    }

    private void else_s(Token token) {
        if(token.lexeme.equals("else")){
            consume();
            stmt();
            hold = false;
            endStatement();
        }else{
            endStatement();
        }
    }

    private void isO_stmt(Token token) {
        if(isVar(token) && next.lexeme.equals(")")){
            consume();
            consume();
            if(current.lexeme.equals(";")){
                consume();
                endStatement();
            }else{            
                errorRecover(";");
            }
        }else if(isExp(token)){

        }else{
            errorRecover("", false);
        }
    }

    private boolean isExp(Token token) {
        return false;
    }

    void dec_stmt(Token token){
        if(isIdentifier(token)){            
            consume();
            ass_stmt(current);
            x(current);
            
        }else{
            errorRecover(current.lexeme, false);
        }
    }

    private void x(Token token) {
        if(token.lexeme.equals(",")){
            if(next.lexeme.equals(";")){
                errorRecover(token.lexeme, false);
            }else{
                consume();
                dec_stmt(current);
            }
        }else if (!token.lexeme.equals(";")){
            ended = true;
            errorRecover(token.lexeme, true);
        }
    }

    void ass_stmt(Token token){
        if(token.lexeme.equals("=")){
            consume();
            value(current);
        }else if (!token.lexeme.equals(",") && !token.lexeme.equals(";")){
            ended = true;
            errorRecover("=");
        }
    }

    void value(Token token){
        if(token.lexeme.equals("scan")){
            consume();
            if(current.lexeme.equals("(")){
                consume();
                if(current.lexeme.equals(")")){
                    consume();
                }else{                
                    errorRecover(")");
                }                
            }else{                
                errorRecover("(");
            }
        }else{            
            exp(token);
        }
    }

    void exp(Token token){
        if(isConstant(token)){
            consume();
        }else if(isIdentifier(token)){
            consume();  
        }
    }

    void exp_p(Token token){

    }
    
    boolean isVar(Token token){
        
        if(isIdentifier(token) || isConstant(token)){
            return true;
        }
        return false;
    }
    
    //FUNCTIONS
    void errorRecover(String str){
        statement.isValid = false;                  
        statement.message = "Expected/Missing '" + str + "'" ;
        do{
            consume();
        }while(!current.lexeme.equals(";") && !current.lexeme.equals("}") && !s_tokens.empty());
        consume();
        endStatement();
    }

    void errorRecover(String str, Boolean x){
        statement.isValid = false;                  
        if(x){            
            statement.message = "Unknown Syntax '" + str + "'";
        }else{
            statement.message = "Invalid Syntax " ;
        }
        do{            
            System.out.println("Skip " + current.lexeme);
            consume();
        }while(( !current.lexeme.equals("}") && !current.lexeme.equals(";")) && !s_tokens.empty());
        consume();           
        endStatement();
    }

    void consume(){
        if(!end){
            statement.tokens.add(current);
            System.out.println("Consumed: " + current.lexeme);
            if(!s_tokens.empty()){            
                //System.out.println("NEXT = " + s_tokens.peek().lexeme);
                current = s_tokens.pop();
                if(!s_tokens.empty())
                    next = s_tokens.peek();
            }else{
                end = true;
            }
        }
        
    }

    void endStatement(){
        if(statement.tokens.size() != 0 && !hold){            
            statement.line = statement.tokens.get(0).line;
            statements.add(statement);
            statement = new Statement();
            System.out.println("ENDED");
        }
    }

    boolean isKeyword(Token token){
        if(token.token == TokenType.KEYWORD){
            return true;
        }
        return false;
    }
    boolean isDelimeter(Token token){
        if(token.token == TokenType.DELIMETER_BRACKET){
            return true;
        }
        return false;
    }
    boolean isOperator(Token token){
        if(token.token == TokenType.OPERATOR){
            return true;
        }
        return false;
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
        }else if(token.lexeme == "\"" || token.lexeme == "'" ){
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

}
