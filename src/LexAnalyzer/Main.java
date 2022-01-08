package LexAnalyzer;

/*  TO-DO list
    
    [] Validate the Input File based on extension
    [] Text File -> Array of String per Line 
    [] File Write for the "symbol_table" Output File

*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args){

        //File Input
        try {            
            File file = new File("E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\code.ast");

            Scanner scan = new Scanner(file);
            scan.useDelimiter("");

            while (scan.hasNext()) {                
                String data = scan.next();
                System.out.print(data);
            }
            scan.close();
            

        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot Locate File!");
            e.printStackTrace();
        }
        
        
    }
}
