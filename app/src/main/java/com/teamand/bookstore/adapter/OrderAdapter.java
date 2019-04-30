package com.teamand.bookstore.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamand.bookstore.R;
import com.teamand.bookstore.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    private List<Order> orderList;
    private Context context;

    public OrderAdapter(Context context, List<Order> orderList){
        this.context = context;
        this.orderList = orderList;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_order,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Order order = orderList.get(i);
        holder.tvIdOrder.setText(order.getId()+"");
        holder.tvPaymentMethod.setText(order.getPaymentMethod());
        holder.tvTime.setText(order.getDateCreate().toString());
        holder.tvStatus.setText(order.getStatus());
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;
        return orderList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        private TextView tvTime, tvPaymentMethod, tvStatus, tvIdOrder;
        public Holder(@NonNull View itemView) {
            super(itemView);
            tvIdOrder = itemView.findViewById(R.id.tv_id_order);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
