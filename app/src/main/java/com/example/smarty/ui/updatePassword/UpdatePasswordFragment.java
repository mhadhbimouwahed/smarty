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
                current_password.setError("Ce champ ne peut pas être vide");
            }else if(new_password.getText().toString().equals("")){
                new_password.setError("Ce champ ne peut pas être vide");
            }else if(current_password.getText().toString().equals(new_password.getText().toString())){
                AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
                builder.create();
                builder.setTitle("Erreur");
                builder.setMessage("Les mots de passe ne doivent pas correspondre, veuillez vérifier à nouveau");
                builder.setPositiveButton("OK",((dialog, which) -> dialog.dismiss()));
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
                                            not_found.setTitle("Erreur");
                                            not_found.setMessage("Le mot de passe actuel que vous avez fourni est incorrect");
                                            not_found.setPositiveButton("OK",((dialog, which) -> dialog.dismiss()));
                                            not_found.show();
                                        }else{
                                            updatePasswordViewModel.user.updatePassword(new_password.getText().toString())
                                                    .addOnCompleteListener(secondTask->{
                                                        if (secondTask.isSuccessful()){


                                                            hashMap.put("Password",new_password.getText().toString());

                                                            updatePasswordViewModel.collectionReference.document(updatePasswordViewModel.user.getUid()).update(hashMap)
                                                                    .addOnCompleteListener(thirdTask->{
                                                                        Toast.makeText(getContext().getApplicationContext(), "Mot de passe mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                                                        current_password.setText("");
                                                                        new_password.setText("");
                                                                    }).addOnFailureListener(thirdFailure->{
                                                                Log.d("ERROR_UPDATING_PASSWORD_USERS",thirdFailure.getMessage());
                                                            });
                                                        }else{
                                                            AlertDialog.Builder failed_to_change=new AlertDialog.Builder(getContext());
                                                            failed_to_change.create();
                                                            failed_to_change.setTitle("Erreur");
                                                            failed_to_change.setMessage("Failed to update the password, please contact the admin");
                                                            failed_to_change.setPositiveButton("OK",((dialog, which) -> dialog.dismiss()));
                                                            failed_to_change.show();
                                                        }
                                                    }).addOnFailureListener(secondFailure->{
                                                Toast.makeText(getContext().getApplicationContext(), "Le problème pourrait être votre connexion Internet", Toast.LENGTH_SHORT).show();
                                            });
                                        }
                                    }

                                }else{
                                    Toast.makeText(getContext().getApplicationContext(), "Il y a eu une erreur dans la base de données, veuillez réessayer plus tard", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(firstFailure->{
                        Toast.makeText(getContext().getApplicationContext(), "Le problème pourrait être votre connexion Internet", Toast.LENGTH_SHORT).show();
                    });
                }else{
                    Toast.makeText(getContext().getApplicationContext(), "Vous n'êtes pas connecté, cet incident sera signalé", Toast.LENGTH_SHORT).show();
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