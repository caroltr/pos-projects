package br.com.cten.dm114projetofinal.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProductsAdapter extends RecyclerView.Adapter<UserProductsAdapter.ProductsViewHolder> {

    private Context mContext;
    private List<Product> mAllProducts;

    public UserProductsAdapter(Context context, List<Product> allProducts) {
        mContext = context;
        mAllProducts = allProducts;
    }

    public void updateDataSet(List<Product> allProducts) {
        mAllProducts = allProducts;
    }

    static class ProductsViewHolder extends RecyclerView.ViewHolder {

        TextView mProductCode;
        TextView mProductName;
        TextView mProductPrice;

        ProductsViewHolder(View view) {
            super(view);

            mProductCode = view.findViewById(R.id.tv_product_code);
            mProductName = view.findViewById(R.id.tv_product_name);
            mProductPrice = view.findViewById(R.id.tv_product_price);
        }

       /* @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Remover");
        }*/

    }


    @NonNull
    @Override
    public UserProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new UserProductsAdapter.ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserProductsAdapter.ProductsViewHolder holder, int position) {

        holder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) ->
                menu.add("remover").setOnMenuItemClickListener(item -> {

                    deleteProduct(mAllProducts.get(position));
                    return true;
        }));

        holder.mProductCode.setText(mAllProducts.get(position).getCode());
        holder.mProductPrice.setText(String.valueOf(mAllProducts.get(position).getPrice()));
    }

    private void deleteProduct(Product product) {
        RequestsManager.getInstance(mContext).deleteProductOfInterest(product, new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {

                if(response.body() != null) {
                    mAllProducts.remove(product);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Falha ao remover produto de interesse.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Toast.makeText(mContext, "Falha ao remover produto de interesse.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAllProducts.size();
    }
}
