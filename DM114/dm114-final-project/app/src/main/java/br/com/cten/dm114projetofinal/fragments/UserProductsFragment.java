package br.com.cten.dm114projetofinal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.adapters.UserProductsAdapter;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProductsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private UserProductsAdapter mAdapter;

    List<Product> allProducts;

    public UserProductsFragment() {
        // Required empty public constructor
    }

    public static UserProductsFragment newInstance() {
        UserProductsFragment fragment = new UserProductsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_products, container, false);

        mRecyclerView = view.findViewById(R.id.rv_products);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        FloatingActionButton fabAddProduct = view.findViewById(R.id.fab_add_product);
        fabAddProduct.setOnClickListener(v -> {

            // Add product
            Fragment fragment = AddProductsFragment.newInstance();
            getActivity()
                    .getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        });

        if (savedInstanceState != null) {
            allProducts = (List<Product>) savedInstanceState.getSerializable("allProducts");
            setAdapter();
        } else {
            getAllProducts();
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("allProducts", (Serializable) allProducts);
        super.onSaveInstanceState(outState);
    }

    public void getAllProducts() {

        RequestsManager.getInstance(getActivity()).getAllUserProducts(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                allProducts = response.body();

                if(allProducts != null) {

                    // specify an adapter (see also next example)
                    if(mAdapter == null) {
                        setAdapter();
                    } else {
                        mAdapter.updateDataSet(allProducts);
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Falha ao buscar pedidos de interesse.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAdapter() {
        mAdapter = new UserProductsAdapter(getActivity(), allProducts);
        mRecyclerView.setAdapter(mAdapter);
    }
}
