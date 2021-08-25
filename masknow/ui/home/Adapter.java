package com.booknbooks.maskinfo.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.booknbooks.maskinfo.PopupActivity;
import com.booknbooks.maskinfo.R;
import com.booknbooks.maskinfo.ui.map.MapFragment;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater inflater;
    ArrayList<Home> pharms;
    Context context;

    FragmentManager fragmentManager;

    TextView txtResult;

    public Adapter(Context ctx, ArrayList<Home> pharms, FragmentManager fragmentManager){
        this.inflater = LayoutInflater.from(ctx);
        this.pharms = pharms;
        this.context = ctx;

        this.fragmentManager = fragmentManager;

    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_layout,parent,false);

        //추가
//        BottomNavigationView bottom = inflater.inflate
//                (R.layout.activity_main, parent, false).findViewById(R.id.nav_view);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        // bind the data
        holder.pharmName.setText(pharms.get(position).getName());
        holder.pharmAddr.setText(pharms.get(position).getaddress());

        if(pharms.get(position).getremain().equals("plenty")) {
            if(pharms.get(position).getType().equals("01")){
            holder.pharmRemain.setImageResource(R.drawable.pharmacy1);
            } else if (pharms.get(position).getType().equals("02")) {
                holder.pharmRemain.setImageResource(R.drawable.post1);
            } else {
                holder.pharmRemain.setImageResource(R.drawable.hanaro1);
            }
        }

        else if (pharms.get(position).getremain().equals("some")){
            if(pharms.get(position).getType().equals("01")){
                holder.pharmRemain.setImageResource(R.drawable.pharmacy2);
            } else if (pharms.get(position).getType().equals("02")) {
                holder.pharmRemain.setImageResource(R.drawable.post2);
            } else {
                holder.pharmRemain.setImageResource(R.drawable.hanaro2);
            }
        }

        else if (pharms.get(position).getremain().equals("few")){
            if(pharms.get(position).getType().equals("01")){
                holder.pharmRemain.setImageResource(R.drawable.pharmacy3);
            } else if (pharms.get(position).getType().equals("02")) {
                holder.pharmRemain.setImageResource(R.drawable.post3);
            } else {
                holder.pharmRemain.setImageResource(R.drawable.hanaro3);
            }
        }

        else if (pharms.get(position).getremain().equals("empty")){
            if(pharms.get(position).getType().equals("01")){
                holder.pharmRemain.setImageResource(R.drawable.pharmacy4);
            } else if (pharms.get(position).getType().equals("02")) {
                holder.pharmRemain.setImageResource(R.drawable.post4);
            } else {
                holder.pharmRemain.setImageResource(R.drawable.hanaro4);
            }
        }

        else {
            holder.pharmRemain.setImageResource(R.drawable.pharmacy4);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//               goFragment(fragmentManager, R.id.nav_host_fragment,
//                        MapFragment.newInstance(pharms.get(position).getLatitude(),pharms.get(position).getLongitude(),
//                                pharms.get(position).getaddress()));

                Intent intent = new Intent(context, PopupActivity.class);

                intent.putExtra("class",pharms.get(position));
                context.startActivity(intent);


            }
        });



        //holder.Type.setImageResource(R.drawable.pharmacy1);
        //Picasso.get().load(songs.get(position).getCoverImage()).into(holder.songCoverImage);

    }

    @Override
    public int getItemCount() {
        return pharms.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{
        TextView pharmName,pharmAddr;
        ImageView pharmRemain;

        //ImageView Type;
        //ImageView songCoverImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pharmName = itemView.findViewById(R.id.Title);
            pharmAddr = itemView.findViewById(R.id.Address);

            pharmRemain = itemView.findViewById(R.id.coverImage);


        }
    }

    public void setItemList(ArrayList<Home> pharms) {
        this.pharms=pharms;
        notifyDataSetChanged();
    }



    /**
     * 프래그먼트를 보여준다.
     *
     * @param fragmentManager 프래그먼트 매니저
     * @param containerViewId 프래그먼트를 보여줄 컨테이너 뷰 아이디
     * @param fragment        프래그먼트
     */
    public void goFragment(FragmentManager fragmentManager, int containerViewId,
                           Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }







}
