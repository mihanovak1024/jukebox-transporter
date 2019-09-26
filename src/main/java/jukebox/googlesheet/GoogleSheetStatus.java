package jukebox.googlesheet;

import java.util.HashMap;
import java.util.Map;

public enum GoogleSheetStatus {

    NEW("New"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected"),
    DOWNLOADED("Downloaded");

    private String sheetStatusName;
    private static final Map<String, GoogleSheetStatus> googleSheetStatusIndexMap = new HashMap<>();

    static {
        for (GoogleSheetStatus googleSheetStatus : values()) {
            googleSheetStatusIndexMap.put(googleSheetStatus.sheetStatusName, googleSheetStatus);
        }
    }

    GoogleSheetStatus(String sheetStatusName) {
        this.sheetStatusName = sheetStatusName;
    }

    public String getSheetStatusName() {
        return sheetStatusName;
    }

    public static GoogleSheetStatus getStatusFromName(String searchingSheetStatusName) throws RuntimeException {
        GoogleSheetStatus googleSheetStatus = googleSheetStatusIndexMap.get(searchingSheetStatusName);
        if (googleSheetStatus == null) {
            throw new GoogleSheetStatusException("No status found for " + searchingSheetStatusName);
        }
        return googleSheetStatus;
    }
}
