package com.example.android.employeeapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class All_Employers extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] BRANCH_NAME={"MANOJ","KRIHDNS","ASKKD"};
    String[] Name_logo={"mj","ks","ac"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employers);

        recyclerView=(RecyclerView)findViewById(R.id.employers_list);


        recyclerView.setHasFixedSize(true);

        CustomAdapter radapter=new CustomAdapter(BRANCH_NAME,Name_logo);

        RecyclerView.LayoutManager rlayoutmanager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setLayoutManager(rlayoutmanager);
        recyclerView.setAdapter(radapter);

        radapter.setonItemclicklistener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
//                Intent i=new Intent(All.this,Sub_Domain_list.class);
//                i.putExtra("Position",pos);
//                startActivity(i);
                Toast.makeText(All_Employers.this, "Selected", Toast.LENGTH_SHORT).show();
            }
        });


    }




    public static class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ExampleViewHolder> {

        public String[] names_array;
        public String[] logo_array;

        private OnItemClickListener mlister;
        public interface OnItemClickListener{
            void onItemClick(int pos);
        }

        public void setonItemclicklistener(OnItemClickListener listener)
        {
            mlister=listener;
        }


        public CustomAdapter(String[] n_array,String[] l_array)
        {
            names_array=n_array;
            logo_array=l_array;

        }

        public static class ExampleViewHolder extends RecyclerView.ViewHolder{

            public TextView t_logo;
            public TextView t_name;

            public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
                super(itemView);
                t_logo=(TextView)itemView.findViewById(R.id.id_name_logo);
                t_name=(TextView)itemView.findViewById(R.id.id_name);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener!=null){
                            int pos=getAdapterPosition();
                            if(pos!=RecyclerView.NO_POSITION){
                                listener.onItemClick(pos);
                            }
                        }
                    }
                });
            }
        }

        @NonNull
        @Override
        public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employer_details,viewGroup, false);
            ExampleViewHolder evh=new ExampleViewHolder(v,mlister);
            return evh;
        }

        @Override
        public void onBindViewHolder(@NonNull ExampleViewHolder viewHolder, int i) {
            String tlogo=logo_array[i];
            String tname=names_array[i];

            viewHolder.t_logo.setText(tlogo);
            viewHolder.t_name.setText(tname);

        }


        @Override
        public int getItemCount() {
            return 5;
        }

    }
}
