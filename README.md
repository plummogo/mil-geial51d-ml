#   Okostelefon és IoT eszközök programozása (mil-geial51d-ml) - Android Studio Bumblebee beadandó

## Beadandó ismertetése
A beadandó feladatom egy teendő lista applikáció Kotlin[^1] nyelvbe írva, Android Studio Bumblebee [^2] verziójú IDE-t használva. A felhasználó képes teendőket felvinni az alkalmazásba, teljesíteni illetve törölni a teljesített teendőket.

## Főbb project struktúra
```
└── 📁app
        └── 📁src
            ...
            └── 📁main
                └── 📁java/com/example/mil_geial51d_ml
                    └── MainActivity.kt
                    └── Todo.kt
                    └── TodoAdapter.kt
                └── 📁res
                    ...
                    └── 📁layout
                        └── activity_main.xml
                        └── item_todo.xml
      ...
      └── .gitignore
      └── build.gradle
```

### build.gradle konfiguráció
Kikellett egészíteni sorral, ugyanis ezzel engedélyezem a View Binding funkciót, amely megkönnyíti a kezelői felület-elemek programkódból való hozzáférését, anélkül, hogy findViewById hívásokat kellene használni.[^3]

```
viewBinding {
  enabled = true
}
```
### activity_main.xml
Ez a fő layout fájl az alkalmazásban, ahová felrakom az elemeket.
Elérési útja: ```app/src/main/res/layout/activity_main.xml```

#### RecyclerView 
Ez a beépített komponens lehetővé teszi listák és rácsok megjelenítését, ez lesz az alapja a Teendőknek (több teendő lista elemek felvitelét)[^4]

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width : itt definiálom a komponens szélességét, ```match_parent``` beállítással, a szélesség ki fog tölteni minden rendelkezésre álló helyet vízszintesen a szülőelemében.
  - android:layout_height : itt definiálom a komponens magasságát, ```0dp``` beállítással, a ConstraintLayout[^5] miatt a magassága dinamikusan, a korlátok által meghatározott tér kitöltésével fog változni.
  - app:layout_constraintBottom_toTopOf :  alsó korlátot az editTextTodoTitle ID-jű elem felső széléhez rögzíti, ami azt jelenti, hogy a RecyclerView alja az említett elem tetejéig terjed.
  - app:layout_constraintEnd_toEndOf : a jobb szélét a szülő elem végéhez igazítja.
  - app:layout_constraintStart_toStartOf : a bal szélét a szülő elem kezdetéhez igazítja.
  - app:layout_constraintTop_toTopOf : a tetejét a szülő elem tetejéhez igazítja.

```
<androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewTodoItems"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        app:layout_constraintBottom_toTopOf="@+id/editTextTodoTitle"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
```

#### EditText
Ez a beépített komponens lehetővé teszi, hogy egy input mezőt adjunk az alkalmazáshoz, ez lesz az alapja Teendők felviteléhez.[^6]

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width :  itt definiálom a komponens szélességét, ```0dp``` beállítással, a ConstraintLayout miatt a szélesség a korlátok határozzák meg, kifeszítve a megadott korlátok között.
  - android:layout_height : itt definiálom a komponens magasságát, ```wrap_content``` beállítással, elegendő helyet biztosít a beírt szöveg megjelenítésére, anélkül, hogy felesleges teret foglalna el.
  - android:hint : szöveges útmutatást jelenít meg, amikor nincs benne szöveg.
  - app:layout_constraintBottom_toBottomOf : az alját a szülőelem aljához rögzíti
  - app:layout_constraintEnd_toStartOf : a jobb oldalát a buttonAddTodo ID-jű elem bal oldalához igazítja, tehát az EditText és a gomb között nem marad szabad hely.
  - app:layout_constraintStart_toStartOf : a bal oldalát a szülőelem bal oldalához igazítja.

```
<EditText
        android:id="@+id/editTextTodoTitle"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="Enter Todo Title"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/buttonAddTodo"
        app:layout_constraintStart_toStartOf="parent" />
```

#### Button
Ez a beépített komponens lehetővé teszi, hogy egy gombot adjunk az alkalmazához, 2 gombot fogok hozzáadni: 

1. Hozzáadás (Add)
Ezzel a gombbal viszek fel új teendőt.

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width, android:layout_height : a gomb mérete igazodik megjelenő szöveg méretéhez, így csak annyi helyet foglal el, amennyi szükséges.
  - android:text : a gombon megjelenő szöveg.
  - app:layout_constraintBottom_toBottomOf : a gomb alját a szülőelem aljához rögzíti.
  - app:layout_constraintEnd_toStartOf : a gomb jobb oldalát buttonDeleteTodo ID-jű gomb bal oldalához igazítja. Ez azt jelenti, hogy a "Hozzáadás" gomb és a "Törlés" gomb között közvetlenül nem marad hely.
  - app:layout_editor_absoluteX : abszolút X koordinátát ad meg a layout.
