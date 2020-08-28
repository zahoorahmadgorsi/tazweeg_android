package com.bulahej.tazweeg.utilties;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.bulahej.tazweeg.apis_responses.UserResponse.User;
import com.bulahej.tazweeg.apis_responses.registration_form_new.DropDown;
import com.bulahej.tazweeg.apis_responses.registration_form_new.Spinners;
import com.bulahej.tazweeg.constant.Constants;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities extends Application {

    private Context context;
    public static final String STRING_FIELD_OK = "StringOK";
    public static final String STRING_FIELD_ERROR = "StringError";

    public static void myToastMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

//    public static boolean isOnline(@NonNull Activity activity) {
//        ConnectivityManager cm =
//                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//            return true;
//        } else {
//            new android.app.AlertDialog.Builder(activity).setTitle("Alert!!!").setMessage("Internet Not Available")
//                    .setPositiveButton("OK", null).show();
//        }
//        return false;
//    }

    public static String getAppVersion(@NonNull Context context)
    {
        String versionName = "Version: ";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName += packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName + " Build:" + versionCode;
    }

    public static boolean isRTL(Context context) {
        Configuration config = context.getResources().getConfiguration();
        if(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            //in Right To Left layout
            return true;
        }else{
            return false;
        }
    }

    //This method receives the JSON sent by the server and converts it to dropDown custom objects
    public static Spinners JSONResponseToDropDowns(List<Spinners> jsonDropDowns){
        Spinners tempSpinners = new Spinners();
        for (Spinners item : jsonDropDowns) {
            if (item.getAge() != null){
                tempSpinners.setAge(item.getAge());
            }else if (item.getAnnual_Income() != null){
                tempSpinners.setAnnual_Income(item.getAnnual_Income());
            }else if (item.getBody_Type() != null){
                tempSpinners.setBody_Type(item.getBody_Type());
            }else if (item.getBride_Arrangement() != null){
                tempSpinners.setBride_Arrangement(item.getBride_Arrangement());
            }else if (item.getCondemningBride() != null){
                tempSpinners.setCondemningBride(item.getCondemningBride());
            }else if (item.getConsultant_Type() != null){
                tempSpinners.setConsultant_Type(item.getConsultant_Type());
            }else if (item.getCountries() != null){
                tempSpinners.setCountries(item.getCountries());
            }else if (item.getDisease_Name() != null){
                tempSpinners.setDisease_Name(item.getDisease_Name());
            }else if (item.getEducation_Level() != null){
                tempSpinners.setEducation_Level(item.getEducation_Level());
            }else if (item.getEthnicity() != null){
                tempSpinners.setEthnicity(item.getEthnicity());
            }else if (item.getEye_Color() != null){
                tempSpinners.setEye_Color(item.getEye_Color());
            }else if (item.getFinancial_Condition() != null){
                tempSpinners.setFinancial_Condition(item.getFinancial_Condition());
            }else if (item.getGender_Type() != null){
                tempSpinners.setGender_Type(item.getGender_Type());
            }else if (item.getHair_Color() != null){
                tempSpinners.setHair_Color(item.getHair_Color());
            }else if (item.getHair_Type() != null){
                tempSpinners.setHair_Type(item.getHair_Type());
            }else if (item.getHeight() != null){
                tempSpinners.setHeight(item.getHeight());
            }else if (item.getInfertility() != null){
                tempSpinners.setInfertility(item.getInfertility());
            }else if (item.getIs_Smoking() != null){
                tempSpinners.setIs_Smoking(item.getIs_Smoking());
            }
            else if (item.getIs_Working() != null){
                tempSpinners.setIs_Working(item.getIs_Working());
            }else if (item.getJob_Title() != null){
                tempSpinners.setJob_Title(item.getJob_Title());
            }else if (item.getJob_Type() != null){
                tempSpinners.setJob_Type(item.getJob_Type());
            }else if (item.getLanguages() != null){
                tempSpinners.setLanguages(item.getLanguages());
            }else if (item.getHasLicense() != null){
                tempSpinners.setHasLicense(item.getHasLicense());
            }else if (item.getLooking_For() != null){
                tempSpinners.setLooking_For(item.getLooking_For());
            }else if (item.getNoOfChildren() != null){
                tempSpinners.setNoOfChildren(item.getNoOfChildren());
            }else if (item.getPayment_Status() != null){
                tempSpinners.setPayment_Status(item.getPayment_Status());
            }else if (item.getRelation() != null){
                tempSpinners.setRelation(item.getRelation());
            }else if (item.getReligion_Commitment() != null){
                tempSpinners.setReligion_Commitment(item.getReligion_Commitment());
            }else if (item.getResidenceType() != null){
                tempSpinners.setResidenceType(item.getResidenceType());
            }else if (item.getSect_Type() != null){
                tempSpinners.setSect_Type(item.getSect_Type());
            }else if (item.getSkin_Color() != null){
                tempSpinners.setSkin_Color(item.getSkin_Color());
            }else if (item.getSocial_Status() != null){
                tempSpinners.setSocial_Status(item.getSocial_Status());
            }else if (item.getStates() != null){
                tempSpinners.setStates(item.getStates());
            }else if (item.getTitle() != null){
                tempSpinners.setTitle(item.getTitle());
            }else if (item.getUser_Type() != null){
                tempSpinners.setUser_Type(item.getUser_Type());
            }else if (item.getWeight() != null){
                tempSpinners.setWeight(item.getWeight());
            }else if (item.getWillPayToBride() != null){
                tempSpinners.setWillPayToBride(item.getWillPayToBride());
            }else if (item.getWorking() != null){
                tempSpinners.setWorking(item.getWorking());
            }
        }
        return tempSpinners;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] byteFormat = baos.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);   //not saving properly if Base64.DEFAULT,NO_WRAP
        return imgString;
    }

    public static Bitmap getBitmapFromEncoded64ImageString( String encodedImage) {
        byte[] byteFormat = Base64.decode(encodedImage, Base64.DEFAULT);      //0
        Bitmap decodedByte = BitmapFactory.decodeByteArray(byteFormat, 0, byteFormat.length);
        return decodedByte;
//        return byteFormat;
    }

    public static boolean isValidDate(int selectedDate, int selectedMonthIndex, int selectedYear) {
        Calendar calendar = Calendar.getInstance();
        int currentDate, currentMonthIndex, currentYear;

        currentDate = calendar.get(Calendar.DATE);
        currentMonthIndex = calendar.get(Calendar.MONTH);
        currentYear = calendar.get(Calendar.YEAR);

        if (selectedYear == currentYear) {
            if (selectedMonthIndex == currentMonthIndex) {
                if (selectedDate >= currentDate) {
                    return true;
                } else return false;
            } else if (selectedMonthIndex > currentMonthIndex) { //month is grater then previous month.
                return true;
            } else return false; //selected month is previous month

        } else if (selectedYear > currentYear) {
            return true;
        } else return false; // selected year is previous year.

    }

    public static Boolean isValidPhoneNumber(String phoneNumber) {

        //String regexp = "^(?:971|966|968|965|973)(?:2|3|4|6|7|9|50|51|52|54|55|56|58)[0-9]{7}$"; //your regexp here
        String regexp = "^(?:971|966|968|965|973)(?:1|2|3|4|5|6|7|8|9)[0-9]{8}$";   //Count code then 1-9 and then 8 digit phone number

        if (phoneNumber.matches(regexp)) {
            return true;
        }else{
            return false;
        }
    }

    //2 = Admin, 3 = Consultant, 4 = User
    public static int getSelectedUserType(SharedPreferences preferences){
        if(preferences.getInt(Constants.CURRENT_USER,Constants.USER_TYPE_MEMBER) == Constants.USER_TYPE_MEMBER){//Member
            return Constants.USER_TYPE_MEMBER;
        }else if(preferences.getInt(Constants.CURRENT_USER,Constants.USER_TYPE_MEMBER) == Constants.USER_TYPE_CONSULTANT){//consultant
            return Constants.USER_TYPE_CONSULTANT;
        }
        return -1;
    }

    public static User getLoggedInMember(SharedPreferences preferences){
        Gson gson = new Gson();
        String json = preferences.getString(Constants.LOGGED_IN_USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public static int getIndexByID(List<DropDown> list , int ID){
        int index = 0;
        for (DropDown item : list)
        {
            if (item.getValueId() == ID){
                return index;
            }
            index += 1;
        }
        return -1;
    }

    //Using day of year is incorrect since it doesn’t correspond to the same dates in leap years and non leap years. For example, in a leap year April 11 is day 102, in a non leap year it is 101. So the code in the
    // question may give incorrect results near one’s birthday.
    public static int getAge(String dobString , String format){
        if (dobString == null || dobString.length() == 0)
            return 0;
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH) ;
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month, day);  //0 is jan

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age < 0 ? 0 : age ;  //if age is -ve then just return 0
    }

    public static void myLogDebug(String message, Object... arg) {
        String msg = String.format(new Locale("en"), message, arg);
        Log.d("MyLog", msg);
    }

    public static void myLogError(String message, Object... arg) {
        String msg = String.format(new Locale("en"), message, arg);
        Log.e("MyLog", msg);
    }

    public static void myLogWarning(String message, Object... arg) {
        String msg = String.format(new Locale("en"), message, arg);
        Log.w("MyLog", msg);
    }

    /*This method will hide the input keyboard when user will click on button to save data.*/
    public static void hideScreenKeyboard(Activity activity) {
        InputMethodManager manager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
        try {
            manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.myLogError("Exception: %s", e.getMessage());
        }
    }

    /*Share some text in email, sms, whatsapp, facebook etc.*/
    public static void shareThisAppIntent(Activity activity, String title, String textBody) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textBody);
        activity.startActivity(shareIntent);
    }

    public static void shareIntent(Activity activity, String title, String textBody) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textBody);
        activity.startActivity(shareIntent);
    }

    /*Send email using intent*/
    public static void sendEmailIntent(Activity activity, String emailClientTitle, String email, String subject, String msgBody) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { email });
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, msgBody);
        activity.startActivity(Intent.createChooser(intent, emailClientTitle));
    }

    public static void phoneCallIntent(Activity activity, String MobileNumber) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 0);
        } else {
            String PhoneNumber = String.format("tel: %s", MobileNumber);
            Intent CallIntent = new Intent(Intent.ACTION_CALL, Uri.parse(PhoneNumber));
            activity.startActivity(CallIntent);
        }
    }

    public static void openUrlIntent(Activity activity, String url) {

        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("http://" + url));
