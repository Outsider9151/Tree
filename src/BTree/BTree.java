package BTree;

import java.util.ArrayList;

public class BTree {
    BTreeNode root;
    private final int t;

    public BTree(int t){
        root = new BTreeNode();
        this.t = t;
    }

    //插入不更新重复的key
    public void BInsert(BTreeNode x, Word key){
        if (root.getKeys().size() == 0){
            root.getKeys().add(key);
            return;
        }
        //判断是否有重复的key
        for(int i = 0; i < x.getKeys().size(); i++){
            if(x.getKey(i).getWord().equals(key.getWord())){
                System.out.println("Key " + key.getWord() + " conflicts, we use the former data.");
                return;
            }
        }
        //判断结点是否过长需要分裂
        if(x.getKeys().size() >= (2 * t - 1))
            x = BTreeSplit(x);
        //找到key应该插入的位置
        int i = position(x, key.getWord());
        //若该结点不是叶结点，则一直递归到它是叶结点再插入
        if(x.notLeaf())
            BInsert(x.getChild(i), key);
        else
            x.getKeys().add(i, key);
    }

    //更新key，若无则插入key
    public void BInsertUpdate(BTreeNode x, Word key){
        if(root.getKeys().size() == 0) {
            root.getKeys().add(key);
            return;
        }
        //判断是否有需要更新的key，并直接更新返回
        for(int i = 0; i < x.getKeys().size(); i++){
            if(x.getKey(i).getWord().equals(key.getWord())){
                x.getKey(i).setValue(key.getValue());
                System.out.println("Key " + key.getWord() + " has been updated.");
                return;
            }
        }
        //与插入的代码相同，插入后有提示
        if(x.getKeys().size() >= (2 * t - 1))
            x = BTreeSplit(x);
        int i = position(x, key.getWord());
        if(x.notLeaf())
            BInsertUpdate(x.getChild(i), key);
        else {
            x.getKeys().add(i, key);
            System.out.println("Key " + key.getWord() + " missing, so we insert it.");
        }
    }

    //分裂过长的结点
    private BTreeNode BTreeSplit(BTreeNode x){
        //递归结束条件，长度小于最大值
        if(x.getKeys().size() < (2 * t - 1))
            return x;
        BTreeNode parent = x.getParent();
        //分别存储原结点的前半部分和后半部分的key，留出中间的key
        ArrayList<Word> newKeys1 = new ArrayList<>(x.getKeys().subList(0, t - 1));
        ArrayList<Word> newKeys2 = new ArrayList<>(x.getKeys().subList(t, 2 * t - 1));
        //以后半部分的key创建新的结点
        BTreeNode newNode = new BTreeNode(newKeys2);
        //如果该结点没有父亲，则创建一个父结点作为根节点，并将中间的key赋给它
        if(parent == null){
            parent = new BTreeNode();
            parent.getKeys().add(x.getKey(t - 1));
            x.setKeys(newKeys1);
            x.setParent(parent);
            parent.getChildren().add(x);
            newNode.setParent(parent);
            parent.getChildren().add(newNode);
            root = parent;
        }
        //若该结点有父亲，找到改结点在父亲孩子中的位置，并在对应位置插入中间的key值
        else {
            parent.getKeys().add(x.getPosition(), x.getKey(t - 1));
            newNode.setParent(parent);
            parent.getChildren().add(x.getPosition() + 1, newNode);
            x.setKeys(newKeys1);
        }
        //若原结点有孩子，将孩子分给分裂后的两个节点
        if(x.getChildren().size() > 0){
            ArrayList<BTreeNode> temp1 = new ArrayList<>(x.getChildren().subList(0, t));
            ArrayList<BTreeNode> temp2 = new ArrayList<>(x.getChildren().subList(t, 2 * t));
            x.setChildren(temp1);
            for (BTreeNode bTreeNode : temp2) bTreeNode.setParent(newNode);
            newNode.setChildren(temp2);
        }
        return BTreeSplit(parent);
    }

    //删除
    public void BDelete(BTreeNode x, String key){
        if (root == null)
            return;
        int index = -1;
        //判断x的key之中是否有待删除的key
        for(int i = 0; i < x.getKeys().size(); i++){
            if(x.getKey(i).getWord().equals(key)) {
                index = i;
                break;
            }
        }
        //如果没有，若被检查的结点有孩子，则递归到孩子继续找；若没有孩子了仍找不到，则报错
        if(index == -1){
            if (x.notLeaf()){
                //非根节点长度不超过t-1需要向兄弟借孩子
                if (x.getParent() != null && x.getKeys().size() <= t - 1)
                    x = Fixup(x);
                index = position(x, key);
                BDelete(x.getChild(index), key);
            }
            else
                System.out.println("Key " + key + " missing!");
        }
        //有就删除
        else
            delete(x, index);
    }

