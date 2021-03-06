package com.example.smarty.ui.portableComputers;

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
import com.example.smarty.databinding.PortableComputersBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class PortableComputersFragment extends Fragment {

    private PortableComputersViewModel portableComputersViewModel;
    private PortableComputersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        portableComputersViewModel =
                new ViewModelProvider(this).get(PortableComputersViewModel.class);

        binding = PortableComputersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final RecyclerView recyclerView=binding.portableComputersItems;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext().getApplicationContext()));
        portableComputersViewModel.list=new ArrayList<>();
        portableComputersViewModel.clientAdapter=new ClientAdapter(requireContext().getApplicationContext(), portableComputersViewModel.list);
        recyclerView.setAdapter(portableComputersViewModel.clientAdapter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.progressBarPor.setVisibility(View.VISIBLE);
        portableComputersViewModel.collectionReference.whereEqualTo("ProductCategory","ordinateurs portables").get().addOnCompleteListener(task->{

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

                   binding.progressBarPor.setVisibility(View.GONE);
                   portableComputersViewModel.list.add(product);
                   portableComputersViewModel.clientAdapter.notifyDataSetChanged();
               }
           }else{
               Toast.makeText(requireContext().getApplicationContext(), "veuillez v??rifier votre connexion internet", Toast.LENGTH_SHORT).show();
               binding.progressBarPor.setVisibility(View.GONE);
           }
        }).addOnFailureListener(failure->{
            Toast.makeText(requireContext().getApplicationContext(), "??chec du chargement des produits", Toast.LENGTH_SHORT).show();
            binding.progressBarPor.setVisibility(View.GONE);
        });
        binding.progressBarPor.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.progressBarPor.setVisibility(View.GONE);
    }
}