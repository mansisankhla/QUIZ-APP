package com.example.quizera;

import android.os.Build;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
    public static FirebaseFirestore g_firestore;

    public static  List<CategoryModel> g_catList = new ArrayList<>();
    public static List<QuestionModel>g_quesList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED=1;
    public static final int ANSWERED=2;
    public static ProfileModel myprofile = new ProfileModel("NA",null);

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void createUserData(String email, String name,MyCompleteListener completeListener)
    {
        Map<String,Object> userData = new ArrayMap<>();
        userData.put("EMAIL_ID",email);
        userData.put("NAME",name);
        userData.put("TOTAL_SCORE",0);

        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc,userData);
        DocumentReference countDoc = g_firestore.collection("USERS").document("TOTAL_USERS");
        batch.update(countDoc,"COUNT", FieldValue.increment(1));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                completeListener.onSuccess();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                completeListener.onFailure();

            }
        });
    }

    public static void getUserData(MyCompleteListener completeListener)
    {
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        myprofile.setName(documentSnapshot.getString("NAME"));
                        myprofile.setEmail(documentSnapshot.getString("EMAIL_ID"));
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadCategories(MyCompleteListener completeListener)
    {
        g_catList.clear();
        g_firestore.collection("QUIZ").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                Map<String, QueryDocumentSnapshot>docList = new ArrayMap<>();
                for (QueryDocumentSnapshot doc:queryDocumentSnapshots)
                {
                    docList.put(doc.getId(),doc);
                }
                QueryDocumentSnapshot catListDoc = docList.get("Categories");
                long catCount = catListDoc.getLong("COUNT");
                for (int i=1;i<=catCount;i++)
                {
                    String catID = catListDoc.getString("CAT"+String.valueOf(i)+"_ID");
                    QueryDocumentSnapshot catDoc = docList.get(catID);
                    String catName = catDoc.getString("NAME");
                    int time = catDoc.getLong("TIME").intValue();
                    g_catList.add(new CategoryModel(catID, catName,time));
                }
                completeListener.onSuccess();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                completeListener.onFailure();

            }
        });
    }

    public static void loadQuestions(MyCompleteListener completeListener)
    {
        g_quesList.clear();
        g_firestore.collection("Questions").whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc:queryDocumentSnapshots)
                        {
                            g_quesList.add(new QuestionModel(
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    -1,
                                    NOT_VISITED
                            ));
                        }
                        completeListener.onSuccess();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                completeListener.onFailure();

            }
        });
    }
    public static void loadData(MyCompleteListener completeListener)
    {
       loadCategories(new MyCompleteListener() {
           @Override
           public void onSuccess() {
               getUserData(completeListener);
           }

           @Override
           public void onFailure() {
           completeListener.onFailure();
           }
       });
}
}
