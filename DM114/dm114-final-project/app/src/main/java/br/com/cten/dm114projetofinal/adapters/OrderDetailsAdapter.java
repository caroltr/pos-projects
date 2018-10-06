package br.com.cten.dm114projetofinal.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.cten.dm114projetofinal.R;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailsViewHolder> {

    private List<String> mAllProductsId;

    public OrderDetailsAdapter(List<String> allProductsId) {
        mAllProductsId = allProductsId;
    }

    static class OrderDetailsViewHolder extends RecyclerView.ViewHolder {

        TextView mProductId;

        OrderDetailsViewHolder(View view) {
            super(view);
            mProductId = view.findViewById(R.id.tv_product_id);
        }
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.OrderDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_id, parent, false);

        return new OrderDetailsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.OrderDetailsViewHolder holder, int position) {

        holder.mProductId.setText(mAllProductsId.get(position));
    }

    @Override
    public int getItemCount() {
        return mAllProductsId.size();
    }
}
