package com.example.smarty.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smarty.R;
import com.example.smarty.databinding.CartFragmentBinding;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private CartFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        cartViewModel=new ViewModelProvider(this).get(CartViewModel.class);
        binding=CartFragmentBinding.inflate(inflater,container,false);


        View root=binding.getRoot();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}