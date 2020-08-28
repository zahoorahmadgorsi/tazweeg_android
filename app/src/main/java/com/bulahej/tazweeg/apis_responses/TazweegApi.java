package com.bulahej.tazweeg.apis_responses;

import com.bulahej.tazweeg.apis_responses.UserResponse.UserPaymentResponse;
import com.bulahej.tazweeg.apis_responses.UserResponse.UserResponse;
import com.bulahej.tazweeg.apis_responses.list_countries.CountryResponse;
import com.bulahej.tazweeg.apis_responses.list_emirates.EmirateResponse;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Type;
import com.bulahej.tazweeg.apis_responses.signup_login.ConsultantResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/*
 *
 * Post man link:
 *          https://www.getpostman.com/collections/a702c6ba2791a296113a
 *
 * */

public class TazweegApi {
    public static final String DEVICE_TYPE = "Android";
    public static final String BaseUrl = "https://tazweeg.ae/api/api/";
//    public static final String BaseUrl = "http://10.3.141.169/api/api/"; //ahsan local
//    public static final String BaseUrl = "https://tazweeg.ae/testapi/api/";
//    public static final String BaseUrl = "https://tazweeg.ae/uaeapi/api/";  //for 1dh payment

    private static GetServices getServices = null;

    public static GetServices getInstance() {
        if (getServices == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build())
                    .build();
            getServices = retrofit.create(GetServices.class);
        }
        return getServices;
    }

    public interface GetServices {

        @GET("stateM/countryM")
        Call<CountryResponse> getListOfCountries();

        @GET("stateM/{country_id}")
        Call<EmirateResponse> getStatesByCountry(@Path("country_id") int country_id);

        @GET("consultantByStateM/{emirate_id}")
        Call<ConsultantResponse> getListOfConsultants(@Path("emirate_id") int emirateID);

        @GET("type")
        Call<Type> getTypes();

        @FormUrlEncoded
        @POST("signupM/mobileVerification")
        Call<MobileSignUpResponse> mobileVerification(@Field("mobile") String mobile,
                                  @Field("countryId") int countryId);

        @FormUrlEncoded
        @POST("signupM/signUpNew")
        Call<UserResponse> signUp(@Field("fullName") String name,
                                  @Field("email") String email,
                                  @Field("mobile") String phoneNumber,
                                  @Field("typeId") int typeId,
                                  @Field("stateId") String stateId,
                                  @Field("consultantId") int consultantId,
                                  @Field("genderId") int genderId,
                                  @Field("setLang") int setLang);

        @FormUrlEncoded
        @POST("loginM")
        Call<UserResponse> login(@Field("email") String email,
                                 @Field("password") String password
        );

        @FormUrlEncoded
        @POST("forgotM")
        Call<UserResponse> forgotPassword(@Field("forgotType") String forgotType,
                                 @Field("value") String value
        );

        @FormUrlEncoded
        @POST("memberMobile/1")
        Call<UserResponse> stepOne(@Field("userId") int userId,
                                   @Field("email") String email,
                                   @Field("emirateId") String emirateId,
                                   @Field("family") String family,
                                   @Field("isFamilyShow") Boolean isFamilyShow,
                                   @Field("birthDate") String birthDate,
                                   @Field("birthPlace") String birthPlace,
                                   @Field("countryId") int countryId,
                                   @Field("stateId") int stateId,
                                   @Field("address") String address,
                                   @Field("residenceTypeId") int residenceTypeId,
                                   @Field("ethnicityId") int ethnicityId,
                                   @Field("motherNationalityId") int motherNationalityId,
                                   @Field("userUpdateId") int userUpdateId
        );

        @FormUrlEncoded
        @POST("memberMobile/2")
        Call<UserResponse> stepTwo(@Field("userId") int userId,
                                    @Field("isSmokingId") int isSmokingId,
                                    @Field("skinColorId") int skinColorId,
                                    @Field("hairColorId") int hairColorId,
                                    @Field("hairTypeId") int hairTypeId,
                                    @Field("eyeColorId") int eyeColorId,
                                    @Field("heightId") int heightId,
                                    @Field("bodyTypeId") int bodyTypeId,
                                    @Field("memberWeightId") int memberWeightId,
                                    @Field("sectTypeId") int sectTypeId,
                                    @Field("memberLicenseId") int memberLicenseId,
                                    @Field("sBrideArrangmentId") int sBrideArrangmentId,
                                    @Field("isPolygamy") String isPolygamy,
                                    @Field("sCondemnBrideId") int sCondemnBrideId,
                                    @Field("userUpdateId") int userUpdateId
        );

        @FormUrlEncoded
        @POST("memberMobile/3")
        Call<UserResponse> stepThree(@FieldMap Map<String, String> your_variable_name);

        @FormUrlEncoded
        @POST("memberMobile/4")
        Call<UserResponse> stepFour(@FieldMap Map<String, String> your_variable_name);

        @FormUrlEncoded
        @POST("memberMobile/5")
        Call<UserResponse> stepFive(@Field("userId") int userId,
                                   @Field("firstRelative") String firstRelative,
                                   @Field("firstRelativeNumber") String firstRelativeNumber,
                                   @Field("firstRelativeRelationId") int firstRelativeRelationId,
                                   @Field("secondRelative") String secondRelative,
                                    @Field("secondRelativeNumber") String secondRelativeNumber,
                                   @Field("secondRelativeRelationId") int secondRelativeRelationId,
                                   @Field("applicantDescription") String applicantDescription,
                                   @Field("languagesId") String languagesId,
                                   @Field("signature") String signature,
                                   @Field("userUpdateId") int userUpdateId
        );

        @FormUrlEncoded
        @POST("paymentM/{userId}")
        Call<UserPaymentResponse> getPaymentURL(
                @Path("userId") int userId,
                @Field("emailAddress") String emailAddress,
                @Field("paymentType") int paymentType
        );

        @FormUrlEncoded
        @POST("paymentStatusM/")
        Call<UserResponse> getPaymentConfirmation(@Field("refNo") String refNo,
                                    @Field("userId") int userId
        );

        @GET("getData/matchingMembersByMemberIdM/{member_id}")
        Call<UserResponse> getMatchingsByMemberID(@Path("member_id") int memberID);


        @FormUrlEncoded
        @POST("getData/getMemberDetailByIdM//")
        Call<UserResponse> getMemberDetailsByMemberID(@Field("memberId") int refNo,
                                                  @Field("conId") int conId
        );

        @FormUrlEncoded
        @POST("signupM/changePassword")
        Call<UserResponse> changePassword(@Field("id") int id,
                                          @Field("password") String password,
                                          @Field("oldPassword") String oldPassword
        );

        @FormUrlEncoded
        @POST("update/memberPriority")
        Call<UserResponse> changeMatchingsOrder(@Field("userId") int userId,
                                   @Field("memberId") int memberId,
                                   @Field("priorityId") int priorityId,
                                   @Field("memberIdShift") int memberIdShift,
                                   @Field("priorityIdShift") int priorityIdShift,
                                   @Field("userUpdateId") int userUpdateId,
                                   @Field("deviceInfo") String deviceInfo,
                                   @Field("appVersion") String appVersion
        );

        @FormUrlEncoded
        @POST("delete/memberPriority")
        Call<UserResponse> deleteMatching(@Field("userId") int userId,
                                                @Field("memberId") int memberId,
                                                @Field("userUpdateId") int userUpdateId,
                                                @Field("deviceInfo") String deviceInfo,
                                                @Field("appVersion") String appVersion
        );

    }

}
