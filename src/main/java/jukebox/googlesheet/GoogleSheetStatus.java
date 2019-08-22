package jukebox.googlesheet;

public enum GoogleSheetStatus {

    NEW("New"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DOWNLOADED("Downloaded");

    String sheetStatusName;

    GoogleSheetStatus(String sheetStatusName) {
        this.sheetStatusName = sheetStatusName;
    }

    public static GoogleSheetStatus getStatusFromName(String searchingSheetStatusName) throws RuntimeException {
        for (GoogleSheetStatus googleSheetStatus : GoogleSheetStatus.values()) {
            if (googleSheetStatus.sheetStatusName.equals(searchingSheetStatusName)) {
                return googleSheetStatus;
            }
        }
        throw new GoogleSheetStatusException("No status found for " + searchingSheetStatusName);
    }
}
