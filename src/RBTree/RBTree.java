package RBTree;

public class RBTree {
    public RBTreeNode root;
    public final RBTreeNode NIL = new RBTreeNode(null, null, null, RBTreeNode.black, null, null);

    public RBTree(){
        root = NIL;
    }

    //插入
    public void RBInsert(RBTreeNode z) {
        RBTreeNode x = root;
        RBTreeNode y = NIL;
        //定位到z该插入的叶结点位置，并判断是否有相同的key
        while(x != NIL){
            y = x;
            if (z.getKey().equals(x.getKey())){
                System.out.println("Key " + z.getKey() + " conflicts, we use the former data.");
                return;
            }
            if (z.getKey().compareTo(x.getKey()) < 0)
                x = x.getLeft();
            else
                x = x.getRight();
        }
        z.setParent(y);
        //树为空时，设置根结点
        if(y == NIL)
            root = z;
        //比较key大小来判断应该是左子还是右子
        else if (z.getKey().compareTo(y.getKey()) < 0)
            y.setLeft(z);
        else
            y.setRight(z);
        //补上新的虚拟结点
        z.setLeft(NIL);
        z.setRight(NIL);
        //新增结点一律为红色
        z.setColour(RBTreeNode.red);
        //调整
        RBInsertFixup(z);
    }

    //更新key，若没有则插入
    public void RBInsertUpdate(RBTreeNode z) {
        RBTreeNode x = root;
        RBTreeNode y = NIL;
        //判断是否有相同的key，若有则更新并返回，没有则插入
        while(x != NIL){
            y = x;
            if (z.getKey().equals(x.getKey())){
                System.out.println("Key " + z.getKey() + " has been updated.");
                x.setValue(z.getValue());
                return;
            }
            //后于插入部分相同，插入后有提示
            if (z.getKey().compareTo(x.getKey()) < 0)
                x = x.getLeft();
            else
                x = x.getRight();
        }
        z.setParent(y);
        if(y == NIL)
            root = z;
        else if (z.getKey().compareTo(y.getKey()) < 0)
            y.setLeft(z);
        else
            y.setRight(z);
        z.setLeft(NIL);
        z.setRight(NIL);
        z.setColour(RBTreeNode.red);
        RBInsertFixup(z);
        System.out.println("Key " + z.getKey() + " missing, so we insert it.");
    }

    //插入调整
    private void RBInsertFixup(RBTreeNode z) {
        //一直调整到父结点是黑色为止
        while(z.getParent().getColour() == RBTreeNode.red){
            //z的父亲是左子
            if(z.getParent().isLeft()){
                RBTreeNode y = z.getUncle();
                //第一种情况，叔叔也是红色，则换颜色
                if(y.getColour() == RBTreeNode.red){
                    z.getParent().setColour(RBTreeNode.black);
                    y.setColour(RBTreeNode.black);
                    z.getGrandpa().setColour(RBTreeNode.red);
                    z = z.getGrandpa();
                }
                //第二种情况，叔叔是黑色，z是右子，则左旋
                else if (!z.isLeft()){
                    z = z.getParent();
                    leftRotation(z);
                }
                //第三种情况，叔叔是黑色，z是左子，则右旋，并换色
                else {
                    z.getParent().setColour(RBTreeNode.black);
                    z.getGrandpa().setColour(RBTreeNode.red);
                    rightRotation(z.getGrandpa());
                }
            }
            //z的父亲是右子，与左子情况左右对换
            else {
                RBTreeNode y = z.getUncle();
                if(y.getColour() == RBTreeNode.red){
                    z.getParent().setColour(RBTreeNode.black);
                    y.setColour(RBTreeNode.black);
                    z.getGrandpa().setColour(RBTreeNode.red);
                    z = z.getGrandpa();
                }
                else if (z.isLeft()){
                    z = z.getParent();
                    rightRotation(z);
                }
                else {
                    z.getParent().setColour(RBTreeNode.black);
                    z.getGrandpa().setColour(RBTreeNode.red);
                    leftRotation(z.getGrandpa());
                }
            }
        }
        //根结点设为黑色
        root.setColour(RBTreeNode.black);
    }

    //左旋
    private void leftRotation(RBTreeNode x) {
        RBTreeNode y = x.getRight();
        //把y的左子树设为x的右子树
        x.setRight(y.getLeft());
        if (y.getLeft() != NIL)
            y.getLeft().setParent(x);
        //y替代x
        y.setParent(x.getParent());
        if (x.getParent() == NIL)
            root = y;
        else if (x.isLeft())
            x.getParent().setLeft(y);
        else
            x.getParent().setRight(y);
        //x设为y的左孩
        y.setLeft(x);
        x.setParent(y);
    }

    //右旋
    private void rightRotation(RBTreeNode x) {
        RBTreeNode y = x.getLeft();
        //把y的右子树设为x的左子树
        x.setLeft(y.getRight());
        if (y.getRight() != NIL)
            y.getRight().setParent(x);
        //y替代x
        y.setParent(x.getParent());
        if (x.getParent() == NIL)
            root = y;
        else if (!x.isLeft())
            x.getParent().setRight(y);
        else
            x.getParent().setLeft(y);
        //x设为y的右孩
        y.setRight(x);
        x.setParent(y);
    }

