package ru.zelenbiruz.zmonl.Model;

import android.text.TextUtils;

public class ConfigCardsCredit {
    public String browserType;
    public String description;
    public String cash;
    public String mastercard;
    public String mir;
    public String qiwi;
    public String visa;
    public String yandex;
    public String id;
    public String isActive;
    public String itemId;
    public String name;
    public String order;
    public String orderButtonText;
    public String percent;
    public String percentPostfix;
    public String percentPrefix;
    public int position;
    public String score;
    public String screen;
    public String summMax;
    public String summMid;
    public String summMin;
    public String summPostfix;
    public String summPrefix;
    public String termMax;
    public String termMid;
    public String termMin;
    public String termPostfix;
    public String termPrefix;

    public int getMastercard() {
        try {
            if (!TextUtils.isEmpty(mastercard)) {
                return Integer.parseInt(mastercard);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getMir() {
        try {
            if (!TextUtils.isEmpty(mir)) {
                return Integer.parseInt(mir);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }
    public int getQiwi() {
        try {
            if (!TextUtils.isEmpty(qiwi)) {
                return Integer.parseInt(qiwi);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }
    public int getSummMax() {
        try {
            if (!TextUtils.isEmpty(summMax)) {
                return Integer.parseInt(summMax);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getSummMid() {
        try {
            if (!TextUtils.isEmpty(summMid)) {
                return Integer.parseInt(summMid);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }
    public int getSummPostfix() {
        try {
            if (!TextUtils.isEmpty(summPostfix)) {
                return Integer.parseInt(summPostfix);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getSummPrefix() {
        try {
            if (!TextUtils.isEmpty(summPrefix)) {
                return Integer.parseInt(summPrefix);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getVisa() {
        try {
            if (!TextUtils.isEmpty(visa)) {
                return Integer.parseInt(visa);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getYandex() {
        try {
            if (!TextUtils.isEmpty(yandex)) {
                return Integer.parseInt(yandex);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getTermMax() {
        try {
            if (!TextUtils.isEmpty(termMax)) {
                return Integer.parseInt(termMax);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getTermMid() {
        try {
            if (!TextUtils.isEmpty(termMid)) {
                return Integer.parseInt(termMid);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getTermMin() {
        try {
            if (!TextUtils.isEmpty(termMin)) {
                return Integer.parseInt(termMin);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getTermPostfix() {
        try {
            if (!TextUtils.isEmpty(termPostfix)) {
                return Integer.parseInt(termPostfix);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public int getTermPrefix() {
        try {
            if (!TextUtils.isEmpty(termPrefix)) {
                return Integer.parseInt(termPrefix);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }
    public int getCash() {
        try {
            if (!TextUtils.isEmpty(cash)) {
                return Integer.parseInt(cash);
            }

        } catch (NumberFormatException e) {
        }
        return 0;

    }
    public float getScore() {
        try {
            if (!TextUtils.isEmpty(score)) {
                return Float.parseFloat(score);
            }

        } catch (NumberFormatException e) {
        }
        return 0;
    }
}
