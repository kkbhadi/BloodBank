package com.bloodbankapp.firebase;


import android.util.Log;

import androidx.annotation.NonNull;

import com.bloodbankapp.models.Donor;
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

    public void saveUserData(final Donor donor, final FirebaseNetworkCallback callback) {
        DatabaseReference donorNodeReference = database.getReference("Donors");
        donorNodeReference.child(donor.getDonorId()).setValue(donor).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callback.onSuccess(donor);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callback.onFailure(e.getMessage());
            }
        });

    }

    public void fetchUserData(String donorId, final FirebaseNetworkCallback callback) {
        DatabaseReference ref = database.getReference("Donors").child(donorId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onSuccess(snapshot.getValue(Donor.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    public void getDonorList(final FirebaseNetworkCallback callback) {
        DatabaseReference donorNodeReference = database.getReference("Donors");

        donorNodeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<Donor> donorList = new ArrayList<>();
                for (DataSnapshot donor : snapshot.getChildren()) {
                    Donor donorObject = donor.getValue(Donor.class);
                    if (donorObject != null && donorObject.isDonor())
                        donorList.add(donorObject);
                }
                callback.onSuccess(donorList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });

    }

}
