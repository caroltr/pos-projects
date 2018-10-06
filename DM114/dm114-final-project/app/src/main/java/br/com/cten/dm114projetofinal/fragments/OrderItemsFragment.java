package br.com.cten.dm114projetofinal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.adapters.OrderDetailsAdapter;
import br.com.cten.dm114projetofinal.infraestructure.RequestsManager;
import br.com.cten.dm114projetofinal.model.Order;
import br.com.cten.dm114projetofinal.model.OrderItem;

public class OrderItemsFragment extends Fragment {

    public OrderItemsFragment() {
        // Required empty public constructor
    }

    public static OrderItemsFragment newInstance(Order order) {
        OrderItemsFragment fragment = new OrderItemsFragment();

        Bundle args = new Bundle();
        args.putSerializable("order", order);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        if (getArguments() != null) {
            Order order = (Order) getArguments().getSerializable("order");

            TextView tvId = view.findViewById(R.id.tv_id);
            TextView tvEmail = view.findViewById(R.id.tv_email);
            TextView tvFreight = view.findViewById(R.id.tv_freight);
            RecyclerView rvOrderItems = view.findViewById(R.id.rv_order_items);

            tvId.setText(String.valueOf(order.getId()));
            tvEmail.setText(order.getEmail());
            tvFreight.setText(String.valueOf(order.getFreightPrice()));

            List<String> productsId = new ArrayList<>();
            for(OrderItem product : order.getOrderItems()) {
                productsId.add(product.getProductId());
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            rvOrderItems.setLayoutManager(layoutManager);

            OrderDetailsAdapter adapter = new OrderDetailsAdapter(productsId);
            rvOrderItems.setAdapter(adapter);
        }

        return view;
    }
}
