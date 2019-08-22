package jukebox;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class UtilTest extends BaseTest {

    @Test
    public void readJSONToObject_emptyJson_nullValues() throws IOException {
        // given
        String jsonString = "{}";

        // when
        LocalProperties localPropertiesObject = Util.readJSONToObject(jsonString, LocalProperties.class);

        // then
        assertThat(localPropertiesObject.getEmail(), nullValue());
        assertThat(localPropertiesObject.getPassword(), nullValue());
        assertThat(localPropertiesObject.getSpreadsheetId(), nullValue());
    }

    @Test
    public void readJSONToObject_validJson_validValues() throws IOException {
        // given
        String jsonString = "{\"email\":\"lele@gmail.com\",\"password\":\"lalala\",\"spreadsheet_id\":\"123\",\"spreadsheet_range\":\"range\"}";

        // when
        LocalProperties localPropertiesObject = Util.readJSONToObject(jsonString, LocalProperties.class);

        // then
        assertThat(localPropertiesObject.getEmail(), equalTo("lele@gmail.com"));
        assertThat(localPropertiesObject.getPassword(), equalTo("lalala"));
        assertThat(localPropertiesObject.getSpreadsheetId(), equalTo("123"));
        assertThat(localPropertiesObject.getSpreadsheetRange(), equalTo("range"));
    }

    @Test
    public void readJSONToObject_extraJsonValue_validValuesNoException() throws IOException {
        // given
        String jsonString = "{\"email\":\"lele@gmail.com\",\"password\":\"lalala\",\"spreadsheet_id\":\"123\",\"spreadsheet_range\":\"range\",\"extraValue\":\"extra\"}";

        // when
        LocalProperties localPropertiesObject = Util.readJSONToObject(jsonString, LocalProperties.class);

        // then
        assertThat(localPropertiesObject.getEmail(), equalTo("lele@gmail.com"));
        assertThat(localPropertiesObject.getPassword(), equalTo("lalala"));
        assertThat(localPropertiesObject.getSpreadsheetId(), equalTo("123"));
        assertThat(localPropertiesObject.getSpreadsheetRange(), equalTo("range"));
    }

    @Test
    public void readJSONToObject_missingJsonValue_validValuesNoException() throws IOException {
        // given
        String jsonString = "{\"email\":\"lele@gmail.com\",\"password\":\"lalala\",\"spreadsheet_id\":\"123\"}";

        // when
        LocalProperties localPropertiesObject = Util.readJSONToObject(jsonString, LocalProperties.class);

        // then
        assertThat(localPropertiesObject.getEmail(), equalTo("lele@gmail.com"));
        assertThat(localPropertiesObject.getPassword(), equalTo("lalala"));
        assertThat(localPropertiesObject.getSpreadsheetId(), equalTo("123"));
        assertThat(localPropertiesObject.getSpreadsheetRange(), nullValue());
    }

}
