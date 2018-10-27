package com.alejandrolora.fruitsworld.adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.alejandrolora.fruitsworld.R;
import com.alejandrolora.fruitsworld.models.Fruit;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> fruits;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    // Pasamos el activity en vez del context, ya que nos hará falta para poder inflar en context menu
    public FruitAdapter(List<Fruit> fruits, int layout, Activity activity, OnItemClickListener listener) {
        this.fruits = fruits;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(fruits.get(position), this.listener);

    }


    @Override
    public int getItemCount() {
        return fruits.size();
    }

    // Implementamos las interfaces OnCreateContextMenuListener y OnMenuItemClickListener
    // para hacer uso del context menu en RecyclerView, y sobreescribimos los métodos
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewQuantity;
        public ImageView imageViewBackground;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewQuantity = itemView.findViewById(R.id.textViewQuality);
            imageViewBackground = itemView.findViewById(R.id.imageViewBackground);
            // Añadimos al view el listener para el context menu, en vez de hacerlo en
            // el activity mediante el método registerForContextMenu
            itemView.setOnCreateContextMenuListener(this);
        }

        private void bind(final Fruit fruit, final OnItemClickListener listener){
            this.textViewName.setText(fruit.getName());
            this.textViewDescription.setText(fruit.getDescription());
            this.textViewQuantity.setText(String.valueOf(fruit.getQuantity()));
            // Lógica aplicada para la limitación de la cantidad en cada elemento fruta
            if (fruit.getQuantity() == Fruit.LIMIT_QUANTITY){
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.colorAlert));
                textViewQuantity.setTypeface(null, Typeface.BOLD);
            } else{
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.defaultTextColor));
                textViewQuantity.setTypeface(null, Typeface.NORMAL);
            }
            // Cargamos la imagen con Picasso
            Picasso.with(activity).load(fruit.getImgBackground()).fit().into(this.imageViewBackground);
            // Añadimos el listener click para cada elemento fruta
            this.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   listener.onItemClick(fruit,getAdapterPosition());
                }
            });
        }

        // Sobreescribimos onCreateContextMenu, dentro del ViewHolder,
        // en vez de hacerlo en el activity

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            // Recogemos la posición con el método getAdapterPosition
            Fruit fruitSelected = fruits.get(this.getAdapterPosition());
            // Establecemos título e icono para cada elemento, mirando en sus propiedades
            contextMenu.setHeaderTitle(fruitSelected.getName());
            contextMenu.setHeaderIcon(fruitSelected.getImgIcon());
            // Inflamos el menú
            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu_fruits, contextMenu);
            // Por último, añadimos uno por uno, el listener onMenuItemClick para
            // controlar las acciones en el contextMenu, anteriormente lo manejábamos
            // con el método onContextItemSelected en el activity
            for (int i =0; i<contextMenu.size(); i++){
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // No obtenemos nuestro objeto info porque la posición la podemos rescatar desde getAdapterPosition
            switch (item.getItemId()){
                case R.id.delete_fruit:
                    // Observa que como estamos dentro del adaptador, podemos acceder
                    // a los métodos propios de él como notifyItemRemoved o notifyItemChanged
                    fruits.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                case R.id.reset_fruit_quantity:
                    fruits.get(getAdapterPosition()).resetQuantity();
                    notifyItemChanged(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Fruit fruit, int position);
    }
}

