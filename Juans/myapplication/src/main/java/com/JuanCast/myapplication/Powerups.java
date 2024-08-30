package com.JuanCast.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.JuanCast.myapplication.models.ProductIDS;
import com.JuanCast.myapplication.models.ProductPowerUp;
import com.JuanCast.myapplication.models.ServerTime;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Powerups extends AppCompatActivity {

    // Navigation variables
    private ImageView Community;
    private ImageView profile;
    private ImageView Cast;
    private ImageView home;
    private ImageView logo;

    // TextView variables
    private TextView tvSuns;
    private TextView ads;
    private TextView star;

    CardView cardViewAgimat;
    CardView cardViewApolaki;

    TextView PU_AgimatNiJuanPrice;
    TextView PU_ApolakiPrice;
    TextView PU_AgimatNiJuanDuration;
    TextView PU_ApolakiDuration;
    TextView PU_ActivePowerup;
    TextView PU_PowerupTimeout;

    ConstraintLayout PU_LoadingContainer;



    BillingClient billingClient;

    ProductDetails apolakiProductDetail;
    ProductDetails agimatNiJuanProductDetail;

    FirebaseFirestore db;
    FirebaseDatabase firebaseDatabase;

    String currentUserId;

    ArrayList<ProductPowerUp> powerupsProductDetails = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_powerups);

        // Initialize ImageViews
        Community = findViewById(R.id.Community);
        profile = findViewById(R.id.profile);
        Cast = findViewById(R.id.Cast);
        home = findViewById(R.id.home);
        logo = findViewById(R.id.logo); // Make sure to set the correct ID for logo

        // Initialize TextViews
        tvSuns = findViewById(R.id.tvSuns);
        ads = findViewById(R.id.tvAds);
        star = findViewById(R.id.tvStars);

        cardViewAgimat = findViewById(R.id.cardViewAgimat);
        cardViewApolaki = findViewById(R.id.cardViewApolaki);
        PU_AgimatNiJuanPrice = findViewById(R.id.PU_AgimatNiJuanPrice);
        PU_ApolakiPrice = findViewById(R.id.PU_ApolakiPrice);
        PU_AgimatNiJuanDuration = findViewById(R.id.PU_AgimatNiJuanDuration);
        PU_ApolakiDuration = findViewById(R.id.PU_ApolakiDuration);
        PU_LoadingContainer = findViewById(R.id.PU_LoadingContainer);
        PU_ActivePowerup = findViewById(R.id.PU_ActivePowerup);
        PU_PowerupTimeout= findViewById(R.id.PU_PowerupTimeout);


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        db = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        initBilling();


        //Test

        // Set OnClickListeners for ImageViews
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Homepage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                overridePendingTransition(0, 0); // No animation
                startActivity(intent);
                finish();
            }
        });

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, PostActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
            }
        });

        Cast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Voting.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation

            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Homepage.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        // Set OnClickListeners for TextViews
        tvSuns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, Store.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, ads.class); // Ensure the class name is correctly capitalized
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Powerups.this, StarStore.class);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation
                finish();
            }
        });

        cardViewAgimat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PU_ActivePowerup.getText().toString().trim().isEmpty())
                {
                    List<BillingFlowParams.ProductDetailsParams> productDetailsParams = new ArrayList<>();
                    productDetailsParams.add(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(agimatNiJuanProductDetail).build());
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                    billingClient.launchBillingFlow(Powerups.this, billingFlowParams);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You still have an active power up", Toast.LENGTH_LONG).show();
                }

            }
        });

        cardViewApolaki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PU_ActivePowerup.getText().toString().trim().isEmpty())
                {
                    List<BillingFlowParams.ProductDetailsParams> productDetailsParams = new ArrayList<>();
                    productDetailsParams.add(BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(apolakiProductDetail).build());
                    BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder().setProductDetailsParamsList(productDetailsParams).build();
                    billingClient.launchBillingFlow(Powerups.this, billingFlowParams);
                }
                else{
                    Toast.makeText(getApplicationContext(),"You still have an active power up", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    private void disable()
    {
        PU_LoadingContainer.setVisibility(View.VISIBLE);
    }

    private void enable()
    {

        PU_LoadingContainer.setVisibility(View.GONE);

    }



    private void setCurrentPowerupInformation()
    {
        db.collection("User").document(currentUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                new ServerTime().addOnSuccessListener(new ServerTime.ServerTimeSuccessListener<Date>() {
                    @Override
                    public void onSuccess(Date serverTime) {
                        Map<String,Object> data = documentSnapshot.getData();
                        if(data.containsKey("active_powerup"))
                        {
                            if(data.get("active_powerup")!= "")
                            {
                                Date powerupTimeout = ((Timestamp) data.get("powerup_timeout")).toDate();
                                if(!Tools.dateTimeEnd(serverTime,powerupTimeout))
                                {
                                    PU_ActivePowerup.setText(Tools.getPowerupNameFromProductID((String)data.get("active_powerup")));
                                    String minutesEnd = Tools.getMinutesFromDateTimeInterval(serverTime,powerupTimeout) + " minutes";
                                    PU_PowerupTimeout.setText(minutesEnd);
                                }
                            }


                        }

                        enable();
                    }
                });


            }
        });
    }



    private Date getPowerupTimeout(String powerupID, Date purchaseDate)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(purchaseDate);
        for(ProductPowerUp productPowerUp : powerupsProductDetails){
            if(productPowerUp.getProductID().equals(powerupID))
            {
                calendar.add(Calendar.MINUTE, (int)productPowerUp.getDurationMinutes());
            }
        }

        return calendar.getTime();
    }

    private void setProductInformation(String priceText, TextView priceTextView, String durationText,TextView durationTextView)
    {
        priceTextView.setText(priceText);
        durationTextView.setText(durationText);
    }


    private void connectToGPlayBilling()
    {
        disable();

        billingClient.startConnection(
                new BillingClientStateListener() {
                    @Override
                    public void onBillingServiceDisconnected() {
                        connectToGPlayBilling();
                        Log.d("BILLINGTAG","DISCONNECTED");
                    }

                    @Override
                    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                        {
                            getPowerUpsDetails();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),billingResult.getDebugMessage(),Toast.LENGTH_LONG).show();
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
                Log.d("PURCHASETAG1",billingResult.getDebugMessage());
                if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                {
                    Log.d("PURCHASETAG1","OK");
                    updateUserPurchase(purchase.getProducts());
                }
                else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED)
                {
                    Toast.makeText(getApplicationContext(),"You canceled your purchase", Toast.LENGTH_LONG).show();
                }
                else if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.SERVICE_DISCONNECTED)
                {
                    Toast.makeText(getApplicationContext(),"You lost your connection", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.d("PURCHASETAG",billingResult.getDebugMessage());
                }
            }
        };

        billingClient.consumeAsync(consumeParams,listener);
    }

    private String getPriceFromProductID(String productID)
    {
        if(productID.equals(agimatNiJuanProductDetail.getProductId()))
        {
            return agimatNiJuanProductDetail.getOneTimePurchaseOfferDetails().getFormattedPrice();
        }
        else if(productID.equals(apolakiProductDetail.getProductId()))
        {
            return apolakiProductDetail.getOneTimePurchaseOfferDetails().getFormattedPrice();
        }
        return  "";
    }

    private ProductPowerUp getProductPowerupFromID(String powerupID)
    {
        for(ProductPowerUp productPowerUp : powerupsProductDetails)
        {
            if(productPowerUp.getProductID().equals(powerupID))
            {
                return productPowerUp;
            }
        }
        return null;
    }


    private void updateUserPurchase(List<String> productIDs)
    {
        Log.d("BILLINGTAG2", productIDs.get(0));
        db.collection("products").document("powerups").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                new ServerTime().addOnSuccessListener(new ServerTime.ServerTimeSuccessListener<Date>() {
                    @Override
                    public void onSuccess(Date serverTime) {
                        Map<String,Object> powerUps = documentSnapshot.getData();
                        Map<String, Object> apolakiDetails = null;
                        Map<String, Object> agimatNiJuanDetails = null;


                        for(Map.Entry<String,Object> entry : powerUps.entrySet())
                        {
                            if(entry.getKey().trim().equals(ProductIDS.APOLAKI))
                            {
                                apolakiDetails = (Map<String,Object>)entry.getValue();
                            }
                            else if(entry.getKey().trim().equals(ProductIDS.AGIMAT_NI_JUAN))
                            {
                                agimatNiJuanDetails = (Map<String,Object>)entry.getValue();
                            }
                        }


                        String productIds = "";
                        for(String id : productIDs)
                        {
                            if(apolakiDetails.get("product_id").equals(id))
                            {
                                productIds += (productIds.equals("") ? id : ","+id );
                            }
                            else if(agimatNiJuanDetails.get("product_id").equals(id))
                            {
                                productIds += (productIds.equals("") ? id : ","+id );
                            }
                        }


                        // Update transaction history
                        Map<String,Object> transactHistoryAdd = new HashMap<>();
                        transactHistoryAdd.put("reference_number",productIds);
                        transactHistoryAdd.put("timestamp",new Timestamp(serverTime));
                        transactHistoryAdd.put("transaction_type","powerup_purchase");
                        transactHistoryAdd.put("amount_charged",getPriceFromProductID(productIds));
                        transactHistoryAdd.put("user_id",currentUserId);
                        db.collection("transaction_history").add(transactHistoryAdd)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TRANSACTIONTAG",documentReference.getId());
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("TRANSACTIONTAG","Error:" + e.getMessage());
                                        Toast.makeText(getApplicationContext(),"Error:" + e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                        //Update user powerup status
                        Map<String,Object> starUpdate = new HashMap<>();
                        starUpdate.put("active_powerup",productIds);
                        starUpdate.put("powerup_timeout",getPowerupTimeout(productIds,serverTime));
                        db.collection("User")
                                .document(currentUserId)
                                .update(starUpdate)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Purchased successful",Toast.LENGTH_LONG).show();
                                        setCurrentPowerupInformation();

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
        });
    }


    private void getPowerUpsDetails(){
        db.collection("products").document("powerups").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String,Object> powerUps = documentSnapshot.getData();
                Map<String, Object> apolakiDetails = null;
                Map<String, Object> agimatNiJuanDetails = null;

                for(Map.Entry<String,Object> entry : powerUps.entrySet())
                {

                    if(entry.getKey().trim().equals(ProductIDS.APOLAKI))
                    {

                        apolakiDetails = (Map<String,Object>)entry.getValue();


                    }
                    else if(entry.getKey().trim().equals(ProductIDS.AGIMAT_NI_JUAN))
                    {
                        agimatNiJuanDetails = (Map<String,Object>)entry.getValue();
                    }
                }

                List<QueryProductDetailsParams.Product> productList = new ArrayList<>();
                productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId(ProductIDS.APOLAKI).setProductType(BillingClient.ProductType.INAPP).build());
                productList.add(QueryProductDetailsParams.Product.newBuilder().setProductId(ProductIDS.AGIMAT_NI_JUAN).setProductType(BillingClient.ProductType.INAPP).build());

                QueryProductDetailsParams queryProductDetailsParams = QueryProductDetailsParams.newBuilder().setProductList(productList).build();
                Map<String, Object> finalApolakiDetails = apolakiDetails;
                Map<String, Object> finalAgimatNiJuanDetails = agimatNiJuanDetails;
                billingClient.queryProductDetailsAsync(queryProductDetailsParams, new ProductDetailsResponseListener() {
                    @Override
                    public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    for(ProductDetails product : list)
                                    {
                                        Log.d("BILLINGTAG",product.getProductId());
                                        String productID = product.getProductId();
                                        String price = product.getOneTimePurchaseOfferDetails().getFormattedPrice();
                                        if(productID.equals(finalAgimatNiJuanDetails.get("product_id")))
                                        {
                                            powerupsProductDetails.add(
                                                    new ProductPowerUp(
                                                            (String)finalAgimatNiJuanDetails.get("product_id"),
                                                            (Long) finalAgimatNiJuanDetails.get("duration_mins"),
                                                            product));
                                            agimatNiJuanProductDetail = product;
                                            String priceText = "Agimat ni Juan \n"+ price;
                                            String durationText = "Stars voted will be multiplied 2x \n Duration: " + finalAgimatNiJuanDetails.get("duration_mins")+ " mins.";
                                            setProductInformation(priceText,PU_AgimatNiJuanPrice,durationText,PU_AgimatNiJuanDuration);

                                        }
                                        else if(productID.equals(finalApolakiDetails.get("product_id")))
                                        {
                                            powerupsProductDetails.add(
                                                    new ProductPowerUp(
                                                            (String)finalApolakiDetails.get("product_id"),
                                                            (Long) finalApolakiDetails.get("duration_mins"),
                                                            product));
                                            apolakiProductDetail = product;
                                            String priceText = "Apolaki \n"+ price;
                                            String durationText = "Suns voted will be multiplied 2x \nDuration: " + finalApolakiDetails.get("duration_mins")+ " mins.";
                                            setProductInformation(priceText,PU_ApolakiPrice,durationText,PU_ApolakiDuration);
                                        }

                                    }
                                    setCurrentPowerupInformation();

                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),billingResult.getDebugMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                });


            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });






    }
}
