package googlesheet;

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
}