    /*
      向兄弟要key：
      将兄弟的key给父亲
      将父亲的key给需要增长的结点
      将兄弟被转移key的孩子转给新增的来自父亲的key
    */
    private BTreeNode Fixup(BTreeNode x){
        BTreeNode p = x.getParent();
        BTreeNode right = null;
        BTreeNode left = null;
        //结点是头，无左兄弟
        if (x.getPosition() == 0)
            right = p.getChild(1);
        //结点是尾，无右兄弟
        else if (x.getPosition() == p.getKeys().size())
            left = p.getChild(x.getPosition() - 1);
        else {
            left = p.getChild(x.getPosition() - 1);
            right = p.getChild(x.getPosition() + 1);
        }
        //向右兄弟要key
        if (right != null && right.getKeys().size() > t - 1) {
            Word temp = p.getKey(x.getPosition());
            p.setKey(x.getPosition(), right.getKey(0));
            right.getKeys().remove(0);
            x.getKeys().add(temp);
            if (right.getChildren().size() > 0) {
                BTreeNode t = right.getChild(0);
                t.setParent(x);
                x.getChildren().add(t);
                right.getChildren().remove(0);
            }
            return x;
        }
        //向左兄弟要key，注意下标
        else if (left != null && left.getKeys().size() > t - 1) {
            Word temp = p.getKey(x.getPosition() - 1);
            p.setKey(x.getPosition() - 1, left.getKey(left.getKeys().size() - 1));
            left.getKeys().remove(left.getKeys().size() - 1);
            x.getKeys().add(0, temp);
            if (left.getChildren().size() > 0) {
                BTreeNode t = left.getChild(left.getChildren().size() - 1);
                t.setParent(x);
                x.getChildren().add(0, t);
                left.getChildren().remove(left.getChildren().size() - 1);
            }
            return x;
        }
        //左右兄弟都不够，就合并
        else {
            if (left != null) {
                combine(left, x);
                return left;
            }
            else if (right != null) {
                combine(x, right);
                return x;
            }
        }
        return x;
    }

    //合并
    private void combine(BTreeNode x, BTreeNode y){
        BTreeNode p = x.getParent();
        //需要合并的是根节点仅剩的俩孩子
        if(p.getChildren().size() == 2){
            x.getKeys().addAll(p.getKeys());
            x.getKeys().addAll(y.getKeys());
            p.getChildren().remove(1);
            x.setParent(null);
            root = x;
        }
        //一般情况
        else {
            x.getKeys().add(p.getKey(x.getPosition()));
            x.getKeys().addAll(y.getKeys());
            p.getKeys().remove(x.getPosition());
            p.getChildren().remove(y.getPosition());
        }
        //调整下被合并掉的结点的父亲
        for (int i = 0; i < y.getChildren().size(); i++)
            y.getChild(i).setParent(x);
        x.getChildren().addAll(y.getChildren());
        //调整
        if (x.getParent() != null && x.getKeys().size() <= t - 1)
            Fixup(p);
    }

    //删除并调整父亲结点
    private void delete(BTreeNode x, int index){
        //有孩子找继承者替代删除
        if(x.notLeaf()){
            BTreeNode left = getLeft(x.getChild(index + 1));
            x.setKey(index, left.getKey(0));
            left.getKeys().remove(0);
        }
        //叶结点直接删除，并调整
        else {
            x.getKeys().remove(index);
            if (x.getParent() != null && x.getKeys().size() <= t - 1)
                Fixup(x);
        }
    }

    //最左边的孩子，以用来替换父结点
    private BTreeNode getLeft(BTreeNode x){
        if(x.getChildren().size() > 0)
            return getLeft(x.getChild(0));
        else
            return x;
    }

    //找到key在该结点对应位置，用的是二分法
    private int position(BTreeNode x, String key){
        int i = 0;
        if(x.getKeys().size() > 0){
            int left = 0;
            int right = x.getKeys().size() - 1;
            int step;
            if(left != right){
                while((right - left) != 1){
                    step = (right - left)/2;
                    if(x.getKey(left + step).getWord().compareTo(key) > 0)
                        right -= step;
                    else if (x.getKey(i + step).getWord().compareTo(key) < 0)
                        left += step;
                    else
                        return left + step;
                }
            }
            if(key.compareTo(x.getKey(right).getWord()) >= 0)
                i = right + 1;
            else if (key.compareTo(x.getKey(left).getWord()) <= 0)
                i = left;
            else
                i = right;
        }
        return i;
    }

    //查找，与删除的定位类似
    public void BTreesearch(BTreeNode x, String key){
        int index = -1;
        for(int i = 0; i < x.getKeys().size(); i++){
            if (key.equals(x.getKey(i).getWord())) {
                index = i;
                break;
            }
        }
        if (index == -1){
            if (x.notLeaf()){
                if ((x.getParent() != null && x.getKeys().size() <= t - 1))
                    x = Fixup(x);
                index = position(x, key);
                BTreesearch(x.getChild(index), key);
            }
            else
                System.out.println("Key " + key + " missing!");
        }
        else
            System.out.println(x.getKey(index).getPair());
    }

    //遍历
    public void dump(BTreeNode x){
        if (x == null)
            return;
        int i;
        for(i = 0; i < x.getKeys().size(); i++){
            if (x.notLeaf())
                dump(x.getChild(i));
            System.out.println(x.getKey(i).getPair());
        }
        if (x.notLeaf())
            dump(x.getChild(i));
    }
}
