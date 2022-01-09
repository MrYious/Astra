package LexAnalyzer;

/*  TO-DO list
    
    [/] Validate the Input File based on extension
    [/] Text File -> Array of String per Line 
    [] File Write for the "symbol_table" Output File

*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    
    public static void main(String[] args) throws FileNotFoundException{

        // String file_path = "E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\input.ast";
        // E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\input.ast


        String file_path;
        File file;


        Scanner scan = new Scanner(System.in);
        do{
            System.out.println("\nEnter the path of the source file: ");            
            file_path = scan.nextLine();
            file = new File(file_path);
            
        }while( !isExist(file) || !isValid(file_path) );              
        scan.close();
               
        ArrayList<String> lines = prepareFile(file);
        
        // int i = 0;
        // for (String line : lines) {
        //     System.out.println(++i + " " + line);
        // }
        
        //To-Do
        //  Lexer lexer = new Lexer(lines);                     
        //  ArrayList<Token> tokens = lexer.execute();      //Array of Tokens with complete information

    }

    
    private static ArrayList<String> prepareFile(File file) throws FileNotFoundException{
        ArrayList<String> lines = new ArrayList<>();
        System.out.println();
                
        Scanner fileScan = new Scanner(file);
        String line;
        while (fileScan.hasNextLine()) {                
            line = fileScan.nextLine();
            lines.add(line);
        }
        fileScan.close();
        return lines;
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
