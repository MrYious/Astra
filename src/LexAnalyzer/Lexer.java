package LexAnalyzer;

public class Lexer {
        
    private String [] operator = { 
        "+", "-", "*", "/", "^", "%",       
        "==", ">", ">=", "<", "<=", "<>",  
        "!", "|", "&",                      
        "="                                 
    };
    
    private String [] delimeter = { 
        ";", "\"", "'", ",", "(", ")",       
        "{", "}"          
    };
    
    private String [] datatype = { 
        "int", "char", "bool", "float", "string",
    };
    
    private String [] keyword = { 
        "scan", "print", "if", "then", "else",
        "for", "in", "range", "do"
    };
    
    private String [] comment = { 
        "!*", "!**", "*!"
    };


    public enum State {
        START, Q1, Q2, Q3, Q4, Q5, Q6, Q7, INVALID
    }

}


    