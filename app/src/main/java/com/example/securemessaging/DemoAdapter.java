package com.example.securemessaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DemoAdapter  extends RecyclerView.Adapter<DemoviewHolder> {

    private List<ModelClass> userList;
    private final RecyclerViewInterface recyclerViewInterface;


    public DemoAdapter(List<ModelClass>userList, RecyclerViewInterface recyclerViewInterface) {
        this.userList=userList;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public DemoviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_layout_resource_file,parent,false);
        return new DemoviewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DemoviewHolder holder, int position) {

        int resource = userList.get(position).getImageview();
        String name= userList.get(position).getTextview1();
        String msg= userList.get(position).getTextview2();
        String time= userList.get(position).getTextview3();
        String line= userList.get(position).getDivider();

        holder.setData(resource,name,msg,time,line);


    }

    @Override
    public int getItemCount() {
        return userList.size();

    }
}

class DemoviewHolder extends RecyclerView.ViewHolder{



    private ImageView imageView;
    private TextView textView;
    private TextView textView2;
    private TextView textview3;
    private TextView divider;

    public DemoviewHolder( View itemView, RecyclerViewInterface recyclerViewInterface) {
        super(itemView);

        imageView=itemView.findViewById(R.id.imageview);
        textView=itemView.findViewById(R.id.Username);
        textView2=itemView.findViewById(R.id.lastmessage);
        textview3=itemView.findViewById(R.id.last_time);
        divider=itemView.findViewById(R.id.Divider);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewInterface != null ){
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(pos);
                    }
                }
            }
        });



    }




    public void setData(int resource, String name, String msg, String time,String line) {

        imageView.setImageResource(resource);
        textView.setText(name);
        textView2.setText(msg);
        textview3.setText(time);
        divider.setText(line);



    }
}

