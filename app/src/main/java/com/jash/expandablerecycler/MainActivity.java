package com.jash.expandablerecycler;

import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TreeAdapter adapter;
    private TreeAdapter.TreeViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        List<Tree<?>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tree<String> tree = new Tree<>(String.format("第%02d组", i));
            for (int j = 0; j < 10; j++) {
                Tree<String> child = new Tree<>(String.format("第%02d组 第%02d条", i, j));
                tree.addChild(child);
                for (int k = 0; k < 10; k++) {
                    Tree<String> child1 = new Tree<>(String.format("第%02d组 第%02d条 第%02d条", i, j, k));
                    child.addChild(child1);
                    for (int l = 0; l < 10; l++) {
                        child1.addChild(new Tree<Object>("叶节点"));
                    }
                }
            }
            list.add(tree);
        }
        FrameLayout frame = (FrameLayout) findViewById(R.id.main_layout);
        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        adapter = new TreeAdapter(this, list);
        recycler.setAdapter(adapter);
        holder = adapter.onCreateViewHolder(recycler, 0);
        holder.itemView.setClickable(false);
        frame.addView(holder.itemView);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                View child = recyclerView.getChildAt(0);
                int position = recyclerView.getChildAdapterPosition(child);
                //找到可显示的一个item属于哪个组
                for (int i = position; i >= 0 ; i--) {
                    if (adapter.getItemViewType(i) == 0) {
                        holder.itemView.setVisibility(View.VISIBLE);
                        adapter.onBindViewHolder(holder, i);
                        break;
                    }
                }
                //找到第一个显示的group item
                for (int i = 1; i < recyclerView.getChildCount(); i++) {
                    //在悬浮item的高度内没有找到group item
                    if (ViewCompat.getY(recyclerView.getChildAt(i)) > holder.itemView.getHeight()) {
                        ViewCompat.setTranslationY(holder.itemView, 0);
                        break;
                    }
                    //在悬浮item的高度内找到一个group item
                    if (adapter.getItemViewType(position + i) == 0) {
                        View view = holder.itemView;
                        View group_item = recyclerView.getChildAt(i);
                        ViewCompat.setTranslationY(view, ViewCompat.getY(group_item) - view.getHeight());
                        break;
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, RefreshActivity.class));
    }
}
