package com.example.smarty.ui.smartPhones;

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
import com.example.smarty.databinding.SmartPhonesBinding;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SmartPhonesFragment extends Fragment {

    private SmartPhonesViewModel smartPhonesViewModel;
    private SmartPhonesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        smartPhonesViewModel =
                new ViewModelProvider(this).get(SmartPhonesViewModel.class);

        binding = SmartPhonesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView = binding.smartPhonesItems;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        smartPhonesViewModel.list=new ArrayList<>();
        smartPhonesViewModel.clientAdapter=new ClientAdapter(getContext().getApplicationContext(), smartPhonesViewModel.list);
        recyclerView.setAdapter(smartPhonesViewModel.clientAdapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.progressBarSma.setVisibility(View.VISIBLE);
        smartPhonesViewModel.collectionReference.whereEqualTo("ProductCategory","smartphones").get().addOnCompleteListener(task->{

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
                    binding.progressBarSma.setVisibility(View.GONE);
                    smartPhonesViewModel.list.add(product);
                    smartPhonesViewModel.clientAdapter.notifyDataSetChanged();
                }
            }else{
                Toast.makeText(requireContext().getApplicationContext(), "veuillez v??rifier votre connexion internet", Toast.LENGTH_SHORT).show();
                binding.progressBarSma.setVisibility(View.GONE);
            }
        }).addOnFailureListener(failure->{
            Toast.makeText(requireContext().getApplicationContext(), "??chec du chargement des produits", Toast.LENGTH_SHORT).show();
            binding.progressBarSma.setVisibility(View.GONE);
        });
        binding.progressBarSma.setVisibility(View.GONE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.progressBarSma.setVisibility(View.GONE);
    }
}