```
  <Button
        android:id="@+id/buttonAddTodo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Add"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/buttonDeleteTodo"
        app:layout_editor_absoluteX="133dp" />
```
2. Törlés (Delete)
Ezzel a gombbal törlök teljesített teendőt.

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width, android:layout_height : a gomb mérete igazodik megjelenő szöveg méretéhez, így csak annyi helyet foglal el, amennyi szükséges.
  - android:text : a gombon megjelenő szöveg.
  - app:layout_constraintBottom_toBottomOf : a gomb alját a szülőelem aljához igazítja.
  - app:layout_constraintEnd_toEndOf : a gomb jobb szélét a szülőelem jobb széléhez igazítja.
  - app:layout_editor_absoluteX : abszolút X koordinátát ad meg a layout.

```
<Button
        android:id="@+id/buttonDeleteTodo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Delete"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_editor_absoluteX="133dp" />
```
### item_todo.xml
Ez a layout a teendők kinézetéért felelős.
Elérési útja: ```app/src/main/res/layout/item_todo.xml```

#### TextView
Ez a beépített komponens lehetővé teszi, hogy a teendők label-ét megjelenítsem. [^8]

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width : a szélességét 0 dp-re állítja, ConstraintLayout miatt, a szélességét a korlátok határozzák meg, kifeszítve a megadott korlátok között.
  - android:layout_height : a magasságát a benne lévő tartalomhoz igazítja, így csak annyi helyet foglal el, amennyi a megjelenített szöveg magasságához szükséges.
  - android:text : a megjelenítendő szöveg.
  - android:textSize : a szöveg méretét 24sp-re (skálapontok) növeli.
  - app:layout_constraintBottom_toBottomOf : az alját a szülőelem aljához rögzíti.
  - app:layout_constraintEnd_toStartOf : a jobb oldalát checkBoxTodo ID-jű elem bal oldalához igazítja.
  - app:layout_constraintStart_toStartOf : a bal oldalát a szülőelem bal oldalához igazítja.
  - app:layout_constraintTop_toTopOf : a tetejét a szülőelem tetejéhez rögzíti.

```
<TextView
        android:id="@+id/textViewTodoTitle"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="@Example"
        android:textSize="24sp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toStartOf="@+id/checkBoxTodo"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
```

#### CheckBox
Ez a beépített komponens lehetővé teszi, hogy a teendőket kipipáljuk.[^9]

Attribútumai:
  - android:id : itt definálom az id-t a komponensnek, amit később felfogok használni.
  - android:layout_width, android:layout_height : a mérete alkalmazkodik a benne lévő tartalomhoz, azaz csak annyi helyet foglal el, amennyi a megjelenített elem (a jelölőnégyzet és a szöveg, ha van) megjelenítéséhez szükséges.
  - app:layout_constraintBottom_toBottomOf :az alját a szülőelem aljához rögzíti.
  - app:layout_constraintEnd_toEndOf : a jobb szélét a szülőelem jobb széléhez igazítja.
  - app:layout_constraintTop_toTopOf : a tetejét a szülőelem tetejéhez rögzíti.

```
<CheckBox
        android:id="@+id/checkBoxTodo"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"/>
```

### MainActivity.kt
Activity osztály része amely az alkalmazás fő képernyőjét valósítja meg [^10]

#### Késői inicializálású változók deklarálása

  - todoAdapter: az adapter kezeli a teendők listáját a RecyclerView-ban.
  - binding: ViewBinding használatával inicializál egy változót, amely lehetővé teszi a layout elemek könnyű és biztonságos elérését.
    
```
private lateinit var todoAdapter: TodoAdapter
private lateinit var binding: ActivityMainBinding
```

#### onCreate metódus
Akkor hívódik meg, amikor az aktivitás létrejön, az alapvető inicializálást csinálom meg.

  - setContentView(R.layout.activity_main): az aktivitás felületének layoutja, később felülírják a ViewBindinggal.
  - todoAdapter: inicializálja a TodoAdapter-t egy üres MutableListtel.
  - binding: inicializálja a binding változót, amely lehetővé teszi a layout elemek programkódból történő elérését.
  - setContentView: beállítja a tartalmat a ViewBinding által generált root-ra, ami lehetővé teszi a ViewBinding használatát.
  - binding.recyclerViewTodoItems.adapter: beállítja a RecyclerView adapterét a todoAdapter-re.
  - binding.recyclerViewTodoItems.layoutManager: beállítja a RecyclerView LayoutManagerét[^11] egy új LinearLayoutManager[^12] példányra, ami lineáris listát hoz létre.

Teendő hozzáadása:
Amikor a felhasználó rákattint a Hozzáadás gombra, a kód létrehoz egy új Todo objektumot szövegével, hozzáadja ezt az adapter listájához, majd törli a beviteli mező tartalmát.

Teendő törlése:
Amikor a felhasználó rákattint a Törlés gombra, a kód meghívja a ```todoAdapter.deleteTodo()``` metódust, ami eltávolítja a kijelölt teendőket a listából.

