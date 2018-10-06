package br.com.cten.dm114projetofinal.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.fragments.OrderItemsFragment;
import br.com.cten.dm114projetofinal.model.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private Activity mContext;
    private List<Order> mAllOrders;

    public OrdersAdapter(Activity context, List<Order> allOrders) {
        mContext = context;
        mAllOrders = allOrders;
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {

        public TextView mOrderId;
        public TextView mFreightPrice;
        public TextView mAmountOrderItems;

        public OrdersViewHolder(View view) {
            super(view);
            mOrderId = view.findViewById(R.id.tvOrderId);
            mFreightPrice = view.findViewById(R.id.tvFreightPrice);
            mAmountOrderItems = view.findViewById(R.id.tvAmountOrderItems);
        }
    }

    @NonNull
    @Override
    public OrdersAdapter.OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);

        return new OrdersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> {

            Fragment fragment = OrderItemsFragment.newInstance(mAllOrders.get(position));
            mContext.getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        });

        holder.mOrderId.setText(String.valueOf(mAllOrders.get(position).getId()));
        holder.mFreightPrice.setText(String.valueOf(mAllOrders.get(position).getFreightPrice()));
        holder.mAmountOrderItems.setText(String.valueOf(mAllOrders.get(position).getOrderItems().size()));
    }

    @Override
    public int getItemCount() {
        return mAllOrders.size();
    }
}
