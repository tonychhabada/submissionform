package com.submissionform.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Common {
    public static Common sharedInsance = new Common();
    public static NumberFormat formatter = formatter = NumberFormat.getInstance();

    public static NumberFormat getFormatter() {
        formatter.setMinimumIntegerDigits(1);
        formatter.setMaximumFractionDigits(8);
        formatter.setGroupingUsed(true);
        return formatter;
    }

    public void showDialog(Context context, String message) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }


    public void showDialogTitle(Context context, String message,String title) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(message);
        builder1.setTitle(title);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void saveStringPreference(String data, Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(key, data);
        editor.commit();

    }

    public void saveBooleanPreference(Boolean data, Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putBoolean(key, data);
        editor.commit();

    }

    public String getListPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPrefs.getString(key, "");
        return value;
    }


    public Boolean getBooleanPreference(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Boolean value = sharedPrefs.getBoolean(key, false);
        return value;
    }
    public String parseDateWithoutLocal(String date, String format, String informat) {

        SimpleDateFormat month_date = new SimpleDateFormat(informat);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date1 = sdf.parse(date);
//            addMonths(date1,1);
            return month_date.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


    }
    public String parseDateOption(String date, String format, String informat) {

        SimpleDateFormat month_date = new SimpleDateFormat(informat, Locale.ENGLISH);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date1 = sdf.parse(date);
//            addMonths(date1,1);
            return month_date.format(date1);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }


    }
    public void addMonths(Date date, int numMonths){
        date.setMonth((date.getMonth() - 1 + numMonths) % 12 + 1);
    }
    public String parseDate(String date, String format, String informat) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        android.text.format.DateFormat df = new android.text.format.DateFormat();

        DateFormat myFormat = new SimpleDateFormat(format);
        myFormat.setTimeZone(tz);
        try {

            return df.format(informat, myFormat.parse(date)).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    public String parseDateOrder(String date, String format, String informat) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat destFormat=new SimpleDateFormat(informat);

        DateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(tz);
        try {
            Date myDate = df.parse(date);
            return destFormat.format(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String formatNumber(Double numbertoformat) {
        DecimalFormat f = new DecimalFormat("##.00");
//        return f.format(numbertoformat);
        return String.format("%.2f", numbertoformat);
    }
    public String formatNumberSingle(Double numbertoformat) {
        DecimalFormat f = new DecimalFormat("##.0");
//        return f.format(numbertoformat);
        return String.format("%.1f", numbertoformat);
    }
    public String formatNumberWithComma(Double numbertoformat) {
        DecimalFormat f = new DecimalFormat("#,###.00");
//        return f.format(numbertoformat);
        return String.format("%,.2f", numbertoformat);
    }

    public String withSuffix(long count) {
        if (count < 1000) return "" + count;
        int exp = (int) (Math.log(count) / Math.log(1000));
        return String.format("%.2f%c",
                count / Math.pow(1000, exp),
                "KMBTPE".charAt(exp - 1));
    }

    public String fiveDayUrl() {
        String fiveday = "";
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -7);
        dt = c.getTime();
        Date endDate = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = fmt.format(dt);
        String endDateString = fmt.format(endDate);
        fiveday = "start=" + startDateString + "&end=" + endDateString;
        return fiveday;
    }
    public String OneMonthUrl() {
        String oneMonth = "";
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -1);
        dt = c.getTime();
        Date endDate = new Date();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String startDateString = fmt.format(dt);
        String endDateString = fmt.format(endDate);
        oneMonth = "start=" + startDateString + "&end=" + endDateString;
        return oneMonth;
    }

    public String twoYearUrl() {
        String twoyearString = "";
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.YEAR, -2);
        dt = c.getTime();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        twoyearString = "start=" + fmt.format(dt);
        return twoyearString;
    }


    public Date oneMonthDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -1);
        dt = c.getTime();
        return dt;
    }

    public Date threeMonthDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -3);
        dt = c.getTime();
        return dt;
    }

    public Date sixMonthDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, -6);
        dt = c.getTime();
        return dt;
    }

    public Date oneYearDate() {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.YEAR, -1);
        dt = c.getTime();
        return dt;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri,Context context) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
