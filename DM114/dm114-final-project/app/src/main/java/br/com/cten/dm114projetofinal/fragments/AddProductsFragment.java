package br.com.cten.dm114projetofinal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.adapters.AllProductsAdapter;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.ProductDetail;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private AllProductsAdapter mAdapter;

    public AddProductsFragment() {
        // Required empty public constructor
    }

    public static AddProductsFragment newInstance() {
        AddProductsFragment fragment = new AddProductsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_products, container, false);

        mRecyclerView = view.findViewById(R.id.rv_products);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getAllProducts();

        return view;
    }

    public void getAllProducts() {

        RequestsManager.getInstance(getActivity()).getAllProducts(new Callback<List<ProductDetail>>() {
            @Override
            public void onResponse(Call<List<ProductDetail>> call, Response<List<ProductDetail>> response) {
                List<ProductDetail> allProducts = response.body();

                if(allProducts != null) {

                    mAdapter = new AllProductsAdapter(getActivity(), allProducts);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<ProductDetail>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Falha ao buscar produtos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
