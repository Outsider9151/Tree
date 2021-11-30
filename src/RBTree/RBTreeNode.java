package RBTree;
public class RBTreeNode {
    public final static int black = 0;
    public final static int red = 1;

    private RBTreeNode left;
    private RBTreeNode right;
    private RBTreeNode parent;

    private int colour;
    private final String key;
    private String value;

    public RBTreeNode(String key, String value){
        this(null, null, null, black, key, value);
    }

    public RBTreeNode(RBTreeNode left, RBTreeNode right, RBTreeNode parent,
                      int colour, String key, String value){
        this.key = key;
        this.value = value;
        this.colour = colour;
        this.parent = parent;
        this.left = left;
        this.right = right;
    }

    public RBTreeNode getLeft(){
        return left;
    }

    public RBTreeNode getRight(){
        return right;
    }

    public RBTreeNode getParent(){
        return parent;
    }

    public int getColour(){
        return colour;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public void setLeft(RBTreeNode left){
        this.left = left;
    }

    public void setRight(RBTreeNode right){
        this.right = right;
    }

    public void setParent(RBTreeNode parent){
        this.parent = parent;
    }

    public void setColour(int colour){
        this.colour = colour;
    }

    public void setValue(String value){
        this.value = value;
    }

    public RBTreeNode getUncle(){
        if (parent.parent.left == parent)
            return parent.parent.right;
        else
            return parent.parent.left;
    }

    public RBTreeNode getBrother(){
        if (parent.left == this)
            return parent.right;
        else
            return parent.left;
    }

    public RBTreeNode getGrandpa(){
        return parent.parent;
    }

    public boolean isLeft(){
        if(parent == null)
            return false;
        return parent.left == this;
    }

    public String getPair(){
        return key + " " + value;
    }
}
