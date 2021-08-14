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
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.create();
                builder.setTitle("Error");
                builder.setMessage("Passwords must not match, please check again");
                builder.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                builder.show();
            }else{
                updatePasswordViewModel.user= updatePasswordViewModel.firebaseAuth.getCurrentUser();
                if (updatePasswordViewModel.user!=null){
                    updatePasswordViewModel.collectionReference.whereEqualTo("UserID",updatePasswordViewModel.user.getUid())
                            .get()
                            .addOnCompleteListener(firstTask->{
                                if (firstTask.isSuccessful()){
                                    for (QueryDocumentSnapshot documentSnapshot:firstTask.getResult()){
                                        HashMap<String,Object> hashMap=new HashMap<>(documentSnapshot.getData());
                                        if (!current_password.getText().toString().equals(hashMap.get("Password"))){
                                            AlertDialog.Builder not_found=new AlertDialog.Builder(getContext());
                                            not_found.create();
                                            not_found.setTitle("Error");
                                            not_found.setMessage("The current password you provided is incorrect");
                                            not_found.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                                            not_found.show();
                                        }else{
                                            updatePasswordViewModel.user.updatePassword(new_password.getText().toString())
                                                    .addOnCompleteListener(secondTask->{
                                                        if (secondTask.isSuccessful()){


                                                            hashMap.put("Password",new_password.getText().toString());

                                                            updatePasswordViewModel.collectionReference.document(updatePasswordViewModel.user.getUid()).update(hashMap)
                                                                    .addOnCompleteListener(thirdTask->{
                                                                        Toast.makeText(getContext().getApplicationContext(), "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                                        current_password.setText("");
                                                                        new_password.setText("");
                                                                    }).addOnFailureListener(thirdFailure->{
                                                                Log.d("ERROR_UPDATING_PASSWORD_USERS",thirdFailure.getMessage());
                                                            });
                                                        }else{
                                                            AlertDialog.Builder failed_to_change=new AlertDialog.Builder(getContext());
                                                            failed_to_change.create();
                                                            failed_to_change.setTitle("Error");
                                                            failed_to_change.setMessage("Failed to update the password, please contact the admin");
                                                            failed_to_change.setPositiveButton("Okay",((dialog, which) -> dialog.dismiss()));
                                                            failed_to_change.show();
                                                        }
                                                    }).addOnFailureListener(secondFailure->{
                                                Toast.makeText(getContext().getApplicationContext(), "The issue could be your internet connection", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    }

                                }else{
                                    Toast.makeText(getContext().getApplicationContext(), "There was a database error, please try later", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(firstFailure->{
                        Toast.makeText(getContext().getApplicationContext(), "The issue could be your internet connection", Toast.LENGTH_SHORT).show();
                    });
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "You are not logged in, this incident will be reported", Toast.LENGTH_SHORT).show();
                }
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
        binding=null;
    }
}