```
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        todoAdapter = TodoAdapter(mutableListOf())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerViewTodoItems.adapter = todoAdapter
        binding.recyclerViewTodoItems.layoutManager = LinearLayoutManager(this)

        binding.buttonAddTodo.setOnClickListener {
            val todoTitle = binding.editTextTodoTitle.text.toString()
            if(todoTitle.isNotEmpty()) {
                val todo = Todo(todoTitle)
                todoAdapter.addTodo(todo)
                binding.editTextTodoTitle.text.clear()
            }
        }

        binding.buttonDeleteTodo.setOnClickListener {
            todoAdapter.deleteTodo()
        }
    }
```

### Todo.kt
Kotlin adat osztály definícióját tartalmazza, ami a Todo típusú objektumokat írja le, célja az adattárolás.[^13]
  - title: teendő címét tárolja.
  - isChecked: azt tárolja, hogy a teendő be van-e jelölve, alapértelmezés szerint a teendő nincs kijelölve.

```
data class Todo (
    val title: String,
    var isChecked: Boolean = false
)
```


### TodoAdapter.kt
Az adapter feladata, hogy összekapcsolja teendők listáját a RecyclerView-val, amely megjeleníti a teendőket.[^14]

- TodoAdapter: Az adapter, amely egy MutableList<Todo> típusú listát kap, megjelenítendő teendőket.
- TodoViewHolder: ViewHolder [^15], amely a RecyclerView egyes eleminek a nézeteit tárolja, ItemTodoBinding segítségével hozzáfér a layout elemekhez.
- onCreateViewHolder: új ViewHolder példányokat hoz létre, létrehozza az egyes listaelemek layoutját és inicializálja a ViewHolder-t.
- onBindViewHolder: összeköti a teendők adatait az egyes listaelemek nézeteivel.
- getItemCount: visszaadja a listában lévő elemek számát, ami szükséges a RecyclerView számára, hogy tudja, hány elemet kell megjeleníteni.
- addTodo: új teendőt ad hozzá a listához, és értesíti az adaptert, hogy egy új elem került beszúrásra.
  
  ![image](https://github.com/plummogo/mil-geial51d-ml/assets/16595977/f009198a-9130-437a-93ba-b2861fb15572)
- deleteTodo: eltávolítja az összes kijelölt teendőt a listából, és értesíti az adaptert az adatok változásáról.
  
  ![image](https://github.com/plummogo/mil-geial51d-ml/assets/16595977/f309c71e-7a3c-4521-bd4f-96c30ce783b9)
- toggleStrikeThrough: áthúzza a teendő címét, TextView paintFlags beállításával végzi, ahol a STRIKE_THRU_TEXT_FLAG használata jelzi az áthúzást. [^16]
  
  ![image](https://github.com/plummogo/mil-geial51d-ml/assets/16595977/26bab6ad-4c7a-4df0-bbd6-2f26e4186e50)


```
class TodoAdapter(private val todoList: MutableList<Todo>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    fun addTodo(newTodo: Todo) {
        todoList.add(newTodo)
        notifyItemInserted(todoList.size - 1)
    }

    fun deleteTodo() {
        todoList.removeAll { todo -> todo.isChecked}
        notifyDataSetChanged()
    }

    private fun toggleStrikeThrough(title: TextView, isChecked: Boolean) {
        if(isChecked) {
            title.paintFlags = title.paintFlags or STRIKE_THRU_TEXT_FLAG
        } else {
            title.paintFlags = title.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todoList[position]
        with(holder.binding) {
            textViewTodoTitle.text = currentTodo.title
            checkBoxTodo.isChecked = currentTodo.isChecked
            toggleStrikeThrough(textViewTodoTitle, currentTodo.isChecked)
            checkBoxTodo.setOnCheckedChangeListener { _, isChecked ->
                toggleStrikeThrough(textViewTodoTitle, isChecked)
                currentTodo.isChecked = !currentTodo.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}
```


[^1]:https://kotlinlang.org/
[^2]: https://hu.wikipedia.org/wiki/Caesar-rejtjel](https://developer.android.com/studio/releases/past-releases/as-bumblebee-release-notes)https://developer.android.com/studio/releases/past-releases/as-bumblebee-release-notes
[^3]: https://stackoverflow.com/questions/57117338/how-to-use-view-binding-in-android
[^4]: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView
[^5]: https://developer.android.com/reference/androidx/constraintlayout/widget/ConstraintLayout
[^6]: https://developer.android.com/reference/android/widget/EditText
[^7]: https://developer.android.com/reference/android/widget/Button
[^8]: https://developer.android.com/reference/android/widget/TextView
[^9]: https://developer.android.com/reference/android/widget/CheckBox
[^10]: https://developer.android.com/reference/kotlin/android/app/Activity
[^11]: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.LayoutManager 
[^12]: https://developer.android.com/reference/androidx/recyclerview/widget/LinearLayoutManager
[^13]: https://kotlinlang.org/docs/data-classes.html
[^14]: https://www.baeldung.com/kotlin/adapter-pattern
[^15]: https://developer.android.com/reference/androidx/recyclerview/widget/RecyclerView.ViewHolder
[^16]: https://developer.android.com/reference/android/graphics/Paint
