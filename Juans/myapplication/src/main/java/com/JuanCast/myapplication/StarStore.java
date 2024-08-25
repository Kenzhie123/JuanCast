package com.JuanCast.myapplication;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.JuanCast.myapplication.Receiver.StarChangeReceiver;
import com.JuanCast.myapplication.models.ProductStar;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.collect.ImmutableList;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StarStore extends AppCompatActivity {


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String currentUserId;

    private TextView available_stars;

    //navvar
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView home;

    private ImageView logo;

    private TextView tvSuns;
    private TextView ads;
    private TextView ups;

    private RelativeLayout noInternetLayout;
    private StarChangeReceiver networkChangeReceiver;

    private TextView SS_StarAmount1;
    private TextView SS_StarPrice1;
    private TextView SS_StarAmount2;
    private TextView SS_StarPrice2;
    private TextView SS_StarAmount3;
    private TextView SS_StarPrice3;
    private TextView SS_StarAmount4;
    private TextView SS_StarPrice4;
    private TextView SS_StarAmount5;
    private TextView SS_StarPrice5;
    private TextView SS_StarAmount6;
    private TextView SS_StarPrice6;

    private LinearLayout SS_MarketItem1;
    private LinearLayout SS_MarketItem2;
    private LinearLayout SS_MarketItem3;
    private LinearLayout SS_MarketItem4;
    private LinearLayout SS_MarketItem5;
    private LinearLayout SS_MarketItem6;

    private TextView SS_Overlay;
    private ProgressBar SS_ProgressBar;
    private ConstraintLayout SS_OverlayContainer;

    BillingClient billingClient;


    // Getter para sa noInternetLayout
    public RelativeLayout getNoInternetLayout() {
        return noInternetLayout;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_store);
        ImageView logo = findViewById(R.id.logo);

        auth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth instance
        firebaseFirestore = FirebaseFirestore.getInstance(); // Initialize FirebaseFirestore instance
        user = auth.getCurrentUser();
        currentUserId = user.getUid();

        available_stars = findViewById(R.id.available_stars);
        loadStarPoints(currentUserId, available_stars); // Pass currentUserId to loadUserName method

        SS_MarketItem1 = findViewById(R.id.SS_MarketItem1);
        SS_MarketItem2 = findViewById(R.id.SS_MarketItem2);
        SS_MarketItem3 = findViewById(R.id.SS_MarketItem3);
        SS_MarketItem4 = findViewById(R.id.SS_MarketItem4);
        SS_MarketItem5 = findViewById(R.id.SS_MarketItem5);
        SS_MarketItem6 = findViewById(R.id.SS_MarketItem6);
        SS_StarAmount1 = findViewById(R.id.SS_StarAmount1);
        SS_StarPrice1= findViewById(R.id.SS_StarPrice1);
        SS_StarAmount2= findViewById(R.id.SS_StarAmount2);
        SS_StarPrice2= findViewById(R.id.SS_StarPrice2);
        SS_StarAmount3= findViewById(R.id.SS_StarAmount3);
        SS_StarPrice3= findViewById(R.id.SS_StarPrice3);
        SS_StarAmount4= findViewById(R.id.SS_StarAmount4);
        SS_StarPrice4= findViewById(R.id.SS_StarPrice4);
        SS_StarAmount5= findViewById(R.id.SS_StarAmount5);
        SS_StarPrice5= findViewById(R.id.SS_StarPrice5);
        SS_StarAmount6= findViewById(R.id.SS_StarAmount6);
        SS_StarPrice6= findViewById(R.id.SS_StarPrice6);
        SS_Overlay= findViewById(R.id.SS_Overlay);
        SS_ProgressBar= findViewById(R.id.SS_ProgressBar);
        SS_OverlayContainer= findViewById(R.id.SS_OverlayContainer);




        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        //navvar
        Community = findViewById(R.id.Community);
        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        Cast = findViewById(R.id.Cast);
        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

/////////////////////////////////////////////////////////////////////////////////////////

        TextView tvSuns = findViewById(R.id.tvSuns);
        tvSuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Store.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ads = findViewById(R.id.tvAds);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, ads.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });

        TextView ups = findViewById(R.id.tvPowerUps);
        ups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StarStore.this, Powerups.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // Walang animation
            }
        });


        noInternetLayout = findViewById(R.id.noInternetLayout);
        networkChangeReceiver = new StarChangeReceiver();

        // Initial check for internet connectivity
        if (!isConnected()) {
            noInternetLayout.setVisibility(View.VISIBLE);
        } else {
            noInternetLayout.setVisibility(View.GONE);
        }


        //Google Play Billing Section
        initBilling();


        SS_MarketItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(0);
            }
        });
        SS_MarketItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(1);
            }
        });
        SS_MarketItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(2);
            }
        });
        SS_MarketItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(3);
            }
        });
        SS_MarketItem5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(4);
            }
        });
        SS_MarketItem6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseClick(5);
            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();

        // Register receiver to listen for network changes
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister receiver to avoid memory leaks
        unregisterReceiver(networkChangeReceiver);
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void loadStarPoints(String userId, TextView available_stars) {
        firebaseFirestore.collection("User").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long votingPoints = documentSnapshot.getLong("votingPoints");
                        if (votingPoints != null) {
                            available_stars.setText(String.valueOf(votingPoints));
                        } else {
                            available_stars.setText("No voting points available");
                        }
                    } else {
                        available_stars.setText("User not found");
                    }
                })
                .addOnFailureListener(e -> {
                    available_stars.setText("Failed to load voting points");
                    Log.e(TAG, "Error fetching voting points: " + e.getMessage());
                });
    }

    public void enable()
    {
        SS_OverlayContainer.setVisibility(View.GONE);
    }

    public void disable()
    {
        SS_OverlayContainer.setVisibility(View.VISIBLE);
    }

    private void updateStarCount(int count)
    {
        available_stars.setText(String.valueOf(count));
    }

    private void connectToGPlayBilling()
    {
        disable();

        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingServiceDisconnected() {
                        Log.d("BILLINGTAG","DISCONNECTED");
                        connectToGPlayBilling();

                    }

                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                        {

                            getStarsProductsDetails();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),billingResult.getDebugMessage(),Toast.LENGTH_LONG).show();
                            Log.d("BILLINGTAG","FAILED");
                        }
                    }
                }
        );
    }

    private void initBilling()
    {
        billingClient = BillingClient.newBuilder(getApplicationContext()).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null)
                {
                    for(Purchase purchase : list)
                    {

                        handlePurchase(purchase);
                    }
                }
                else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED)
                {
//                    Toast.makeText(getApplicationContext(),"You canceled your purchase",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Unexpected Error",Toast.LENGTH_LONG).show();
                }
            }
        }).enablePendingPurchases().build();
        connectToGPlayBilling();
    }

    private void handlePurchase(Purchase purchase)
    {
        ConsumeParams consumeParams = ConsumeParams.newBuilder().setPurchaseToken(purchase.getPurchaseToken()).build();

        ConsumeResponseListener listener = new ConsumeResponseListener() {
            @Override
            public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                {
                    updateUserPurchase(purchase.getProducts());
                }
            }
        };

        billingClient.consumeAsync(consumeParams,listener);
    }

    private void updateUserPurchase(List<String> productIDs)
    {
        //Get current star amount depending on product ID
        firebaseFirestore.collection("products").document("stars")
                        .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        long totalStarPurchased = 0;

                        String productIds = "";
                        for(String id : productIDs)
                        {
                            for(Map.Entry<String,Object> products : data.entrySet())
                            {
                                Map<String,Object> productAttributes = (Map<String, Object>) products.getValue();
                                if(productAttributes.get("product_id").equals(id))
                                {
                                    totalStarPurchased += (Long) productAttributes.get("star_amount");
                                    productIds += (productIds.equals("") ? id : ","+id );
                                }

                            }

                        }

                        Date dateNow = Calendar.getInstance().getTime();
                        Map<String,Object> transactHistoryAdd = new HashMap<>();
                        transactHistoryAdd.put("reference_number",productIds);
                        transactHistoryAdd.put("star",totalStarPurchased);
                        transactHistoryAdd.put("sun",0);
                        transactHistoryAdd.put("timestamp",new Timestamp(dateNow));
                        transactHistoryAdd.put("transaction_type","star_purchase");
                        transactHistoryAdd.put("amount_charged",getPriceFromProductID(productIds));
                        transactHistoryAdd.put("user_id",currentUserId);
                        firebaseFirestore.collection("transaction_history").add(transactHistoryAdd)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                         Log.d("TRANSACTIONTAG",documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                        //Update user star amount
                        int currentTotalStars = Integer.parseInt(available_stars.getText().toString());
                        Map<String,Object> starUpdate = new HashMap<>();
                        starUpdate.put("votingPoints",(currentTotalStars +totalStarPurchased));
                        long finalTotalStarPurchased = totalStarPurchased;
                        firebaseFirestore.collection("User")
                                .document(currentUserId)
                                .update(starUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Purchased successful",Toast.LENGTH_LONG).show();
                                        updateStarCount((int) (currentTotalStars + finalTotalStarPurchased));

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });


                    }
                });

    }

    private void setProductInformation(TextView starPrice, TextView starAmount,String price, String amount)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                starPrice.setText(price);
                starAmount.setText(amount);
            }
        }).start();
    }


    List<ProductDetails> currentProductDetails = null;
    private String getPriceFromProductID(String productID)
    {
        for(ProductDetails productDetail : currentProductDetails)
        {
            if(productDetail.getProductId().equals(productID))
            {
                return productDetail.getOneTimePurchaseOfferDetails().getFormattedPrice();
            }
        }

        return "0";
    }
    private void purchaseClick(int index)
    {

        Log.d("BILLINGTAG","CLICKED");
        List<BillingFlowParams.ProductDetailsParams> productDetailsParams = new ArrayList<>();
        productDetailsParams.add(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(currentProductDetails.get(index)).build());

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
        billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
    }

    private void getStarsProductsDetails()
    {

        firebaseFirestore.collection("products").document("stars").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ArrayList<ProductStar> productStarList = new ArrayList<>();
                List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
                for(int i = 1; i <=6 ;i++)
                {
                    String keyTemplate = "star_" + i;
                    Map<String,Object> productMap = (Map<String,Object>)documentSnapshot.get(keyTemplate);
                    ProductStar currentProduct = new ProductStar((String)productMap.get("product_id"),((Long)productMap.get("star_amount")).intValue());
                    productStarList.add(currentProduct);

                    productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId(currentProduct.getProductID()).setProductType(BillingClient.ProductType.INAPP).build());
                }

                QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(productList).build();
                billingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                        {
                            currentProductDetails = list;
                            for(ProductDetails productDetails : list)
                            {

                                String productID = productDetails.getProductId();
                                String price = productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice();
                                if(productID.equals(productStarList.get(0).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice1,SS_StarAmount1,price,String.valueOf(productStarList.get(0).getStarAmount()));
                                }
                                else if(productID.equals(productStarList.get(1).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice2,SS_StarAmount2,price,String.valueOf(productStarList.get(1).getStarAmount()));
                                }
                                else if(productID.equals(productStarList.get(2).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice3,SS_StarAmount3,price,String.valueOf(productStarList.get(2).getStarAmount()));
                                }
                                else if(productID.equals(productStarList.get(3).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice4,SS_StarAmount4,price,String.valueOf(productStarList.get(3).getStarAmount()));
                                }
                                else if(productID.equals(productStarList.get(4).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice5,SS_StarAmount5,price,String.valueOf(productStarList.get(4).getStarAmount()));
                                }
                                else if(productID.equals(productStarList.get(5).getProductID()))
                                {
                                    setProductInformation(SS_StarPrice6,SS_StarAmount6,price,String.valueOf(productStarList.get(5).getStarAmount()));
                                }
                            }

                            enable();

                        }
                        else
                        {
                            Log.d("BILLINGTAG",billingResult.getDebugMessage());
                        }


                    }
                });
            }

        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("BILLINGTAG",e.getMessage());
            }
        });



    }




}
