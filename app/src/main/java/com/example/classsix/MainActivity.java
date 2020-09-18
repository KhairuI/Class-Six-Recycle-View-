package com.example.classsix;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ClickInterface{

    private String[] playerName;
    private List<Model> playerList;
    private Model model;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private MyAdapter adapter;
    private String deletePlayer="";


    private int[] image={R.drawable.messi,R.drawable.shakib,
            R.drawable.neymar,R.drawable.mushfiqur,R.drawable.tamim,R.drawable.bill,
            R.drawable.mark,R.drawable.jamal,R.drawable.siddikur,R.drawable.virat};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerList= new ArrayList<>();
        recyclerView= findViewById(R.id.recycleViewId);
        refreshLayout= findViewById(R.id.refreshId);

        playerName= getResources().getStringArray(R.array.player);

        for(int i=0;i<playerName.length;i++){
            model= new Model(image[i],playerName[i]);
            playerList.add(model);
        }

        adapter= new MyAdapter(playerList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model= new Model(R.drawable.mark,"Mark Jukarbarg");
                playerList.add(model);
                adapter.notifyDataSetChanged();
                refreshLayout.setRefreshing(false);
            }
        });

        ItemTouchHelper itemTouchHelper= new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    ItemTouchHelper.SimpleCallback simpleCallback= new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper
            .END|ItemTouchHelper.DOWN|ItemTouchHelper.START,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position= viewHolder.getAdapterPosition();
            if(direction== ItemTouchHelper.LEFT || direction== ItemTouchHelper.RIGHT ){

                deletePlayer= playerList.get(position).getName();
                playerList.remove(position);
                adapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView,deletePlayer+" is Deleted",Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model= new Model(image[position],deletePlayer);
                        playerList.add(position,model);
                        adapter.notifyItemInserted(position);
                    }
                }).show();

            }



        }
    };

    @Override
    public void onItemClick(int position) {

        Toast.makeText(this, ""+playerList.get(position).getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLongItemClick(final int position) {

        AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Delete").setIcon(R.drawable.ic_delete).setMessage("Do you want to delete ?")
                .setCancelable(true).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               playerList.remove(position);
               adapter.notifyItemRemoved(position);
            }
        }).create().show();

    }
}