    //查找
    public RBTreeNode search(String key) {
        RBTreeNode x = root;
        //根据key值大小比较查找，找到或者到虚拟结点结束
        while (x != NIL && !key.equals(x.getKey())){
            if (key.compareTo(x.getKey()) < 0)
                x = x.getLeft();
            else
                x = x.getRight();
        }
        return x;
    }

    //替换删除
    private void RBTransplant(RBTreeNode u, RBTreeNode v) {
        if (u.getParent() == NIL)
            root = u;
        else if (u.isLeft())
            u.getParent().setLeft(v);
        else
            u.getParent().setRight(v);
        v.setParent(u.getParent());
    }

    private RBTreeNode TreeMinimum(RBTreeNode x) {
        while (x.getLeft() != NIL)
            x = x.getLeft();
        return x;
    }

    //删除前的准备
    public void delete(String key) {
        //先通过查找定位到需要删除的结点
        RBTreeNode z = search(key);
        if (z == NIL)
            System.out.println("Key " + key + " missing!");
        else
            RBDelete(z);
    }

    //删除结点
    private void RBDelete(RBTreeNode z) {
        RBTreeNode y = z;
        int yOriginalColor = y.getColour();
        RBTreeNode x;
        //z是叶结点
        if (z.getLeft() == NIL) {
            x = z.getRight();
            RBTransplant(z, z.getRight());
        }
        else if (z.getRight() == NIL) {
            x = z.getLeft();
            RBTransplant(z, z.getLeft());
        }
        //z不是叶结点
        else {
            //继承者
            y = TreeMinimum(z.getRight());
            yOriginalColor = y.getColour();
            x = y.getRight();
            //继承者y就是z的孩子，则y的继承者替代y的位置
            if (y.getParent() == z)
                x.setParent(y);
            //继承者y准备替代z的位置，y的继承者替代y的位置
            else {
                RBTransplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            //继承者y替代z，z被删除
            RBTransplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColour(z.getColour());
        }
        //改动黑色结点需要调整
        if (yOriginalColor == RBTreeNode.black)
            RBDeleteFixup(x);
    }

    //删除调整
    private void RBDeleteFixup(RBTreeNode x) {
        //调整非根黑色结点
        while(x != root && x.getColour() == RBTreeNode.black){
            //x为左子
            if(x.isLeft()){
                RBTreeNode w = x.getBrother();
                //第一种情况，兄弟是红色的，则换颜色并左旋，得到新的黑色兄弟
                if(w.getColour() == RBTreeNode.red){
                    w.setColour(RBTreeNode.black);
                    x.getParent().setColour(RBTreeNode.red);
                    leftRotation(x.getParent());
                    w = x.getBrother();
                }
                //第二种情况，兄弟是黑色，且兄弟的两个孩子也是黑色，换颜色并向父结点迭代
                if (w.getLeft().getColour() == RBTreeNode.black && w.getRight().getColour() == RBTreeNode.black){
                    w.setColour(RBTreeNode.red);
                    x = x.getParent();
                }
                //第三种情况，兄弟和远侄子是黑色，近侄子是红色,则换颜色并右旋，得到新的黑兄弟
                else if (w.getRight().getColour() == RBTreeNode.black){
                    w.getLeft().setColour(RBTreeNode.black);
                    w.setColour(RBTreeNode.red);
                    rightRotation(w);
                    w = x.getBrother();
                }
                //第四种情况，兄弟是黑的，兄弟的两个孩子都是红的,则换颜色左旋，结束循环
                w.setColour(x.getParent().getColour());
                x.getParent().setColour(RBTreeNode.black);
                w.getRight().setColour(RBTreeNode.black);
                leftRotation(x.getParent());
                x = root;
            }
            //x是右子，则左右对换
            else {
                RBTreeNode w = x.getBrother();
                if(w.getColour() == RBTreeNode.red){
                    w.setColour(RBTreeNode.black);
                    x.getParent().setColour(RBTreeNode.red);
                    rightRotation(x.getParent());
                    w = x.getParent().getLeft();
                }
                if (w.getRight().getColour() == RBTreeNode.black && w.getLeft().getColour() == RBTreeNode.black){
                    w.setColour(RBTreeNode.red);
                    x = x.getParent();
                }
                else if (w.getLeft().getColour() == RBTreeNode.black){
                    w.getRight().setColour(RBTreeNode.black);
                    w.setColour(RBTreeNode.red);
                    leftRotation(w);
                    w = x.getParent().getLeft();
                }
                w.setColour(x.getParent().getColour());
                x.getParent().setColour(RBTreeNode.black);
                w.getLeft().setColour(RBTreeNode.black);
                rightRotation(x.getParent());
                x = root;
            }
        }
        //保证根结点是黑色
        x.setColour(RBTreeNode.black);
    }

    //中序遍历
    public void dump(RBTreeNode x){
        if(x != NIL){
            dump(x.getLeft());
            System.out.println(x.getPair());
            dump(x.getRight());
        }
    }
}
