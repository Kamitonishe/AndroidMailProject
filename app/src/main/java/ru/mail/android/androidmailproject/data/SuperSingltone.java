package ru.mail.android.androidmailproject.data;

/**
 * Created by dmitrykamaldinov on 12/15/17.
 */

public class SuperSingltone {
    private static SuperSingltone instance;
    private String picture;
    private boolean optionsDialogIsCalled;
    private boolean onlyFavorites, compareOnlyToFavorites;

    public SuperSingltone() {
        optionsDialogIsCalled = false;
    }

    public void setPicture(String newPicture) {
        picture = newPicture;
    }

    public String getPicture() {
        return picture;
    }

    public void setOptionsDialogIsCalled(boolean isCalled) {
        optionsDialogIsCalled = isCalled;
    }

    public boolean isOptionsDialogCalled() {
        return optionsDialogIsCalled;
    }

    public static SuperSingltone getInstance() {
        synchronized (SuperSingltone.class) {
            if (instance == null) {
                instance = new SuperSingltone();
            }
            return instance;
        }
    }

    public void setOnlyFavorites(boolean onlyFavorites) {
        this.onlyFavorites = onlyFavorites;
    }

    public void setCompareOnlyToFavorites(boolean onlyFavorites) {
        this.compareOnlyToFavorites = onlyFavorites;
    }

    public boolean isOnlyFavorites() {
        return onlyFavorites;
    }

    public boolean isCompareOnlyToFavorites() {
        return compareOnlyToFavorites;
    }
}
