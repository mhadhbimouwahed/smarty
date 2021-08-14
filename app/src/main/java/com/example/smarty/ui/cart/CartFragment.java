package com.example.smarty.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarty.CartItemsAdapter;
import com.example.smarty.Product;
import com.example.smarty.R;
import com.example.smarty.databinding.CartFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class CartFragment extends Fragment {

    private CartViewModel cartViewModel;
    private CartFragmentBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){


        cartViewModel=new ViewModelProvider(this).get(CartViewModel.class);
        binding=CartFragmentBinding.inflate(inflater,container,false);

        View root=binding.getRoot();
        final RecyclerView recyclerView=binding.cartItems;

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));
        cartViewModel.list=new ArrayList<>();
        cartViewModel.cartItemsAdapter=new CartItemsAdapter(getContext().getApplicationContext(), cartViewModel.list);
        recyclerView.setAdapter(cartViewModel.cartItemsAdapter);
        firebaseAuth=FirebaseAuth.getInstance();


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.totalPrise.setText("");
        user=firebaseAuth.getCurrentUser();
        if(user!=null){
            cartViewModel.collectionReference.document(user.getEmail()).collection("currentUserCart").get()
                    .addOnCompleteListener(task->{
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
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
                                cartViewModel.list.add(product);
                                cartViewModel.cartItemsAdapter.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), "There are no products in the cart yet", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(failure->{
                        Log.d("ERROR_DISPLAYING_CART_PRODUCTS",failure.getMessage());
            });
        }else{
            Toast.makeText(getContext().getApplicationContext(), "you need to be logged in", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}

/**/

