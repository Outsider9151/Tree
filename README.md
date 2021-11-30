# Tree
A project of Data Structure
红黑树和B树实现英语单词频率查找表
# Project 1 说明文档
 语言：Java
 环境：IntelliJ IDEA

---
## 1. 红黑树

- 红黑树是一种先操作后调整的平衡二叉树
- 主要实现了插入、更新、删除、查找和遍历
- 基本是参考的书上伪代码

### 1.1 红黑树的结点
#### 变量：
```java
public final static int black = 0;
public final static int red = 1;

private RBTreeNode left;
private RBTreeNode right;
private RBTreeNode parent;

private int colour;
private String key;
private String value;
```
#### 函数：
主要是一些获取、设置结点属性的函数，以及一些简单的判断
```java
public RBTreeNode(String key, String value)
public RBTreeNode(RBTreeNode left, RBTreeNode right, RBTreeNode parent, int colour, String key, String value)
public RBTreeNode getLeft()
public RBTreeNode getRight()
public RBTreeNode getParent()
public int getColour()
public String getKey()
public String getValue()

public void setLeft(RBTreeNode left)
public void setRight(RBTreeNode right)
public void setParent(RBTreeNode parent)
public void setColour(int colour)
public void setValue(String value)

public RBTreeNode getUncle()
public RBTreeNode getBrother()
public RBTreeNode getGrandpa()
public boolean isLeft()
public String getPair()
```
### 1.2 红黑树结构
#### 变量：
```java
//根结点
public RBTreeNode root;
//虚拟结点
public final RBTreeNode NIL;
```
#### 函数：
##### 插入
key始终先被插入到叶结点再进行调整
先循环比较大小找到key应该被插入的叶结点，若遇到相同的key则报错并停止循环返回，不进行插入操作；成功插入到叶结点后给key补上左右虚拟结点作为孩子，再进行调整
插入调整在父结点是红色时进行调整，是黑色时停止。分为三种情况，
第一种叔叔也是红色，则根据红黑树要求换颜色；
第二种叔叔是黑色且待调整结点与父亲不同侧，则以父结点为轴向父亲所在侧旋转；
第三种叔叔是黑色且待调整结点与父亲同侧，则先换颜色，再以父结点为轴向父亲所在侧的反向旋转
每种情况调整好后都需要检查父节点是否为红色，需要继续调整，最后设根结点为黑色

##### 更新
与插入大致相同，只是在循环比较大小时若遇到相同的key，则直接重设改结点的value完成更新并返回；其余操作与插入一致

##### 查找
先循环比较key值的大小来查找key所在结点，再返回其value。我在结点中设了返回pair的函数，可以直接打印
在循环中如果找到或者遇到虚拟结点则结束循环并返回

##### 删除
先通过查找定位到需要删除的结点，有则删除，无则报错。删除后若结点为黑色，则需要调整。
调整时若遇到根结点，或待调整结点为黑色，则停止
分为四种情况：
第一种情况，兄弟是红色的，则换颜色并以父结点为轴向待调整结点所在侧旋转，得到新的黑色兄弟；
第二种情况，兄弟是黑色，且兄弟的两个孩子也是黑色，换颜色，待调整结点改为父结点；
第三种情况，兄弟和远侄子是黑色，近侄子是红色,则换颜色并以兄弟节点为轴向兄弟结点所在侧旋转，得到新的黑兄弟；
第四种情况，兄弟是黑的，兄弟的两个孩子都是红的,则换颜色并以父结点为轴向待调整结点所在侧旋转，结束循环
最后将根节点设为黑色

##### 遍历
中序遍历，通过递归实现

#### 实现
开始运行后就直接初始化，并报初始化中重复的key值，之后会有菜单界面
```java
Your English word information frequency list has been initialized successfully!
You can use following command to operate on your list:
0 exit
1 insert by file
2 insert by command
3 delete by file
4 delete by command
5 update
6 search
7 dump
Your command:
```
按0会退出程序，1会插入指定文件，3会删除指定文件，7会显示目前list中所有单词
2会有提示行，需要键入指定格式的单词和频率，如 world J 10.0
```java
Please input the word you want to insert in the pattern of 'word part frequency':
```
4也会有提示行，只需键入想要删除的单词，不需要频率
```java
Please input the word you want to delete:
```
5也会有提示行，需要键入指定格式的想要更新的单词，如 world J 10.0
```java
Please input the word you want to update in the pattern of 'word part frequency':
```
6也会有提示行，只需要键入想要查找的单词，不需要频率
```java
Please input the word you want to search:
```

## 2. B树

- 在删除时先修改后删除以防止大量修改的树
- 用了Java中的ArrayList来实现，避免了插入或删除时对数组的大规模修改
- 因与红黑树不同，B树的每个结点不止存放一个key，为避免返回时需要key和value查找2次，我用了Word类用来存放key和value，并作为存入B树中的对象
- 基本上是对着老师的PPT上的动画一点点写的，书上的伪代码我尝试过，但一是因删除时没有伪代码，我很难记住什么时候应该更新属性，二是因为插入时书上的那堆i和j看的我头疼，所以自己尝试参考着书和PPT写了
- 因为用了ArrayList，结点中存放的key数量是实时更新的，不再需要我去更新，因而我也忘记了leaf属性也需要更新。在写完后发现了这个问题，我的leaf判断并没有及时更新，只在需要知道结点leaf属性时才会进行判断，其他时候leaf始终是true，但似乎也不会影响结果，所以没改。

### 2.1 B树的结点
#### 变量
```java
private boolean leaf;
private ArrayList<Word> keys;
private ArrayList<BTreeNode> children;
private BTreeNode parent;
```
#### 函数
主要是获取和设置结点属性的函数
```java
public BTreeNode(ArrayList<Word> keys)public void setKey(int i, Word word)
public Word getKey(int i)
public ArrayList<Word> getKeys()
public void setKeys(ArrayList<Word> keys)
public BTreeNode getChild(int i)
public ArrayList<BTreeNode> getChildren()
public void setChildren(ArrayList<BTreeNode> children)
public BTreeNode getParent()
public void setParent(BTreeNode x)
public boolean notLeaf()
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
```

### 2.2 B树的结构
#### 变量
```java
BTreeNode root;
private final int t;
```

#### 函数
##### 插入
先判断该结点内是否已经存在该key若存在则报错返回，再判断该结点是否需要分裂，然后找到key应该插入的位置。如果该结点不是叶结点，则一直递归到它是叶结点再插入。
###### 分裂
新建一个结点储存原结点的后半部分key，原结点改为储存前半部分key
```java
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
```
###### 确定key在指定结点对应位置
用的是二分法
```java
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
```
##### 更新
与插入大致相同，只是在遇到已存在的key时重设它的value再返回

##### 删除
先递归找到要删除的key所在的结点，再进行删除，如果找到叶结点了还没有则报错。在递归查找时如遇到长度不超过t-1的非根结点，则需要调整。删除时，非叶结点找继承者来替代删除结点，结点直接删除。

###### 删除调整
向兄弟借孩子，先将兄弟的key给父亲，再将父亲的key给需要增长的结点，若被转移的key的孩子也转过去，若左右兄弟都不够借，就合并
```java
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
```
###### 合并
```java
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
```
##### 查找
与删除时的定位基本一致，因B树我先写了删除，之后又忘记改了，所以这段代码和删除定位时的代码基本是一样的，而不是像红黑树是删除调用了查找函数

##### 遍历
类似于中序遍历吧，先递归到叶结点，再回来，再递归到叶结点
```java
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
```
#### 实现
与红黑树一致
