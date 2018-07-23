package com.example.ani.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private  List<Photo> list;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater =LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.list_item,viewGroup,false);
        return new MyViewHolder(view,i);
    }

    public MyAdapter(List<Photo> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        ImageView imageView = (ImageView) myViewHolder.imageView;
        TextView textView = myViewHolder.textView;
        textView.setText(list.get(i).getTitle());

        new ShowImage(imageView).execute("https://farm"+list.get(i).getFarm()+".staticflickr.com/"+list.get(i).getServer()+"/"+list.get(i).getId()+"_"+list.get(i).getSecret()+".jpg");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHolder( View itemView,int position) {
        super(itemView);

        imageView =(ImageView)itemView.findViewById(R.id.image);
        textView = (TextView)itemView.findViewById(R.id.description);
    }
}

}
