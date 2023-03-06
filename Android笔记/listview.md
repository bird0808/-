1. listview的使用需要用到适配器adapter,适配器用来链接数据，他是适配器视图的数据桥梁
   常见的适配器：
   ArrayAdapter<T>:用来绑定一个数组，支持泛型操作
	SimpleAdapter:用来绑定在xml中定义的控件对应的数据
	SimpleCursorAdapter:用来绑定游标得到的数据
   BaseAdapter:通用的基础适配器

2. 利用baseadapter的实例：
创建一个Item类来存放list item需要的数据，创建一个适配器类MyBaseAdapter，还有一个内部类ViewHolder 
```java
public class MainActivity extends AppCompatActivity {
    private ListView listview1;
    private ArrayList<Item> item;

    //    private ArrayList<Item> a={};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview1 = findViewById(R.id.listview1);
        item = new ArrayList<>();
        item.add(new Item(R.drawable.one, "标题", "文本"));
        item.add(new Item(R.drawable.two, "标题", "文本"));
        item.add(new Item(R.drawable.three, "标题", "文本"));
        MyBaseAdapter adapter = new MyBaseAdapter(this, item);
        listview1.setAdapter(adapter);
    }

}

class Item {
    private int picture;
    private String title;
    private String text;

    public Item(int picture, String title, String text) {
        this.picture = picture;
        this.title = title;
        this.text = text;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

class MyBaseAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<Item> item;

    public MyBaseAdapter(Context context, ArrayList<Item> item) {
        this.layoutInflater = LayoutInflater.from(context);//获得 LayoutInflater 实例
        this.item = item;
    }

    @Override
    public int getCount() {
        return 12;
    }//这个函数返回需要显示的item数量

    @Override
    public Object getItem(int i) {
        return item.get(i);
    }//返回item

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item, null, false);//绑定Item布局
            holder = new ViewHolder();//自定义内部类，对象holder用来存储文字和图片控件
            holder.picture = view.findViewById(R.id.picture);
            holder.title = view.findViewById(R.id.title);
            holder.text = view.findViewById(R.id.text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //取出app对象
        Item item = this.item.get(i % 3);//此处%3就可以无限轮播
        holder.picture.setBackgroundResource(item.getPicture());
        holder.title.setText(item.getTitle() + (i + 1));
        holder.text.setText(item.getText() + (i + 1));
        return view;
    }

    class ViewHolder {
        ImageView picture;
        TextView title;
        TextView text;
    }//这个类用来方便更新数据，减少不必要的findViewById操作
}
```
---
首先我们要知道setTag方法是干什么的，他是给View对象的一个标签，标签可以是任何内容，我们这里把他设置成了一个对象，因为我们是把vlist2.xml的元素抽象出来成为一个类ViewHolder，用了setTag，这个标签就是ViewHolder实例化后对象的一个属性。我们之后对于ViewHolder实例化的对象holder的操作，都会因为java的引用机制而一直存活并改变convertView的内容，而不是每次都是去new一个