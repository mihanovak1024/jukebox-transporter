package jukebox;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocalProperties {

    private String email;
    private String password;
    @JsonProperty("spreadsheet_id")
    private String spreadsheetId;
    @JsonProperty("spreadsheet_name")
    private String spreadsheetName;
    @JsonProperty("spreadsheet_range_format")
    private String spreadsheetRangeFormat;
    @JsonProperty("spreadsheet_range_min_vertical")
    private int spreadsheetRangeMinVertical;
    @JsonProperty("spreadsheet_range_max_horizontal")
    private int spreadsheetRangeMaxHorizontal;

    public LocalProperties() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSpreadsheetId() {
        return spreadsheetId;
    }

    public void setSpreadsheetId(String spreadsheetId) {
        this.spreadsheetId = spreadsheetId;
    }

    public String getSpreadsheetName() {
        return spreadsheetName;
    }

    public void setSpreadsheetName(String spreadsheetName) {
        this.spreadsheetName = spreadsheetName;
    }

    public String getSpreadsheetRangeFormat() {
        return spreadsheetRangeFormat;
    }

    public void setSpreadsheetRangeFormat(String spreadsheetRangeFormat) {
        this.spreadsheetRangeFormat = spreadsheetRangeFormat;
    }

    public int getSpreadsheetRangeMinVertical() {
        return spreadsheetRangeMinVertical;
    }

    public void setSpreadsheetRangeMinVertical(int spreadsheetRangeMinVertical) {
        this.spreadsheetRangeMinVertical = spreadsheetRangeMinVertical;
    }

    public int getSpreadsheetRangeMaxHorizontal() {
        return spreadsheetRangeMaxHorizontal;
    }

    public void setSpreadsheetRangeMaxHorizontal(int spreadsheetRangeMaxHorizontal) {
        this.spreadsheetRangeMaxHorizontal = spreadsheetRangeMaxHorizontal;
    }
}
