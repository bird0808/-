1.activity或fragment向fragment传送数据
每个fragment实例都可附带一个Bundle对象。该bundle包含键—值对，我们可以像附加extra
到Activity的intent中那样使用它们。一个键-值对即一个argument。
要创建fragment argument，首先需创建Bundle对象。然后，使用Bundle限定类型的put方法
（类似于Intent的方法），将argument添加到bundle中（代码如下所示）。
```java
Bundle args = new Bundle(); 
args.putSerializable(ARG_MY_OBJECT, myObject); 
args.putInt(ARG_MY_INT, myInt); 
args.putCharSequence(ARG_MY_STRING, myString);
```
要附加argument bundle给fragment，需调用Fragment.setArguments(Bundle)方法。而且，
还必须在fragment创建后、添加给activity前完成。
为满足以上要求，Android开发人员采取的习惯做法是：添加名为newInstance()的静态方
法给Fragment类。使用该方法，完成fragment实例及Dundle对象的创建，然后将argument放入
bundle中，最后再附加给fragment。
托管activity需要fragment实例时，转而调用newInstance()方法，而非直接调用其构造方法。
而且，为满足fragment创建argument的要求，activity可给newInstance()方法传入任何需要的参数。

也就是说activity获取extra数据后，通过newInstance方法，把extra数据传进去，在这个方法中，创建bundle，并传递数据给fragment，这样做的好处是可以保证fragment的独立性，保证其可以复用。不同于activity托管的fragment直接传递给托管fragment的activity，那样会破坏fragment的独立性。

如下代码就可以直接在另一个activity或者fragment中调用CrimeFragment的newInstance方法，传递数据。
```java
public class CrimeFragment extends Fragment { 
    private static final String ARG_CRIME_ID = "crime_id"; 
    private Crime mCrime; 
    private EditText mTitleField; 
    private Button mDateButton; 
    private CheckBox mSolvedCheckbox; 
    public static CrimeFragment newInstance(UUID crimeId) { 
        Bundle args = new Bundle(); 
        args.putSerializable(ARG_CRIME_ID, crimeId); 
        CrimeFragment fragment = new CrimeFragment(); 
        fragment.setArguments(args); 
        return fragment; 
    } 
}
```
2.fragment A取回fragment B的数据
- 设置目标fragment
  类似于activity间的关联，可将fragment A(CrimeFragment)设置成fragment B(DatePickerFragment)的目标fragment。
这样，在fragment A和fragment B被销毁并重建后，操作系统会重新关联它们。
调用以下Fragment方法可建立这种关联：
```public void setTargetFragment(Fragment fragment, int requestCode) ```
该方法有两个参数：目标fragment以及类似于传入startActivityForResult(...)方法的
请求代码。需要时，目标fragment使用请求代码确认是哪个fragment在回传数据。目标fragment和请求代码由FragmentManager负责跟踪管理，我们可调用fragment（设置目标fragment的fragment）的getTargetFragment()方法getTargetRequestCode()方法获取它们。
在CrimeFragment.java中，创建请求代码常量，然后将CrimeFragment设为DatePickerFragment实例的目标fragment，
```java
public class CrimeFragment extends Fragment { 
    private static final String ARG_CRIME_ID = "crime_id"; 
    private static final String DIALOG_DATE = "DialogDate"; 
    private static final int REQUEST_DATE = 0; 
    ...
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
    Bundle savedInstanceState) { 
 
    mDateButton.setOnClickListener(new View.OnClickListener() { 
        @Override 
        public void onClick(View v) { 
        FragmentManager manager = getFragmentManager(); 
        DatePickerFragment dialog = DatePickerFragment 
        .newInstance(mCrime.getDate()); 
        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE); 
        dialog.show(manager, DIALOG_DATE);
         } 
    }); 
    return v; 
    } 
}
```
- 传递数据给目标fragment
    建立CrimeFragment与DatePickerFragment之间的联系后，需将数据回传给CrimeFragment。
回传日期将作为extra附加给Intent。
    使用什么方法发送intent信息给目标fragment？虽然令人难以置信，但是我们会让
DatePickerFragment类调用CrimeFragment.onActivityResult(int, int, Intent)方法。
    Activity.onActivityResult(...)方法是ActivityManager在子activity被销毁后调用的父
activity方法。处理activity间的数据返回时，ActivityManager会自动调用Activity.onActivityResult(...)方法。父activity接收到Activity.onActivityResult(...)方法调用命令后，其FragmentManager会调用对应fragment的Fragment.onActivityResult(...)方法。
处理由同一activity托管的两个fragment间的数据返回时，可借用Fragment.onActivityResult(...)方法。因此，直接调用目标fragment的Fragment.onActivityResult(...)方法，
就能实现数据的回传。该方法恰好有我们需要的如下信息。
--请求代码：与传入setTargetFragment(...)方法的代码相匹配，告诉目标fragment返回
结果来自哪里。
--结果代码：决定下一步该采取什么行动。
--Intent：包含extra数据。
在DatePickerFragment类中，新建sendResult(...)私有方法，创建intent并将日期数据作为
extra附加到intent上。最后调用CrimeFragment.onActivityResult(...)方法
```java
private void sendResult(int resultCode, Date date) { 
    if (getTargetFragment() == null) { 
        return; 
    }
    Intent intent = new Intent(); 
    intent.putExtra(EXTRA_DATE, date); 
    getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent); 
}
```
在CrimeFragment中，覆盖onActivityResult(...)方法，从extra中获取日期数据，设置
对应Crime的记录日期，然后刷新日期按钮的显示
```java
@Override 
public void onActivityResult(int requestCode, int resultCode, Intent data) { 
    if (resultCode != Activity.RESULT_OK) { 
        return; 
    } 
    if (requestCode == REQUEST_DATE) { 
        Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE); 
        mCrime.setDate(date); 
        mDateButton.setText(mCrime.getDate().toString()); 
    } 
}
```

