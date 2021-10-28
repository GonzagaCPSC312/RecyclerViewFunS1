package com.sprint.gina.recyclerviewfuns1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
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

        // 4. make it all look better

        // 5. demo of our dynamic data source
    }

    class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
        class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView text1;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);

                text1 = itemView.findViewById(android.R.id.text1);
            }

            public void updateView(Book b) {
                text1.setText(b.toString());
            }
        }

        @NonNull
        @Override
        public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // inflate an XML layout for each child view
            // that is wrapped in a CustomViewHolder
            // that will initialize the values for the views in the layout
            // a few ways to set up the layout
            // 1. use a standard layout provided by android
            // 2. use our own custom layout
            View view = LayoutInflater.from(MainActivity.this)
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
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