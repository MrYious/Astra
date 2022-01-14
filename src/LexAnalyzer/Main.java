package LexAnalyzer;

/** 
    SUBJECT: Principles of Programming Languages;
    COURSE AND SECTION: BSCS 3-3
    
    Programming Language:   AstraLang 

    [/] Lexical Analyzer
    [] Syntax Analyzer
    [] Semantic Analyzer
    
    Five (5) Members
    Leader: 
        Rosario, Mark Edison
    Member(s):
        Constantino, Bismillah
        Cube, Jeremy
        Jizmundo, Piolo Brian
        Tacata, Jericho Vince

    @version 1.0
*/

import java.io.File;
import java.io.FileWriter;  
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) throws FileNotFoundException{

        // E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\input.ast

        File file;

        // header();
        file = getSourceFile();                         //Locate the source file
        String lines = scanFile(file);                  //Convert source file into a string

        Lexer lexer = new Lexer(lines);                 //Lexical Analyzer object
        ArrayList<Token> tokens = lexer.execute();      //Convert into Tokens               
        writeSymbolTable(tokens);                       //Write tokens in the symbol table file
    }
  
    private static void header(){
        System.out.println();
        System.out.println();
        System.out.println("************************************************************");
        System.out.println("*                                                          *");
        System.out.println("*         ██████   ██████ ██████ ██████   ██████           *");
        System.out.println("*        ██    ██  ██       ██   ██  ██  ██    ██          *");
        System.out.println("*        ████████  ██████   ██   ██████  ████████          *");
        System.out.println("*        ██    ██      ██   ██   ██ ██   ██    ██          *");
        System.out.println("*        ██    ██  ██████   ██   ██  ██  ██    ██          *");
        System.out.println("*                                                          *");
        System.out.println("*                                                          *");
        System.out.println("*  This is a compiler for the Astra programming language   *");
        System.out.println("*                                                          *");
        System.out.println("*                  Phase 1: Lexical Analyzer               *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println();
        System.out.println();

    }
    
    private static File getSourceFile(){
        File file;
        String file_path;
        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("Enter the path of the source file: ");            
            file_path = scan.nextLine();
            file = new File(file_path);
            
        }while( !isExist(file) || !isValid(file_path) );              
        scan.close();
        return file;
    }

    private static String scanFile(File file) throws FileNotFoundException{
        String lines = "";
        System.out.println();
                
        Scanner fileScan = new Scanner(file);
        String line;
        
        while (fileScan.hasNextLine()) {                
            line = fileScan.nextLine();
            lines += line + "\n";
        }
        fileScan.close();
        return lines;
    }

    private static void writeSymbolTable(ArrayList<Token> tokens){
        try{    
            int i = 0;            
            FileWriter fw = new FileWriter("E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\symbol_table.astl");    
            
            fw.write("************\t\t\t************\t\t\t\t*****************\n");
            fw.write("*  TOKENS  *\t\t\t*  LEXEME  *\t\t\t\t*  DESCRIPTION  *\n");
            fw.write("************\t\t\t************\t\t\t\t*****************\n\n");
            for (Token token : tokens) {
                fw.write(++i + ":  " + token.getInformation() + "\n");
            }

            fw.close();    
        }catch(Exception e){
            System.out.println(e);
        }

        System.out.println("\nSymbol Table has been successfully generated!\n"); 
    }

    private static boolean isExist(File file){
        if(!file.exists()){
            System.out.println("\nERROR: Cannot Locate File!\nPlease try again.\n");
            return false;
        }
        return true;
    }

    private static boolean isValid(String file_path){
        if(!file_path.endsWith(".ast")){                
            System.out.println("\nERROR: Cannot recognize file format.\n");
            return false;
        }
        return true;
    }

    
}