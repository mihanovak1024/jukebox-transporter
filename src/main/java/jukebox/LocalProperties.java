package jukebox;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocalProperties {

    private String email;
    private String password;
    @JsonProperty("spreadsheet_id")
    private String spreadsheetId;
    @JsonProperty("spreadsheet_range")
    private String spreadsheetRange;

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

    public String getSpreadsheetRange() {
        return spreadsheetRange;
    }

    public void setSpreadsheetRange(String spreadsheetRange) {
        this.spreadsheetRange = spreadsheetRange;
    }
}
