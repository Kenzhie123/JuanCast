package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.JuanCast.myapplication.listadapters.PurchaseTransactionsListAdapter;
import com.JuanCast.myapplication.models.PurchaseTransaction;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PurchaseTransactions extends AppCompatActivity {

    private RecyclerView PT_PurchaseTransactionsRecyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactionList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    private ImageView logo;

    //navvar
    private ImageView Community;
    private ImageView Store;
    private ImageView Cast;
    private ImageView home;
    private ImageView profile;

    private TextView ads;
    private TextView t_purchasebutton;
    private TextView promo;
    private TextView cast;

    private SwipeRefreshLayout PT_PurchaseListRefreshLayout;

    ArrayList<PurchaseTransaction> purchaseTransactionList;

    private void initPurchaseTransactionList()
    {
        purchaseTransactionList = new ArrayList<>();
        PT_PurchaseListRefreshLayout.setRefreshing(true);
        firebaseFirestore.collection("transaction_history")
                .whereEqualTo("user_id",firebaseAuth.getCurrentUser().getUid())
                .whereIn("transaction_type", Arrays.asList("star_purchase","powerup_purchase"))
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                        for(DocumentSnapshot document : queryDocumentSnapshots.getDocuments())
                        {
                            Map<String,Object> data = document.getData();
                            PurchaseTransaction purchaseTransaction = null;
                            if(((String)data.get("transaction_type")).equals("star_purchase"))
                            {
                                purchaseTransaction =
                                        new PurchaseTransaction(
                                                document.getId(),
                                                (String)data.get("amount_charged"),
                                                (String)data.get("transaction_type"),
                                                (String)data.get("reference_number"),
                                                (Long)data.get("star"),
                                                (Timestamp) data.get("timestamp")
                                        );
                            }
                            else{
                                purchaseTransaction =
                                        new PurchaseTransaction(
                                                document.getId(),
                                                (String)data.get("amount_charged"),
                                                (String)data.get("transaction_type"),
                                                (String)data.get("reference_number"),
                                                (Timestamp) data.get("timestamp")
                                        );
                            }


                            purchaseTransactionList.add(purchaseTransaction);
                        }
                        PT_PurchaseTransactionsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        PT_PurchaseTransactionsRecyclerView.setAdapter(new PurchaseTransactionsListAdapter(getApplicationContext(),purchaseTransactionList));
                        PT_PurchaseListRefreshLayout.setRefreshing(false);

                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_purchase_transactions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        home = findViewById(R.id.home);
        profile = findViewById(R.id.profile);

        Community = findViewById(R.id.Community);
        Store = findViewById(R.id.Store);
        Cast = findViewById(R.id.Cast);
        ads = findViewById(R.id.ads);
        cast = findViewById(R.id.cast);
        t_purchasebutton = findViewById(R.id.t_purchasebutton);
        promo = findViewById(R.id.promo);
        PT_PurchaseTransactionsRecyclerView = findViewById(R.id.PT_PurchaseTransactionsRecyclerView);
        PT_PurchaseListRefreshLayout = findViewById(R.id.PT_PurchaseListRefreshLayout);

        initPurchaseTransactionList();

        PT_PurchaseListRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initPurchaseTransactionList();
            }
        });

        //navvar

        Community.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, PostActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Store.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, StarStore.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        Cast.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, Voting.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        home.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, Homepage.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        profile.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, Profile.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });



        ads.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, RewardActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });


        promo.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, RedemptionActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

        cast.setOnClickListener(v -> {
            Intent intent = new Intent(PurchaseTransactions.this, Transactions.class);
            startActivity(intent);
            overridePendingTransition(0, 0); // No animation
        });

    }
}