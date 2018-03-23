package com.semanientreprise.steemitcalc.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.semanientreprise.steemitcalc.R;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.DecimalFormat;
import java.util.List;

import Custom.CustomItemClickListener;
import Model.sbdSteem;
import Model.topFiveCoins;
import adapters.topFiveCoinsAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import network.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import service.APISERVICE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.spinner) MaterialSpinner spinner;
    @BindView(R.id.input_amount) EditText inputAmount;
    @BindView(R.id.btn_calculate) Button btnCalculate;
    @BindView(R.id.input_layout_amount) TextInputLayout inputLayoutAmount;
    @BindView(R.id.cryptoCalculation_tv) TextView cryptoCalculationTV;
    @BindView(R.id.topFiveCoins_recView) RecyclerView topFiveCoinsRecView;
    @BindView(R.id.bitcoinPrice) TextView bitcoinPrice;
    @BindView(R.id.sbdPrice) TextView sbdPrice;
    @BindView(R.id.steemPrice) TextView steemPrice;
    @BindView(R.id.customLoader) MKLoader customLoader;
    @BindView(R.id.loaderWrapper) RelativeLayout loaderWraper;
    @BindView(R.id.mainContentWrapper) ScrollView mainContentWrapper;
    private RecyclerView.Adapter topFiveCoinsAdapter;
    private Call<List<topFiveCoins>> getTopFiveCoins;
    private Call<sbdSteem> getSbdSteemPrice;
    private RecyclerView.LayoutManager topFiveCoinsLayoutManager;
    private FragmentManager manager = getSupportFragmentManager();
    private String cryptoSelected = "";
    private DecimalFormat format = new DecimalFormat("#,###.###");
    double number;
    String stringSbdPrice, stringSteemPrice, stringBitcoinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initMethod();

        initSpinner();
    }

    //method to initialize elements of the application
    // and other necessary calls are made here
    private void initMethod() {
        loaderWraper.setVisibility(View.VISIBLE);
        mainContentWrapper.setVisibility(View.GONE);

        btnCalculate.setEnabled(false);
        cryptoCalculationTV.setVisibility(View.GONE);

        topFiveCoinsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        inputLayoutAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    btnCalculate.setEnabled(false);
                    cryptoCalculationTV.setVisibility(View.GONE);
                } else {
                    btnCalculate.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //method to make call to server to get the top five coins
        // and also the price of sbd and steem
        getTopFiveCoins();
        getSteemSbdPrice();

        topFiveCoinsRecView.setLayoutManager(topFiveCoinsLayoutManager);
        topFiveCoinsRecView.setHasFixedSize(true);
    }

    //method to make a call to server to get the current price of sbd and steem
    private void getSteemSbdPrice() {
        APISERVICE service = ApiClient.getClient().create(APISERVICE.class);
        getSbdSteemPrice = service.getSbdSteemPrice(getString(R.string.REQUEST), getString(R.string.GETCRYPTOPRICE), getString(R.string.NGN));

        getSbdSteemPrice.enqueue(new Callback<sbdSteem>() {
            @Override
            public void onResponse(Call<sbdSteem> call, Response<sbdSteem> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final sbdSteem rowListItem = response.body();

                    stringBitcoinPrice = rowListItem.getPrice();
                    stringSbdPrice = rowListItem.getSbd();
                    stringSteemPrice = rowListItem.getSteem();

                    bitcoinPrice.setText(getString(R.string.one_bitcoin_is_equal) + format.format(Double.valueOf(stringBitcoinPrice)) + getString(R.string.ngn));
                    sbdPrice.setText(getString(R.string.one_sbd_is_equal) + format.format(Double.valueOf(stringSbdPrice)) + getString(R.string.ngn));
                    steemPrice.setText(getString(R.string.one_steem_is_equal) + format.format(Double.valueOf(stringSteemPrice)) + getString(R.string.ngn));

                    mainContentWrapper.setVisibility(View.VISIBLE);
                    loaderWraper.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<sbdSteem> call, Throwable t) {
                Log.d("GOTTEN", t.getMessage());
            }
        });
    }

    //method to make a call to the server to get the top five coins
    private void getTopFiveCoins() {
        APISERVICE service = ApiClient.getClient().create(APISERVICE.class);

        getTopFiveCoins = service.getTopFiveCoins(getString(R.string.REQUEST), getString(R.string.GETTOPFIVE));

        getTopFiveCoins.enqueue(new Callback<List<topFiveCoins>>() {
            @Override
            public void onResponse(Call<List<topFiveCoins>> call, Response<List<topFiveCoins>> response) {
                if (response.code() == 200 && response.isSuccessful()) {
                    final List<topFiveCoins> rowListItem = response.body();

                    topFiveCoinsAdapter = new topFiveCoinsAdapter(rowListItem, MainActivity.this, new CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            //call to the method to show the dialog containing the details
                            //of the clicked coin
                            showCoinDialog(rowListItem, position);
                        }
                    });

                    topFiveCoinsRecView.setAdapter(topFiveCoinsAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<topFiveCoins>> call, Throwable t) {
            }
        });
    }

    //method to show the dialog which will entail the details of the coin clicked
    private void showCoinDialog(List<topFiveCoins> rowListItem, int position) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.clicked_coin_layout, null);

        final Dialog dialog = new Dialog(this, R.style.DialogSlideAnim);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.getWindow().setBackgroundDrawableResource(R.color.white);
        dialog.setCancelable(true);

        //group of code to initialize the elements used in the dialog
        final TextView clickedCoinName = dialog.getWindow().findViewById(R.id.clicked_coin_name);
        final TextView clickedName = dialog.getWindow().findViewById(R.id.clicked_name);
        final TextView clickedSymbol = dialog.getWindow().findViewById(R.id.clicked_symbol);
        final TextView clickedPrice = dialog.getWindow().findViewById(R.id.clicked_price);
        final TextView clickedChange1h = dialog.getWindow().findViewById(R.id.clicked_change1h);
        final TextView clickedChange24h = dialog.getWindow().findViewById(R.id.clicked_change24h);
        final TextView clickedChange7d = dialog.getWindow().findViewById(R.id.clicked_change7d);
        final ImageView dropDownArrow = dialog.getWindow().findViewById(R.id.downArrow);
        final ImageView coinImage = dialog.getWindow().findViewById(R.id.clicked_coin_image);

        //loading of the dialog elements from the details gotten from the
        //call to the server
        clickedCoinName.setText(rowListItem.get(position).getName());
        clickedName.setText(rowListItem.get(position).getName());
        clickedSymbol.setText(rowListItem.get(position).getSymbol());
        clickedPrice.setText("$"+rowListItem.get(position).getPrice());
        clickedChange1h.setText(rowListItem.get(position).getChange1hr());
        clickedChange24h.setText(rowListItem.get(position).getChange24h());
        clickedChange7d.setText(rowListItem.get(position).getChange7d());
        Picasso.with(this).
                load(rowListItem.get(position).getImageUrl())
                .centerInside()
                .resize(100,100)
                .into(coinImage);
        dropDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    //method to initialize the spinner
    //used to display the supported coins
    private void initSpinner() {
        spinner.setItems(getString(R.string.sbd), getString(R.string.bitcoin), getString(R.string.steem));
        cryptoSelected = getString(R.string.sbd);

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                cryptoSelected = item;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.btn_calculate)
    public void onViewClicked() {
        checkInputsAndCalculateOutput();
    }

    //method to check the user input and then calculate
    //the price of the entered input and display the result
    private void checkInputsAndCalculateOutput() {

        String amount = inputLayoutAmount.getEditText().getText().toString();

        if (amount.equals("") || Double.valueOf(amount) <= 0) {
            inputLayoutAmount.setError(getString(R.string.err_invalid_amount));
            requestFocus(inputLayoutAmount);
        } else {
            Double amountDouble = Double.valueOf(amount);
            inputLayoutAmount.setErrorEnabled(false);

            switch (cryptoSelected) {
                case "SBD":
                    number = Double.parseDouble(String.valueOf((Double.valueOf(stringSbdPrice) * amountDouble)));
                    cryptoCalculationTV.setText(amount + getString(R.string.sbd_approximately)+" " + format.format(number) + getString(R.string.ngn));
                    break;
                case "STEEM":
                    number = Double.parseDouble(String.valueOf((Double.valueOf(stringSteemPrice) * amountDouble)));
                    cryptoCalculationTV.setText(amount + getString(R.string.steem_approximately)+" "  + format.format(number) + getString(R.string.ngn));
                    break;
                case "Bitcoin":
                    number = Double.parseDouble(String.valueOf((Double.valueOf(stringBitcoinPrice) * amountDouble)));
                    cryptoCalculationTV.setText(amount + getString(R.string.bitcoin_approximately)+" "  + format.format(number) + getString(R.string.ngn));
                    break;
            }
            cryptoCalculationTV.setVisibility(View.VISIBLE);
        }
    }

    //method to make showing of toast easier
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //Method to handle the back pressed button of the android device.
    @Override
    public void onBackPressed() {
        if (manager.getBackStackEntryCount() == 0) {
            exitApp();
        } else {
            super.onBackPressed();
        }
    }

    //fraction of code to display press the back button again to exist the application
    //based on the back button pressed.
    boolean twice = false;

    private void exitApp() {
        if (twice) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        }
        twice = true;
        Toast.makeText(this, "Tap again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                twice = false;
            }
        }, 3000);
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
