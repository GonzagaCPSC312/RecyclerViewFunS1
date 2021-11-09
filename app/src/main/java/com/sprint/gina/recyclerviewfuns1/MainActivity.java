package com.sprint.gina.recyclerviewfuns1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String TAG = "MainActivityTag";
    List<Book> books; // data source for our recycler view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // warmup
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        books = new ArrayList<>();
        books.add(new Book("The Sorcerer's Stone", "JK Rowling", 111));
        books.add(new Book("The Chamber of Secrets", "JK Rowling", 222));
        books.add(new Book("The Prisoner of Azkaban", "JK Rowling", 333));
        books.add(new Book("The Goblet of Fire", "JK Rowling", 444));

        // adapterviews and recyclerviews: a ViewGroup that get their child views from
        // an adapter
        // adapter: a middleman between a data source and adapterview/recyclerview
        // produces one view for each item in a data source
        // data source: static (e.g. fixed sized array that doesn't change) or
        // dynamic (e.g. list that grows and shrinks in size or a database cursor)

        // examples of adapterviews: Spinner, ListView (1 column), GridView (N columns)...
        // AdapterView vs RecyclerView
        // RecyclerView is like AdapterView 2.0... more performant
        // because it "recycles" views when they go off the screen
        // takes more code to set up though!!

        // game plan for setting up our RecyclerView
        // 1. set the layout manager
        // options: LinearLayoutManager, GridLayoutManager, StaggeredGridLayoutManager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // 2. set up the adapter
        // 2.A. set up a custom adapter by subclassing the abstract class
        // RecyclerView.Adapter
        // 2.B. set up a custom view holder by subclassing the abstract class
        // RecyclerView.ViewHolder
        // 2.C wire it all up!
        CustomAdapter adapter = new CustomAdapter();
        recyclerView.setAdapter(adapter);

        // 3. set up click listeners (plus alert dialogs)
        // normal clicks and long clicks

        // 4. make it all look better
        // lets make our own custom layout to inflate for each item
        // it will use a CardView
        // see Material Design for more info on how to design card views
        // https://material.io/components/cards#anatomy

        // 5. demo of our dynamic data source
        // if the data source changes (e.g. item is removed)
        // the adapter should be notified so it can force
        // a refresh of the recyclerview
        // example: in 5 seconds, let's remove the book at item position 1
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                books.remove(1);
//                // notify the adapter
//                adapter.notifyItemRemoved(1);
//            }
//        }, 5000);
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        // we are going to set up a CAM (contextual action mode) on
        // long click so the user can delete multiple items they have
        // selected
        boolean multiSelect = false;
        ActionMode actionMode;
        ActionMode.Callback callbacks;
        List<Book> selectedItems = new ArrayList<>(); // the list of currently selected items in CAM

        class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
//            TextView text1;
            CardView myCardView1;
            TextView myText1;
            ImageView myImage1;

            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

//                text1 = itemView.findViewById(android.R.id.text1);
                myCardView1 = itemView.findViewById(R.id.myCardView1);
                myText1 = itemView.findViewById(R.id.myText1);
                myImage1 = itemView.findViewById(R.id.myImage1);

                // wire 'em up!!
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }

            public void updateView(Book b) {
//                text1.setText(b.toString());
                myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                myText1.setText(b.toString());
                myImage1.setImageResource(R.drawable.placeholderimage);
            }

            public void selectItem(Book b) {
                if (multiSelect) {
                    if (selectedItems.contains(b)) {
                        // item is already selected, unselect it
                        selectedItems.remove(b);
                        myCardView1.setCardBackgroundColor(getResources().getColor(R.color.white));
                    }
                    else {
                        // item is not selected, select it
                        selectedItems.add(b);
                        myCardView1.setCardBackgroundColor(getResources().getColor(R.color.teal_200));
                    }
                    actionMode.setTitle(selectedItems.size() + " item(s) selected");
                }
            }

            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: ");
                selectItem(books.get(getAdapterPosition()));
            }

            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "onLongClick: ");
                // a demo of alert dialogs
                // use the AlertDialog.Builder class and method chaining
                // to set up an alert dialog
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Item Long Clicked")
//                        .setMessage("You clicked on an item")
//                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "OKAY", Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .setNegativeButton("Dismiss", null);
//                builder.show();

                // enter CAM on long click
                MainActivity.this.startActionMode(callbacks);
                // if you are not nested in MainActivity, you can get a reference
                // to MainActivity via v.getContext()
                selectItem(books.get(getAdapterPosition()));
                return true; // false means this callback did not "consume" the event
            }
        }

        public CustomAdapter() {
            super();

            callbacks = new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    multiSelect = true;
                    actionMode = mode;
                    // inflate the CAM menu
                    MenuInflater menuInflater = getMenuInflater();
                    menuInflater.inflate(R.menu.cam_menu, menu);
                    return true; // yes, enter CAM
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // called when the user clicks on a CAM menu item
                    switch (item.getItemId()) {
                        case R.id.deleteMenuItem:
                            // TODO: delete the items the user has currently selected
                            Toast.makeText(MainActivity.this, "TODO: delete items", Toast.LENGTH_SHORT).show();
                            mode.finish();
                            return true;
                    }
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                    multiSelect = false;
                    // TODO: go through selectedItems and change the card view color back
                    selectedItems.clear();
                    notifyDataSetChanged(); // not good practice (see TODO above)
                }
            };
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // inflate an XML layout for each child view
            // that is wrapped in a CustomViewHolder
            // that will initialize the values for the views in the layout
            // a few ways to set up the layout
            // 1. use a standard layout provided by android
//            View view = LayoutInflater.from(MainActivity.this)
//                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            // 2. use our own custom layout
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(R.layout.card_view_list_item, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
            // called when a ViewHolder needs to be updated to show
            // the data in the data source at position
            Book b = books.get(position);
            holder.updateView(b);
        }

        @Override
        public int getItemCount() {
            return books.size();
        }
    }
}