package com.example.slugbooks.slugbooks;

import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class sendImgUrlToDB extends AsyncTask<String,Void,Void> {

    private StorageReference firebaseStorage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    DataModel dataModel;
    @Override
    protected Void doInBackground(String... strings) {

        firebaseStorage = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("users").child(firebaseAuth.getUid()).child("bookObject").setValue(strings[0]);

        return null;
    }

    /*
    if(igg != null)
        {
            //+ getFileExtension(selectedImage)
            final StorageReference fileRef = storageReference.child( "profilepic.png" );

            fileRef.putFile(igg).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    System.out.println("the downloaded uri is22222: " + task.getResult().toString());
                   // setImageURLstr(task.getResult().toString());
                    return fileRef.getDownloadUrl();
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    img = uri.toString();
                    System.out.println("the downloaded uri is333: " + uri.toString());
                  //  setImageURLstr(uri.toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("dint put it in the storage");
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        img = downloadUri.toString();

                        System.out.println("the downloaded uri is: " + downloadUri.toString());
                        //setImageURLstr(downloadUri.toString());

                    } else {
                        Toast.makeText(RegisterActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }
     */

}
