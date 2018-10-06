package br.com.cten.dm114projetofinal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Product;
import br.com.cten.dm114projetofinal.model.ProductDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoFragment extends Fragment {

    TextView tvCode;
    TextView tvName;
    TextView tvDescription;
    TextView tvOldPrice;
    TextView tvNewPrice;

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    public static ProductInfoFragment newInstance(Product product) {
        ProductInfoFragment fragment = new ProductInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable("productOfInterest", product);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_notification_info, container, false);

        tvCode = view.findViewById(R.id.tv_code);
        tvName = view.findViewById(R.id.tv_name);
        tvDescription = view.findViewById(R.id.tv_description);
        tvOldPrice = view.findViewById(R.id.tv_old_price);
        tvNewPrice = view.findViewById(R.id.tv_new_price);

        Product product = (Product) getArguments().getSerializable("productOfInterest");

        if(product != null) {
            getProductDetails(product);
        }

        return view;
    }

    private void getProductDetails(Product product) {

        RequestsManager.getInstance(getActivity()).getProductDetails(product.getCode(), new Callback<ProductDetail>() {
            @Override
            public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {

                if(response.body() != null) {

                    ProductDetail productDetail = response.body();

                    tvCode.setText(productDetail.getCode());
                    tvName.setText(productDetail.getName());
                    tvDescription.setText(productDetail.getDescription());
                    tvOldPrice.setText(String.valueOf(productDetail.getPrice()));
                    tvNewPrice.setText(String.valueOf(product.getPrice()));

                } else {
                    Toast.makeText(getActivity(), "Falha ao buscar detalhes do produto.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetail> call, Throwable t) {
                Toast.makeText(getActivity(), "Falha ao buscar detalhes do produto.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
