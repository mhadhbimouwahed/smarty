package com.example.smarty.ui.updatePassword;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smarty.R;
import com.example.smarty.databinding.CartFragmentBinding;
import com.example.smarty.databinding.UpdatePasswordFragmentBinding;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordFragment extends Fragment {

    private UpdatePasswordViewModel updatePasswordViewModel;
    private @NonNull UpdatePasswordFragmentBinding binding;

    public static UpdatePasswordFragment newInstance() {
        return new UpdatePasswordFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        updatePasswordViewModel=new ViewModelProvider(this).get(UpdatePasswordViewModel.class);
        binding= UpdatePasswordFragmentBinding.inflate(inflater,container,false);

        View root=binding.getRoot();

        final EditText current_password=binding.currentPassword;
        final EditText new_password=binding.newPassword;
        final TextView change_password=binding.changePassword;
        


        change_password.setOnClickListener(x->{
            if (current_password.getText().toString().equals("")){
                current_password.setError("This field cannot be empty");
            }else if(new_password.getText().toString().equals("")){
                new_password.setError("This field cannot be empty");
            }else if(current_password.getText().toString().equals(new_password.getText().toString())){
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext().getApplicationContext());
                builder.create();
                builder.setTitle("Error");
                builder.setMessage("Passwords must not match, please check again");
                builder.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                builder.show();
            }else{
                updatePasswordViewModel.collectionReference.whereEqualTo("UserID",updatePasswordViewModel.firebaseAuth.getUid()).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                    HashMap<String,Object> hashMap=new HashMap<>(documentSnapshot.getData());
                                    if (hashMap.get("Password").equals(current_password.getText().toString())){
                                        updatePasswordViewModel.firebaseAuth.getCurrentUser().updatePassword(new_password.getText().toString())
                                                .addOnCompleteListener(secondTask->{
                                                    if (secondTask.isSuccessful()){
                                                        hashMap.put("Email",hashMap.get("Email"));
                                                        hashMap.put("FirstName",hashMap.get("FirstName"));
                                                        hashMap.put("LastName",hashMap.get("LastName"));
                                                        hashMap.put("Password",new_password.getText().toString());
                                                        hashMap.put("UserID",hashMap.get("UserID"));

                                                        updatePasswordViewModel.collectionReference.document(updatePasswordViewModel.firebaseAuth.getUid())
                                                                .update(hashMap).addOnCompleteListener(thirdTask->{
                                                                    if (thirdTask.isSuccessful()){
                                                                        Toast.makeText(getContext().getApplicationContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                                        current_password.setText("");
                                                                        new_password.setText("");
                                                                    }else {
                                                                        Toast.makeText(getContext().getApplicationContext(), "There was a problem updating the password", Toast.LENGTH_SHORT).show();
                                                                        current_password.setText("");
                                                                        new_password.setText("");
                                                                    }
                                                        }).addOnFailureListener(thirdFailure->{
                                                            Log.d("Error updating the password",thirdFailure.getMessage());
                                                        });
                                                    }else{
                                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext().getApplicationContext());
                                                        builder.create();
                                                        builder.setTitle("Error");
                                                        builder.setMessage("Couldn't update the password");
                                                        builder.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                                                        builder.show();
                                                    }
                                                }).addOnFailureListener(secondFailure->{
                                                    Log.d("Error updating the password",secondFailure.getMessage());
                                        });
                                    }else{
                                        AlertDialog.Builder builder=new AlertDialog.Builder(getContext().getApplicationContext());
                                        builder.create();
                                        builder.setTitle("Error");
                                        builder.setMessage("The current password you provided is incorrect");
                                        builder.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                                        builder.show();
                                    }
                                }
                            }else{
                                Toast.makeText(getContext().getApplicationContext(), "There is no user with such ID", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(failure->{
                            Log.d("Error getting the user",failure.getMessage());
                });
            }
        });



        return root;
    }



    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}