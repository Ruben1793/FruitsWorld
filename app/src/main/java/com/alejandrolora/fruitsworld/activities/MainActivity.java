package com.alejandrolora.fruitsworld.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.alejandrolora.fruitsworld.R;
import com.alejandrolora.fruitsworld.adapters.FruitAdapter;
import com.alejandrolora.fruitsworld.models.Fruit;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FruitAdapter adapter;

    // Lista de nuestro modelo, fruta
    private List<Fruit> fruits;

    // Variables
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.fruits = getAllFruits();

        recyclerView =findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);

        // Observa como pasamos el activity, con this. Podríamos declarar
        // Activity o Context en el constructor y funcionaría pasando el mismo valor, this
        adapter = new FruitAdapter(fruits, R.layout.recycler_view_fruit_item, this, new FruitAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Fruit fruit, int position) {
                fruit.addQuantity(1);
                adapter.notifyItemChanged(position);
            }
        }) ;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        // No registramos para el menu contexto nada aquí, lo movemos al ViewHolder del adaptador
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.option_menu, menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.add_fruit:
                int position = fruits.size();
                // Rescatamos el número de frutas para saber en que posición insertaremos
                fruits.add(position, new Fruit("Plum " + (++counter), "Fruit added by the user", R.drawable.plum_bg, R.mipmap.ic_plum, 0));
                adapter.notifyItemInserted(position);
                layoutManager.scrollToPosition(position);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private List<Fruit> getAllFruits() {
        return new ArrayList<Fruit>() {{
            add(new Fruit("Strawberry", "Strawberry description",R.drawable.strawberry_bg , R.mipmap.ic_strawberry, 0));
            add(new Fruit("Orange", "Orange description", R.drawable.orange_bg, R.mipmap.ic_orange, 0));
            add(new Fruit("Apple", "Apple description", R.drawable.apple_bg, R.mipmap.ic_apple, 0));
            add(new Fruit("Banana", "Banana description", R.drawable.banana_bg, R.mipmap.ic_banana, 0));
            add(new Fruit("Cherry", "Cherry description", R.drawable.cherry_bg, R.mipmap.ic_cherry, 0));
            add(new Fruit("Pear", "Pear description", R.drawable.pear_bg, R.mipmap.ic_pear, 0));
            add(new Fruit("Raspberry", "Raspberry description", R.drawable.raspberry_bg, R.mipmap.ic_raspberry, 0));
        }};
    }


}
