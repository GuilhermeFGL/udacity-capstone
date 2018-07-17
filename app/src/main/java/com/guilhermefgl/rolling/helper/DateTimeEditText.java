package com.guilhermefgl.rolling.helper;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.guilhermefgl.rolling.R;

public abstract class DateTimeEditText {

   public static TextWatcher mask(final EditText ediTxt) {
       return new TextWatcher() {
           final String mask = ediTxt.getContext().getString(R.string.date_time_mask);
           boolean isUpdating;
           String old = "";

           @Override
           public void afterTextChanged(final Editable s) { }

           @Override
           public void beforeTextChanged(final CharSequence s,
                                         final int start, final int count, final int after) { }

           @Override
           public void onTextChanged(final CharSequence s,
                                     final int start, final int before, final int count) {
               final String str = DateTimeEditText.unmask(s.toString());
               String mascara = "";
               if (isUpdating) {
                   old = str;
                   isUpdating = false;
                   return;
               }
               int i = 0;
               for (final char m : mask.toCharArray()) {
                   if (m != '#' && str.length() > old.length()) {
                       mascara += m;
                       continue;
                   }
                   try {
                       mascara += str.charAt(i);
                   } catch (final Exception e) {
                       break;
                   }
                   i++;
               }
               isUpdating = true;
               ediTxt.setText(mascara);
               ediTxt.setSelection(mascara.length());
           }
       };
   }

   private static String unmask(final String s) {
       return s.replaceAll("[\\s/:]", "");
   }
}
