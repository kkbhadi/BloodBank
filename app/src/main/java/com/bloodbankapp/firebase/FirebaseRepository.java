package com.bloodbankapp.firebase;


import android.util.Log;

import androidx.annotation.NonNull;

import com.bloodbankapp.models.Doner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseRepository {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();


    public void signUpUser(String email, String password, final FirebaseNetworkCallback callback) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            callback.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }

    public void signInUser(String email, String password, final FirebaseNetworkCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            callback.onSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "signInWithEmail:failure", task.getException());
                            callback.onFailure(task.getException().getMessage());

                        }

                    }
                });
    }

    public void signOutUser() {
        FirebaseAuth.getInstance().signOut();
    }

    public void saveUserData(final Doner doner, final FirebaseNetworkCallback callback) {
        DatabaseReference donerNodeReference = database.getReference("Doners");
        donerNodeReference.child(doner.getDonerId()).setValue(doner).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(doner);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage());
            }
        });

    }

    public void fetchUserData(String donerId, final FirebaseNetworkCallback callback) {
        DatabaseReference ref = database.getReference("Doners").child(donerId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccess(snapshot.getValue(Doner.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    public void getDonerList(final FirebaseNetworkCallback callback) {
        DatabaseReference donerNodeReference = database.getReference("Doners");

        donerNodeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Doner> donarList = new ArrayList<>();
                for (DataSnapshot doner : snapshot.getChildren()) {
                    Doner donerObject = doner.getValue(Doner.class);
                    if (donerObject != null && donerObject.isDoner())
                        donarList.add(donerObject);
                }
                callback.onSuccess(donarList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });

    }

}
