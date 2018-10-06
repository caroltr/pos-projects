package br.com.cten.dm114projetofinal.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.ProductDetail;
import br.com.cten.dm114projetofinal.model.RequestProduct;
import br.com.cten.dm114projetofinal.model.UserCredentials;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ProductsViewHolder> {

    private Activity mActivity;
    private List<ProductDetail> mAllProducts;

    public AllProductsAdapter(Activity activity, List<ProductDetail> allProducts) {
        mActivity = activity;
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
    }

    @NonNull
    @Override
    public AllProductsAdapter.ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_detail, parent, false);

        return new AllProductsAdapter.ProductsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductsAdapter.ProductsViewHolder holder, int position) {

        holder.itemView.setOnClickListener(v -> {
            ProductDetail product = mAllProducts.get(position);
            addProduct(product);
        });

        holder.mProductCode.setText(mAllProducts.get(position).getCode());
        holder.mProductName.setText(mAllProducts.get(position).getName()); // TODO change to name, with new request
        holder.mProductPrice.setText(String.valueOf(mAllProducts.get(position).getPrice()));
    }

    @Override
    public int getItemCount() {
        return mAllProducts.size();
    }

    private void addProduct(ProductDetail product) {

        View view = getAddProductView();

        TextView tvCurrentPrice = view.findViewById(R.id.tv_current_price);
        EditText etProductPrice = view.findViewById(R.id.et_product_price);

        tvCurrentPrice.setText(String.valueOf(product.getPrice()));

        AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle("Adicionar \"" + product.getName() + "\"?")
                .setView(view)
                .setPositiveButton("OK", (dialog1, which) -> {

                    String login = getUserCredentialsStored().getLogin();

                    if(etProductPrice == null || etProductPrice.getText() == null
                            || etProductPrice.getText().toString().isEmpty()) {

                        Toast.makeText(mActivity.getApplicationContext(), "Preencha o campo com o preço desejado.", Toast.LENGTH_SHORT).show();
                    } else {

                        Long price = Long.valueOf(etProductPrice.getText().toString());

                        if (price >= product.getPrice()) {

                            Toast.makeText(mActivity.getApplicationContext(), "Preço inválido. Preencha com um valor menor que o preço atual.", Toast.LENGTH_SHORT).show();

                        } else {

                            RequestProduct reqProduct = new RequestProduct();
                            reqProduct.setCode(product.getCode());
                            reqProduct.setEmail(login);
                            reqProduct.setPrice(price);

                            RequestsManager.getInstance(mActivity).addNewProduct(reqProduct, new Callback<Product>() {
                                @Override
                                public void onResponse(Call<Product> call, Response<Product> response) {
                                    Product product = response.body();

                                    if (product != null) {
                                        Toast.makeText(mActivity.getApplicationContext(), "Produto de interesse cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mActivity.getApplicationContext(), "Falha ao cadastrar produto de interesse.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Product> call, Throwable t) {
                                    Toast.makeText(mActivity.getApplicationContext(), "Falha ao cadastrar produto de interesse.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create();
        dialog.show();
    }

    private View getAddProductView() {

        LayoutInflater inflater = mActivity.getLayoutInflater();
        return inflater.inflate(R.layout.layout_add_product, null);
    }

    private UserCredentials getUserCredentialsStored() {
        SharedPreferences preferences = mActivity.getSharedPreferences("PREFS_CREDENTIAL", MODE_PRIVATE);
        String login = preferences.getString("login", null);
        String password = preferences.getString("password", null);

        UserCredentials user = null;
        if(login != null && password != null) {
            user = new UserCredentials();
            user.setLogin(login);
            user.setPassword(password);
        }

        return user;
    }
}
