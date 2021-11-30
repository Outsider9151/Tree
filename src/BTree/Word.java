package BTree;

public class Word {
    private String word;
    private String value;

    public Word(){}

    public Word(String word, String value){
        this.word = word;
        this.value = value;
    }

    public String getWord(){
        return word;
    }

    public void setValue(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public String getPair(){
        return word + " " + value;
    }
}
