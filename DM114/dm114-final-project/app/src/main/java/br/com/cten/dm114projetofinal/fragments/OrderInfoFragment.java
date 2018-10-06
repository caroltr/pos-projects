package br.com.cten.dm114projetofinal.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.cten.dm114projetofinal.R;
import br.com.cten.dm114projetofinal.model.OrderInfo;

public class OrderInfoFragment extends Fragment {

    public OrderInfoFragment() {
        // Required empty public constructor
    }

    public static OrderInfoFragment newInstance(OrderInfo order) {
        OrderInfoFragment fragment = new OrderInfoFragment();

        Bundle args = new Bundle();
        args.putSerializable("orderInfo", order);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_notification_info, container, false);

        TextView id = view.findViewById(R.id.tv_id);
        TextView email = view.findViewById(R.id.tv_email);
        TextView status = view.findViewById(R.id.tv_status);
        TextView reason = view.findViewById(R.id.tv_reason);

        OrderInfo orderInfo = (OrderInfo) getArguments().getSerializable("orderInfo");

        id.setText(String.valueOf(orderInfo.getId()));
        email.setText(orderInfo.getEmail());
        status.setText(orderInfo.getStatus());
        reason.setText(orderInfo.getReason());

        return view;
    }
}
