package RBTree;

import java.io.*;
import java.util.Scanner;
import java.util.StringTokenizer;

public class RBTreeMain {
    static RBTree rbt = new RBTree();

    private static void init(){
        File file = new File("src/Data/init.txt");
        insertFile(file);
    }

    private static void menu(){
        System.out.println("Your English word information frequency list has been initialized successfully!");
        System.out.println("You can use following command to operate on your list:");
        System.out.println("0 exit\n1 insert by file\n2 insert by command\n3 delete by file");
        System.out.println("4 delete by command\n5 update\n6 search\n7 dump");
    }

    private static void insertFile(File file){
        try {
            String s;
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            while ((s = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(s, " ");
                if(st.hasMoreTokens()){
                    String word = st.nextToken().trim();
                    String part = st.nextToken().trim();
                    float frequency = Float.parseFloat(st.nextToken().trim());

                    String value = "-(" + part + ", " + frequency + ")";
                    RBTreeNode x = new RBTreeNode(word, value);
                    rbt.RBInsert(x);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void insert(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the word you want to insert in the pattern of 'word part frequency':");
        String s = input.nextLine();
        StringTokenizer st = new StringTokenizer(s, " ");
        String word = st.nextToken().trim();
        String part = st.nextToken().trim();
        float frequency = Float.parseFloat(st.nextToken().trim());

        String value = "-(" + part + ", " + frequency + ")";
        RBTreeNode x = new RBTreeNode(word, value);
        rbt.RBInsert(x);
    }

    private static void deleteFile(File file){
        try{
            String s;
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            while((s = br.readLine()) != null){
                StringTokenizer st = new StringTokenizer(s, " ");
                if(st.hasMoreTokens()){
                    String word = st.nextToken().trim();
                    rbt.delete(word);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void delete(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the word you want to delete:");
        String word = input.nextLine();
        rbt.delete(word);
    }

    private static void update(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the word you want to update in the pattern of 'word part frequency':");
        String s = input.nextLine();
        StringTokenizer st = new StringTokenizer(s, " ");
        String word = st.nextToken().trim();
        String part = st.nextToken().trim();
        float frequency = Float.parseFloat(st.nextToken().trim());

        String value = "-(" + part + ", " + frequency + ")";
        RBTreeNode x = new RBTreeNode(word, value);
        rbt.RBInsertUpdate(x);
    }

    private static void search(){
        Scanner input = new Scanner(System.in);
        System.out.println("Please input the word you want to search:");
        String word = input.nextLine();
        RBTreeNode t = rbt.search(word);
        if (t == rbt.NIL)
            System.out.println("Key " + word + " missing!");
        else
            System.out.println(t.getPair());
    }

    private static void dump(){
        rbt.dump(rbt.root);
    }

    private static void command(){
        Scanner input = new Scanner(System.in);
        while(true){
            System.out.print("Your command:");
            String command = input.nextLine();
            switch(command){
                case "0":
                    return;
                case "1":
                    File file = new File("src/Data/insert.txt");
                    insertFile(file);
                    System.out.println("Your file has been inserted successfully!");
                    break;
                case "2":
                    insert();
                    break;
                case "3":
                    file = new File("src/Data/delete.txt");
                    deleteFile(file);
                    break;
                case "4":
                    delete();
                    break;
                case "5":
                    update();
                    break;
                case "6":
                    search();
                    break;
                case "7":
                    dump();
                    break;
                default:
                    System.out.println("Your command is invalid, please input again!");
            }
        }
    }

    public static void main(String[] args){
        init();
        menu();
        command();
    }
}
