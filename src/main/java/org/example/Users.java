package org.example;

public class Users {
    // generate user id using UUID
    // generate other field values cost, sales, as numbers and cost< sales, appInvitesSentOut, acceptedInvites, ReviewOnAppStore, ReviewOnGoogle(0-9), averageReview(0<x<5), shareWine, shareWineList
    // addNewCellerFirst24Hours, logged5FirstWeek, scanned10FirstWeek, ratedFirstWeek, weeklyAcitve, monthlyActive, drinkFirstMonth, 1DayRetention, 1WeekRetention, 1MonthRetention, winBack, inactive, as 1 or 0,
    // ARPU
    // ER USER, leadToVivino, leadToVintec, leadToElectrolux, leadToAEG, registeredForWarrantyVintec, registeredForWarrantyAEG, registeredForWarrantyElux,becomeVinClub, isVinte
    String userId;
    int cost;
    int sales;
    int appInvitesSentOutCount;
    int acceptedInvites;
    int reviewOnAppStore;
    int reviewOnGoogle;
    int averageReview ;
    int isShareWine;
    int isShareWineList;
    int ARPU;
    boolean addNewCellerFirst24Hours;
    boolean logged5FirstWeek;
    boolean scanned10FirstWeek;
    boolean ratedFirstWeek;
    boolean weeklyAcitve;
    boolean monthlyActive;
    boolean drinkFirstMonth;
    boolean firstDayRetention;
    boolean firstWeekRetention;
    boolean firstMonthRetention;
    boolean winBack;
    boolean inactive;
    boolean leadToVivino;
    boolean leadToVintec;
    boolean leadToElectrolux;
    boolean leadToAEG;
    boolean registeredForWarrantyVintec;
    boolean registeredForWarrantyAEG;
    boolean registeredForWarrantyElux;
    boolean becomeVinClub;
    boolean isVinte;

    public Users(String userId, int cost, int sales, int appInvitesSentOutCount, int acceptedInvites, int reviewOnAppStore, int reviewOnGoogle, int averageReview, int isShareWine, int isShareWineList, int ARPU, boolean addNewCellerFirst24Hours, boolean logged5FirstWeek, boolean scanned10FirstWeek, boolean ratedFirstWeek, boolean weeklyAcitve, boolean monthlyActive, boolean drinkFirstMonth, boolean firstDayRetention, boolean firstWeekRetention, boolean firstMonthRetention, boolean winBack, boolean inactive, boolean leadToVivino, boolean leadToVintec, boolean leadToElectrolux, boolean leadToAEG, boolean registeredForWarrantyVintec, boolean registeredForWarrantyAEG, boolean registeredForWarrantyElux, boolean becomeVinClub, boolean isVinte) {
        this.userId = userId;
        this.cost = cost;
        this.sales = sales;
        this.appInvitesSentOutCount = appInvitesSentOutCount;
        this.acceptedInvites = acceptedInvites;
        this.reviewOnAppStore = reviewOnAppStore;
        this.reviewOnGoogle = reviewOnGoogle;
        this.averageReview = averageReview;
        this.isShareWine = isShareWine;
        this.isShareWineList = isShareWineList;
        this.ARPU = ARPU;
        this.addNewCellerFirst24Hours = addNewCellerFirst24Hours;
        this.logged5FirstWeek = logged5FirstWeek;
        this.scanned10FirstWeek = scanned10FirstWeek;
        this.ratedFirstWeek = ratedFirstWeek;
        this.weeklyAcitve = weeklyAcitve;
        this.monthlyActive = monthlyActive;
        this.drinkFirstMonth = drinkFirstMonth;
        this.firstDayRetention = firstDayRetention;
        this.firstWeekRetention = firstWeekRetention;
        this.firstMonthRetention = firstMonthRetention;
        this.winBack = winBack;
        this.inactive = inactive;
        this.leadToVivino = leadToVivino;
        this.leadToVintec = leadToVintec;
        this.leadToElectrolux = leadToElectrolux;
        this.leadToAEG = leadToAEG;
        this.registeredForWarrantyVintec = registeredForWarrantyVintec;
        this.registeredForWarrantyAEG = registeredForWarrantyAEG;
        this.registeredForWarrantyElux = registeredForWarrantyElux;
        this.becomeVinClub = becomeVinClub;
        this.isVinte = isVinte;
    }
}
