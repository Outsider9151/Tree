package BTree;

import java.util.ArrayList;

public class BTreeNode {
    private boolean leaf;
    private ArrayList<Word> keys = new ArrayList<>();
    private ArrayList<BTreeNode> children = new ArrayList<>();
    private BTreeNode parent = null;

    public BTreeNode(){
        leaf = true;
    }

    public BTreeNode(ArrayList<Word> keys){
        this.keys = keys;
    }

    public void setKey(int i, Word word){
        keys.set(i, word);
    }

    public Word getKey(int i){
        return keys.get(i);
    }

    public ArrayList<Word> getKeys(){
        return keys;
    }

    public void setKeys(ArrayList<Word> keys){
        this.keys = keys;
    }

    public BTreeNode getChild(int i){
        return children.get(i);
    }

    public ArrayList<BTreeNode> getChildren(){
        return children;
    }

    public void setChildren(ArrayList<BTreeNode> children){
        this.children = children;
    }

    public BTreeNode getParent(){
        return parent;
    }

    public void setParent(BTreeNode x){
        parent = x;
    }

    public boolean notLeaf(){
        leaf = children.size() == 0;
        return !leaf;
    }

    //返回它是父亲的第几个孩子
    public int getPosition(){
        int i = -1;
        if(parent == null)
            i = 0;
        else {
            for (int j = 0; j < parent.getChildren().size(); j++){
                if(parent.getChild(j) == this)
                    i = j;
            }
        }
        return i;
    }
}
