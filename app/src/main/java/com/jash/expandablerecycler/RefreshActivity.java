package com.jash.expandablerecycler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import java.util.ArrayList;
import java.util.List;

public class RefreshActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        List<Tree<?>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Tree<String> tree = new Tree<>(String.format("第%02d组", i));
            for (int j = 0; j < 10; j++) {
                tree.addChild(new Tree<>(String.format("第%02d组 第%02d条", i, j)));
            }
            list.add(tree);
        }
        PullToRefreshRecyclerView pull = (PullToRefreshRecyclerView) findViewById(R.id.pull);
        RecyclerView recyclerView = pull.getRefreshableView();
        pull.setMode(PullToRefreshBase.Mode.BOTH);
        recyclerView.setAdapter(new TreeAdapter(this, list));
    }
}
