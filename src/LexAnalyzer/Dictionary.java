package LexAnalyzer;

import java.util.HashMap;

public class Dictionary {

    // String set1 = "+-*/^%|&;,(){}";    //14
    String validSymbols = "+-*/^%>=<!|&;,(){}\"'!";

    HashMap<String, String> operator = new HashMap<>();
    HashMap<String, String> delimeter = new HashMap<>();
    HashMap<String, String> comment = new HashMap<>();
    HashMap<String, String> datatype = new HashMap<>();
    HashMap<String, String> keyword = new HashMap<>();
    
    
    Dictionary(){
        operator.put("+", "Arithmetic Operator");
        operator.put("-", "Arithmetic Operator");
        operator.put("/", "Arithmetic Operator");
        operator.put("*", "Arithmetic Operator");
        operator.put("^", "Arithmetic Operator");
        operator.put("%", "Arithmetic Operator");
        operator.put("==", "Relational Operator");
        operator.put("<",  "Relational Operator");
        operator.put(">",  "Relational Operator");
        operator.put("<=", "Relational Operator");
        operator.put(">=", "Relational Operator");
        operator.put("<>", "Relational Operator");
        operator.put("|",  "Logical Operator");
        operator.put("&",  "Logical Operator");
        operator.put("!",  "Logical Operator");
        operator.put("=",  "Assignment Operator");

        delimeter.put(";",     "Terminator");
        delimeter.put("\"",    "Double Quotation");
        delimeter.put("'",     "Single Quotation");
        delimeter.put(",",     "Separator");
        delimeter.put("(",     "Open Parenthesis");
        delimeter.put(")",     "Close Parenthesis");
        delimeter.put("{",     "Group Statement - Open Brace");
        delimeter.put("}",     "Group Statement - Close Brace");

        comment.put("!*",      "Single-Line Comment");
        comment.put("!**",     "Multi-Line Comment - Open");
        comment.put("*!",      "Multi-Line Comment - Close");
        comment.put("...",     "Comment Message");

        datatype.put("int",    "Integer Data Type");
        datatype.put("char",   "Character Data Type");
        datatype.put("bool",   "Boolean Data Type");
        datatype.put("float",  "Float Data Type");
        datatype.put("String", "String Data Type");

        keyword.put("scan",    "Input Statement");
        keyword.put("print",   "Output Statement");
        keyword.put("if",      "Conditional Statement");
        keyword.put("then",    "Conditional Statement");
        keyword.put("else",    "Conditional Statement");
        keyword.put("for",     "Looping/Repetition Statement");
        keyword.put("in",      "Looping/Repetition Statement");
        keyword.put("range",   "Looping/Repetition Statement");
        keyword.put("do",      "Looping/Repetition Statement");


    }
    
}
