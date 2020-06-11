package ru.zelenbiruz.zmonl.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataItem {

    @SerializedName("hideInitAgreement")
    public String hideInitAgreement;
    @SerializedName("showDocs")
    public String showDocs;
    @SerializedName("init_license_term")
    public String initLicenseTerm;
    @SerializedName("license_term")
    public String licenseTerm;
    @SerializedName("app_config")
    public ConfigApp appConfig;
    @SerializedName("cards")
    public List<ConfigCard> cards;
    @SerializedName("credits")
    public List<ConfigCredit> credits;
    @SerializedName("loans")
    public List<ConfigLoan> loans;
    @SerializedName("cards_credit")
    public List<ConfigCardsCredit> cardsCredit;
    @SerializedName("cards_debit")
    public List<ConfigCardsDebit> cardsDebit;
    @SerializedName("cards_installment")
    public List<ConfigCardsInstallment> cardsInstallment;

}


