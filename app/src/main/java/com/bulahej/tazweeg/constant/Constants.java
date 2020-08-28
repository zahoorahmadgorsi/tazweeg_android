package com.bulahej.tazweeg.constant;

import android.support.annotation.IntDef;

import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Spinners;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Constants {
    public static Spinners spinners =  null;
    public static User loggedInMember = null;
    public static final String apiGetStates = "statesM";
    public static final String apiSignup = "signupM"; //Same for CodeVerification
    public static final String apiCodeVerification = "signupM/1";
    public static final String apiLogin = "login";
    public static final String apiGetTypes = "type";
    public static final String apiGetConsultantsByState = "consultantByStateM";
    public static final String apiStep1 = "memberMobile/1";
    public static final String apiStep2 = "memberMobile/2";
    public static final String apiStep3 = "memberMobile/3";
    public static final String apiStep4 = "memberMobile/4";
    public static final String apiStep5 = "memberMobile/5";
    public static final String apiStep6 = "paymentM/";
    public static final String apiGetMatchings = "getUser/matchingMembersByMemberIdM/";
    public static final String apiUploadProfilePhoto = "update/picMemberByIdM";
    public static final String apiGetMemberDetails = "getUser/getMemberDetailByIdM/";
    public static final String apiChangePassword = "signupM/changePassword";
    public static final String apiGetConsultantMembers = "getUser/membersByConsultantIdM/";
    public static final String apiForgotPassword = "forgotM";
    public static final String apiUpdateProfileStatus = "update/updateStatusM";
    public static final String apiTransferMember = "update/changeMembersConsultant";
    public static final String urlTermsAndConditionsEN = "https://www.tazweeg.ae/termsEN.html";
    public static final String urlTermsAndConditionsAR = "https://www.tazweeg.ae/termsAR.html";
    public static final String urlPrivacyPolicyEN = "https://www.tazweeg.ae/policyEN.html";
    public static final String urlPrivacyPolicyAR = "https://www.tazweeg.ae/policyAR.html";
    public static final String urlFrequentylAskedQuestionsEN = "https://www.tazweeg.ae/faqsEN.html";
    public static final String urlFrequentylAskedQuestionsAR = "https://www.tazweeg.ae/faqsAR.html";
    public static final String urlSnapChat  = "https://www.snapchat.com/add/bulahij";
    public static final String urlPaymentRedirect = "http://www.tazweeg.ae/matching/?ref=";
    public static final String emailToRecepients = "app@tazweeg.com";
    public static final String phone = "+97126225889";  //its loading from strings.xml
    public static final String address = "5205 - Addax Tower, Al Reem Island, Abu Dhabi, UAE. PO Box 42524";    //its loading from strings.xml
    public static final String urlFacebook = "https://www.facebook.com/tazweeguae/";
    public static final String urlTwitter = "https://twitter.com/tazweeg";
    public static final String urlPintrest = "https://www.pinterest.com/tazweeg/";
    public static final String urlInstagram = "https://www.instagram.com/tazweeg/";
    public static final String urlTelegram = "https://t.me/tazweeg";
    public static final String formatOnDevice = "dd-MMM-yyyy" ;
    public static final String formatToServer = "MM/dd/yyyy" ;
    public static final String formatFromServer = "yyyy-MM-dd'T'HH:mm:ss" ;
    //ENUM OF USER TYPE
    public static final int USER_TYPE_ADMIN = 2;
    public static final int USER_TYPE_CONSULTANT = 3;
    public static final int USER_TYPE_MEMBER = 4;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({USER_TYPE_ADMIN, USER_TYPE_CONSULTANT,USER_TYPE_MEMBER})
    public @interface UserType{}
    //ENUM OF Gender
    public static final int GENDER_BOTH = 0;
    public static final int GENDER_MALE = 7;
    public static final int GENDER_FEMALE = 8;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GENDER_BOTH, GENDER_MALE,GENDER_FEMALE})
    public @interface Gender{}
    //ENUM OF SOCIAL STATUS
    public static final int SOCIAL_STATUS_SINGLE = 56;
    public static final int SOCIAL_STATUS_MARRIED = 57;
    public static final int SOCIAL_STATUS_DIVORCED = 58;
    public static final int SOCIAL_STATUS_WIDOW = 59;
    public static final int SOCIAL_STATUS_DOESNT_MATTER = 842;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SOCIAL_STATUS_SINGLE, SOCIAL_STATUS_MARRIED,SOCIAL_STATUS_DIVORCED,SOCIAL_STATUS_WIDOW,SOCIAL_STATUS_DOESNT_MATTER})
    public @interface socialStatusCode{}
    //ENUM OF IsSmoking
    public static final int IS_SMOKING_NO = 12;
    public static final int IS_SMOKING_YES = 13;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IS_SMOKING_NO, IS_SMOKING_YES})
    public @interface IsSmoking{}
    //ENUM OF IsWorking
    public static final int IS_WORKING_WORKING = 60;
    public static final int IS_WORKING_NOT_WORKING = 61;
    public static final int IS_WORKING_DOESNT_MATTER = 62;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({IS_WORKING_WORKING, IS_WORKING_NOT_WORKING,IS_WORKING_DOESNT_MATTER})
    public @interface IsWorking{}
    //ENUM OF PageType
    public static final int PAGE_TYPE_TERMS = 1;
    public static final int PAGE_TYPE_FAQ = 2;
    public static final int PAGE_TYPE_PRIVACY_POLICY  = 3;
    public static final int PAGE_TYPE_CONTACT_US = 4;
    public static final int PAGE_TYPE_PAYMENT = 5;
    public static final int PAGE_TYPE_SIGNUP = 6;
    public static final int PAGE_TYPE_FB = 7;
    public static final int PAGE_TYPE_INSTA = 8;
    public static final int PAGE_TYPE_PINTEREST  = 9;
    public static final int PAGE_TYPE_TELEGRAM = 10;
    public static final int PAGE_TYPE_TWITTER = 11;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PAGE_TYPE_TERMS, PAGE_TYPE_FAQ,PAGE_TYPE_PRIVACY_POLICY,PAGE_TYPE_CONTACT_US, PAGE_TYPE_PAYMENT,PAGE_TYPE_SIGNUP})
    public @interface PageType{}
    //ENUM OF PaymentsType
    public static final int PAYMENT_TYPE_CHOOSING = 10;
    public static final int PAYMENT_TYPE_MATCHING = 1092;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PAYMENT_TYPE_CHOOSING, PAYMENT_TYPE_MATCHING})
    public @interface PaymenType{}
    //ENUM OF InfoType
    public static final int INFO_TYPE_PROFILE = 1;
    public static final int INFO_TYPE_REQUIRED = 2;
    public static final int INFO_TYPE_CHOOSING  = 3;
    public static final int INFO_TYPE_MATCHING = 4;
    public static final int INFO_TYPE_MARRIED = 5;
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({INFO_TYPE_PROFILE, INFO_TYPE_REQUIRED,INFO_TYPE_CHOOSING,INFO_TYPE_MATCHING, INFO_TYPE_MARRIED})
    public @interface InfoType{}
    //Country Code
    public static final int COUNTRY_CODE_UAE = 218;
    public static final int COUNTRY_CODE_KSA = 181;
    public static final int COUNTRY_CODE_OMAN = 157;
    public static final int COUNTRY_CODE_KUWAIT = 110;
    public static final int COUNTRY_CODE_BAHRAIN = 17;
    //Profile Status
    public static final int  PROFILE_STATUS_INCOMPLETE = 837;   //pending
    public static final int  PROFILE_STATUS_NOTPAID = 9;        //Completed
    public static final int  PROFILE_STATUS_PAID = 10;          //Choosing
    public static final int  PROFILE_STATUS_MATCHING = -1 ;     //matching
    public static final int  PROFILE_STATUS_FINISHED = 857;     //married
    public static final int  PROFILE_STATUS_CANCELLED = 11;     //cancelled

    //SHARED PREFERENCES
    public static final String PREF_NAME = "com.tazweeg";
    public static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    public static final String ON_BOARDING_COMPLETE = "onBoardingComplete";
    public static final String CURRENT_USER = "currentUser";
    public static final String LOGGED_IN_USER = "LoggedInUser";
    public static final String CURRENT_MEMBER = "CurrentMember";    //used in 5 steps


}
