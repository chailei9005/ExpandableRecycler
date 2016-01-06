package com.jash.expandablerecycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.List;

/**
 * Created by jash
 * Date: 16-1-5
 * Time: 上午11:09
 */
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.TreeViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Tree<?>> list;
    private RecyclerView recyclerView;
    private MyItemAnimator animator;

    public TreeAdapter(Context context, List<Tree<?>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public TreeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.group_item, parent, false);
                break;
            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
                break;
            default:
                view = LayoutInflater.from(context).inflate(R.layout.child_item, parent, false);
                break;
        }
        view.setOnClickListener(this);
        return new TreeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TreeViewHolder holder, int position) {
        Tree<?> tree = list.get(position);
        holder.itemView.setPadding(tree.getLevel() * 50, 0, 0, 0);
        switch (getItemViewType(position)){
            case 0:
                holder.group_text.setTextSize(20 - tree.getLevel() * 2);
                holder.group_text.setText((String)tree.getData());
                holder.expand.setChecked(tree.isExpand());
                break;
            case 1:
                holder.child_text.setText((String)tree.getData());
                break;
        }
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isExpandable()?0:1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        animator = new MyItemAnimator();
        this.recyclerView = recyclerView;
        recyclerView.setItemAnimator(animator);
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Tree<?> tree = list.get(position);
        if (tree.isExpandable()) {
            if (tree.isExpand()) {
                removeAll(position + 1, tree.getChildren());
            } else {
                addAll(position + 1, tree.getChildren());
            }
            tree.setExpand(!tree.isExpand());
            notifyItemChanged(position);
        } else {
            Toast.makeText(v.getContext(), (String)tree.getData(), Toast.LENGTH_SHORT).show();
        }
    }
    public void addAll(int position, Collection<? extends Tree<?>> collection){
        list.addAll(position, collection);
        notifyItemRangeInserted(position, collection.size());
        int i = 1;
        for (Tree<?> tree : collection) {
            if (tree.isExpand()) {
                addAll(position + i, tree.getChildren());
                i += tree.getChildren().size();
            }
            i++;
        }
    }

    public void removeAll(int position, Collection<? extends Tree<?>> collection){
        int i = 1;
        for (Tree<?> tree : collection) {
            if (tree.isExpand()) {
                removeAll(position + i, tree.getChildren());
                i += tree.getChildren().size();
            }
            i++;
        }
        list.removeAll(collection);
        notifyItemRangeRemoved(position, collection.size());
    }
    public static class TreeViewHolder extends RecyclerView.ViewHolder{

        private CheckBox expand;
        private TextView group_text;
        private TextView child_text;
        private int position;

        public int getMyPosition() {
            return position;
        }

        public TreeViewHolder(View itemView) {
            super(itemView);
            expand = ((CheckBox) itemView.findViewById(R.id.expand));
            group_text = ((TextView) itemView.findViewById(R.id.group_text));
            child_text = ((TextView) itemView.findViewById(R.id.child_text));
        }
    }
}
