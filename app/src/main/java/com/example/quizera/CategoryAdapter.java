package com.example.quizera;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private final List<CategoryModel>cat_list;

    public CategoryAdapter(List<CategoryModel>cat_list){
        this.cat_list = cat_list;
    }
    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View myView;
        if(view == null)
        {
            myView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup,false);

        }
        else
        {
            myView= view;
        }
        myView.setOnClickListener(view1 -> {
            DbQuery.g_selected_cat_index = i;
            Intent intent = new Intent(view1.getContext(), StartTestActivity.class);
            view1.getContext().startActivity(intent);

        });

        TextView catName= myView.findViewById(R.id.catName);
        catName.setText(cat_list.get(i).getName());
        return myView;


    }
}
