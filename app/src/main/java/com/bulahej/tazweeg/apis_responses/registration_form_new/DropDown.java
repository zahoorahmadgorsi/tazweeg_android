package com.bulahej.tazweeg.apis_responses.registration_form_new;

import android.content.Context;

import com.bulahej.tazweeg.utilties.Utilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DropDown
{
    public void setContext(Context context) {
        this.context = context;
    }

    // variable to hold context
    private Context context;


    @SerializedName("ValueId")
    @Expose
    private int ValueId;

    @SerializedName("ValueEN")
    @Expose
    private String ValueEN;

    @SerializedName("ValueAR")
    @Expose
    private String ValueAR;

    public int getValueId() {
        return ValueId;
    }

    public void setValueId(int valueId) {
        ValueId = valueId;
    }

    public String getValueEN() {
        return ValueEN;
    }

    public void setValueEN(String valueEN) {
        ValueEN = valueEN;
    }

    public String getValueAR() {
        return ValueAR;
    }

    public void setValueAR(String valueAR) {
        ValueAR = valueAR;
    }

    @Override
    public String toString() {
        if (Utilities.isRTL(context) )
            return this.ValueAR;            // What to display in the Spinner list.
        else
            return this.ValueEN;

    }
}
