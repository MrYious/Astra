package LexAnalyzer;

import java.io.File;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Dictionary {

    //PATH E:\BSCS 3-3\PPL\PROJECT\AstraLang\AstraLang\resources\dictionary.astd

    String validSymbols = "+-*/^%>=<!|&;,(){}\"'!";

    HashMap<String, String> operator = new HashMap<>();
    HashMap<String, String> delimeter = new HashMap<>();
    HashMap<String, String> comment = new HashMap<>();
    HashMap<String, String> datatype = new HashMap<>();
    HashMap<String, String> keyword = new HashMap<>();
    HashMap<String, String> bool = new HashMap<>();
    Scanner scan;

    Dictionary() throws FileNotFoundException{
        File file;      
        String file_path;        
        Scanner scan = new Scanner(System.in);
        do{            
            System.out.println("Enter the path of the dictionary file: ");
            
            file_path = scan.nextLine();
            file = new File(file_path);            
        }while( !isExist(file) || !isValid(file_path) );              
        scan.close();
        //file = new File("E:\\BSCS 3-3\\PPL\\PROJECT\\AstraLang\\AstraLang\\resources\\dictionary.astd");
        String data = scanFile(file);

        reader(data);
    }

    private void reader(String data){
        String lines[] = data.split("\\r?\\n");
        String token[];
        for(int i = 0; i < lines.length; i++){
            if(i < 16){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    operator.put(token[0], token[1]);
                }
            }else if(i < 25){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    delimeter.put(token[0], token[1]);
                }
            }else if(i < 29){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    comment.put(token[0], token[1]);
                }
            }else if(i < 35){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    datatype.put(token[0], token[1]);
                }
            }else if(i < 45){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    keyword.put(token[0], token[1]);
                }
            }else if(i < 48){
                if(lines[i].charAt(0) != ':'){
                    token = lines[i].split(":");
                    bool.put(token[0], token[1]);
                }
            }
        }
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

    private static boolean isExist(File file){
        if(!file.exists()){
            System.out.println("\nERROR: Cannot Locate File!\nPlease try again.\n");
            return false;
        }
        return true;
    }

    private static boolean isValid(String file_path){
        if(!file_path.endsWith(".astd")){                
            System.out.println("\nERROR: Cannot recognize file format.\nMust end with .astd\n");
            return false;
        }
        return true;
    }

}   
