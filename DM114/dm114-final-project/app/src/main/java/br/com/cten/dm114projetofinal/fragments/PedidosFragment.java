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
import br.com.cten.dm114projetofinal.adapters.OrdersAdapter;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Order;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosFragment extends Fragment {

    private List<Order> allOrders;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public PedidosFragment() {
        // Required empty public constructor
    }

    public static PedidosFragment newInstance() {
        return new PedidosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        mRecyclerView = view.findViewById(R.id.rv_orders);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        getAllOrders();

        return view;
    }

    private void getAllOrders() {

        RequestsManager.getInstance(getActivity()).getAllOrders(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {

                allOrders = response.body();

                if(allOrders != null) {
                    mAdapter = new OrdersAdapter(getActivity(), allOrders);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Nenhum pedido criado.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(getActivity().getApplicationContext(), "Falha ao buscar pedidos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
