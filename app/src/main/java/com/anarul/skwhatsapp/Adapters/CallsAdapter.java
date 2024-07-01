package com.anarul.skwhatsapp.Adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.anarul.skwhatsapp.Models.Calls;
import com.anarul.skwhatsapp.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CallsAdapter extends RecyclerView.Adapter<CallsAdapter.CallsViewHolder>{
    private List<Calls> list;
    private Context context;

    public CallsAdapter(List<Calls> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_list,parent,false);
        return new CallsViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CallsViewHolder holder, int position) {
    Calls calls=list.get(position);

    holder.tvName.setText(calls.getUserName());
    holder.tvDate.setText(calls.getDate());


    if(calls.getCallType().equals("missed")){
        holder.arrow.setImageDrawable(context.getDrawable(R.drawable.incomming_arrow));
        holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_red_dark));
    }
    else if(calls.getCallType().equals("income")){
        holder.arrow.setImageDrawable(context.getDrawable(R.drawable.incomming_arrow));
        holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
    }
    else {
        holder.arrow.setImageDrawable(context.getDrawable(R.drawable.outgoing_arrow));
        holder.arrow.getDrawable().setTint(context.getResources().getColor(android.R.color.holo_green_dark));
    }

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class CallsViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName,tvDate;
        private CircleImageView profile;
        private ImageView arrow;
        public CallsViewHolder(View view) {
            super(view);
            tvName=view.findViewById(R.id.tv_name);
            tvDate=view.findViewById(R.id.tv_Date_Time);
            profile=view.findViewById(R.id.profile_call);
            arrow=view.findViewById(R.id.arrow_calls);

        }
    }
}
