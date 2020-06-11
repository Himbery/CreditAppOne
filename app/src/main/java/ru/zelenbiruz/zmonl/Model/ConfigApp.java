package ru.zelenbiruz.zmonl.Model;

import android.text.TextUtils;

public class ConfigApp {

    public String cards_credit_item;
    public String cards_debit_item;
    public String cards_installment_item;
    public String cards_item;
    public String credits_item;
    public String loans_item;
    public String hide_order_offer;


    public int getCards_credit_item() {
        try {
            if (!TextUtils.isEmpty(cards_credit_item)) {
                return Integer.parseInt(cards_credit_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getCards_debit_item() {
        try {
            if (!TextUtils.isEmpty(cards_debit_item)) {
                return Integer.parseInt(cards_debit_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getCards_installment_item() {
        try {
            if (!TextUtils.isEmpty(cards_installment_item)) {
                return Integer.parseInt(cards_credit_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getCards_item() {
        try {
            if (!TextUtils.isEmpty(cards_item)) {
                return Integer.parseInt(cards_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getCredits_item() {
        try {
            if (!TextUtils.isEmpty(credits_item)) {
                return Integer.parseInt(credits_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getHide_order_offer() {
        try {
            if (!TextUtils.isEmpty(hide_order_offer)) {
                return Integer.parseInt(hide_order_offer);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getLoans_item() {
        try {
            if (!TextUtils.isEmpty(loans_item)) {
                return Integer.parseInt(loans_item);
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }
}
