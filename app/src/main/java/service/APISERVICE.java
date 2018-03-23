package service;

import java.util.List;

import Model.sbdSteem;
import Model.topFiveCoins;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by GeneralKolo on 2018/03/17.
 */

public interface APISERVICE {

    //Retrofit For getting Top Five coins
    @FormUrlEncoded
    @POST("sbdsteem.php")
    Call<List<topFiveCoins>> getTopFiveCoins(
            @Field("perform") String perform,
            @Field("action") String action
    );

    //Retrofit For getting steem and sbd prices
    @FormUrlEncoded
    @POST("sbdsteem.php")
    Call<sbdSteem> getSbdSteemPrice(
            @Field("perform") String perform,
            @Field("action") String action,
            @Field("currency") String currency
    );
}