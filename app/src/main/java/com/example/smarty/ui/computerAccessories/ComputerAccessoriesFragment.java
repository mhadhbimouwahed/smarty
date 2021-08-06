package com.example.smarty.ui.computerAccessories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smarty.ClientAdapter;
import com.example.smarty.Product;
import com.example.smarty.databinding.ComputerAccessoriesBinding;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ComputerAccessoriesFragment extends Fragment {

    private ComputerAccessoriesViewModel computerAccessoriesViewModel;
    private ComputerAccessoriesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        computerAccessoriesViewModel =
                new ViewModelProvider(this).get(ComputerAccessoriesViewModel.class);

        binding = ComputerAccessoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = binding.computerAccessoriesItems;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        computerAccessoriesViewModel.list=new ArrayList<>();
        computerAccessoriesViewModel.clientAdapter=new ClientAdapter(getContext().getApplicationContext(), computerAccessoriesViewModel.list);
        recyclerView.setAdapter(computerAccessoriesViewModel.clientAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        computerAccessoriesViewModel.collectionReference.whereEqualTo("ProductCategory","accessories").get().addOnCompleteListener(task->{

            if(task.isSuccessful()){

                for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                    HashMap<String,Object> data=new HashMap<>(documentSnapshot.getData());
                    Product product=new Product(data.get("PID"),
                            data.get("ProductName"),
                            data.get("ProductPrise"),
                            data.get("ProductDescription"),
                            data.get("ProductCategory"),
                            data.get("ProductImage"),
                            data.get("ProductManufacturer"),
                            data.get("InStock"),
                            data.get("ProductDiscount"));
                    computerAccessoriesViewModel.list.add(product);
                    computerAccessoriesViewModel.clientAdapter.notifyDataSetChanged();
                }
            }else{
                Toast.makeText(requireContext().getApplicationContext(), "please check you internet connection", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(failure->{
            Toast.makeText(requireContext().getApplicationContext(), "failed to load products", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}