//            i.setUser(Uri.parse(url));
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getTimeString(int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        String hourStr;
        if (calendar.get(Calendar.HOUR) == 0) {
            hourStr = String.valueOf("12");
        } else {
            hourStr = String.valueOf(calendar.get(Calendar.HOUR));
        }

        String minuteStr;
        if (calendar.get(Calendar.MINUTE) < 10) {
            minuteStr = String.format("0%d", calendar.get(Calendar.MINUTE));
        } else {
            minuteStr = String.valueOf(calendar.get(Calendar.MINUTE));
        }

        String AM_PM_Str;
        if (calendar.get(Calendar.AM_PM) == 0) {
            AM_PM_Str = "AM";
        } else {
            AM_PM_Str = "PM";
        }

        String timeStr = String.format("%s:%s %s",
                hourStr,
                minuteStr,
                AM_PM_Str
        );

        return timeStr;

    }

//    --------------------NEW------------------------------------------------------

    public static void showDialogMessage(Context context, String title, String message, String buttonName) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(buttonName, null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void getImageIntent(Activity activity) {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }


    public static String changeDateFormats(String date , String sourceFormat , String destinationFormat) throws ParseException {
//        @TargetApi(Build.VERSION_CODES.O)
//        @RequiresApi(api = Build.VERSION_CODES.O)
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(sourceFormat);
//        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(destinationFormat);
//        System.out.println(dateTime.format(formatter2));
//        return dateTime.format(formatter2);
        if(date == null || date.length() == 0)
            return "";
        SimpleDateFormat spf = new SimpleDateFormat(sourceFormat, Locale.ENGLISH);
        Date newDate = spf.parse(date);
        spf = new SimpleDateFormat(destinationFormat, Locale.ENGLISH);
        date = spf.format(newDate);
        System.out.println(date);
        return date;
    }

    /** Returns the consumer friendly device name */

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getNetworkClass(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
//            case TelephonyManager.NETWORK_TYPE_NR:
//                return "5G";
            default:
                return "Unknown";
        }
    }
}
