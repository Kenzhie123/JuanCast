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

import com.JuanCast.myapplication.Receiver.StarChangeReceiver;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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

    TextView SS_Overlay;
    ProgressBar SS_ProgressBar;

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

        billingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
            @Override
            public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

            }
        }).enablePendingPurchases().build();
        connectToGPlayBilling();

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

    private void enable()
    {
        SS_Overlay.setVisibility(View.GONE);
        SS_ProgressBar.setVisibility(View.GONE);
    }

    private void disable()
    {
        SS_Overlay.setVisibility(View.VISIBLE);
        SS_ProgressBar.setVisibility(View.VISIBLE);
    }

    private void connectToGPlayBilling()
    {
        Log.d("BILLINGTAG","STARTED");
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

    private void getStarsProductsDetails()
    {
        ArrayList<String> productIDList = new ArrayList<>();
        productIDList.add("star_1000");
        productIDList.add("star_5000");
        productIDList.add("star_15000");
        productIDList.add("star_40000");
        productIDList.add("star_100000");
        productIDList.add("star_500000");

        List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_1000").setProductType(BillingClient.ProductType.INAPP).build());
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_5000").setProductType(BillingClient.ProductType.INAPP).build());
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_15000").setProductType(BillingClient.ProductType.INAPP).build());
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_40000").setProductType(BillingClient.ProductType.INAPP).build());
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_100000").setProductType(BillingClient.ProductType.INAPP).build());
        productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId("star_500000").setProductType(BillingClient.ProductType.INAPP).build());

        QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(productList).build();

        billingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {


                for(ProductDetails productDetails : list)
                {

                    if(productDetails.getProductId().equals(productIDList.get(0)))
                    {
                        Log.d("BILLINGTAG",productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_StarPrice1.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }
                    else if(productDetails.getProductId().equals(productIDList.get(1)))
                    {
                        SS_StarPrice2.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }
                    else if(productDetails.getProductId().equals(productIDList.get(2)))
                    {
                        SS_StarPrice3.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }
                    else if(productDetails.getProductId().equals(productIDList.get(3)))
                    {
                        SS_StarPrice4.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }
                    else if(productDetails.getProductId().equals(productIDList.get(4)))
                    {
                        SS_StarPrice5.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem5.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }
                    else if(productDetails.getProductId().equals(productIDList.get(5)))
                    {
                        SS_StarPrice6.setText(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice());
                        SS_MarketItem6.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImmutableList<BillingFlowParams.ProductDetailsParams> productDetailsParams =
                                        ImmutableList.of(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(productDetails).build());

                                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                                billingClient.launchBillingFlow(StarStore.this, billingFlowParams);
                            }
                        });
                    }

                }
                enable();
            }
        });

    }


    /*{
                        Log.d("BILLINGTAG",skuDetails.getPrice());
                        if(skuDetails.getSku().equals(productIDs.get(0)))
                        {

                            SS_StarPrice1.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                        else if(skuDetails.getSku().equals(productIDs.get(1)))
                        {
                            SS_StarPrice2.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                        else if(skuDetails.getSku().equals(productIDs.get(2)))
                        {
                            SS_StarPrice3.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem3.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                        else if(skuDetails.getSku().equals(productIDs.get(3)))
                        {
                            SS_StarPrice4.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                        else if(skuDetails.getSku().equals(productIDs.get(4)))
                        {
                            SS_StarPrice5.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem5.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                        else if(skuDetails.getSku().equals(productIDs.get(5)))
                        {
                            SS_StarPrice6.setText(skuDetails.getOriginalPrice());
                            SS_MarketItem6.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    billingClient.launchBillingFlow(StarStore.this, BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build());
                                }
                            });
                        }
                    }*/


}
