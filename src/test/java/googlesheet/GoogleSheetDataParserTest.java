package googlesheet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static googlesheet.GoogleSheetDataParser.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(value = MockitoJUnitRunner.class)
public class GoogleSheetDataParserTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void parseData_newValidData_googleSheetDataReturned() {
        // given
        String artist = "";
        String song = "Rose Tattoo";
        String title = "";
        String link = "";
        String directory = "";
        String status = "New";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        assertThat(GoogleSheetStatus.NEW, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), nullValue());
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), nullValue());
        assertThat(resultGoogleSheetData.getYoutubeVideoLink(), nullValue());
        assertThat(resultGoogleSheetData.getDirectory(), nullValue());
        assertThat(resultGoogleSheetData.getAllYoutubeVideoLinks(), nullValue());
    }

    @Test
    public void parseData_fullValidData_googleSheetDataReturned() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedLinkList = new ArrayList<>();
        updatedLinkList.add(link);
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), equalTo(artist));
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), equalTo(title));
        assertThat(resultGoogleSheetData.getYoutubeVideoLink(), equalTo(link));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoLinks(), equalTo(updatedLinkList));
    }

    @Test
    public void parseData_fullValidDataMultipleLinks_googleSheetDataReturned() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String linkList = link + "," + link + "," + link;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedLinkList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            updatedLinkList.add(link);
        }
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), equalTo(artist));
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), equalTo(title));
        assertThat(resultGoogleSheetData.getYoutubeVideoLink(), equalTo(link));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoLinks(), equalTo(updatedLinkList));
    }

    @Test
    public void parseData_fullValidData_googleSheetDataReturnedWithNullValues() {
        // given
        String artist = "";
        String song = "Rose Tattoo";
        String title = "";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedLinkList = new ArrayList<>();
        updatedLinkList.add(link);
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), nullValue());
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), nullValue());
        assertThat(resultGoogleSheetData.getYoutubeVideoLink(), equalTo(link));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoLinks(), equalTo(updatedLinkList));
    }

    @Test
    public void parseData_invalidObjectType_GoogleSheetParserException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        Integer directory = 5;
        String status = "Pending";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        exception.expect(GoogleSheetParserException.class);
        exception.expectMessage(GoogleSheetParserException.NOT_OF_TYPE_STRING);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

    @Test
    public void parseData_invalidObjectTypePrimitive_GoogleSheetParserException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        int directory = 5;
        String status = "Pending";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        exception.expect(GoogleSheetParserException.class);
        exception.expectMessage(GoogleSheetParserException.NOT_OF_TYPE_STRING);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

    @Test
    public void parseData_incorrectNumberOfColumns_GoogleSheetParserException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String linkList = "";
        String extraObject = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);
        dataList.add(extraObject);

        exception.expect(GoogleSheetParserException.class);
        exception.expectMessage(GoogleSheetParserException.INCORRECT_COLUMN_SIZE);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

    @Test
    public void parseData_emptySongName_GoogleSheetParserException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        exception.expect(GoogleSheetParserException.class);
        exception.expectMessage(GoogleSheetParserException.SONG_NULL);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

    @Test
    public void parseData_randomStatusName_GoogleSheetStatusException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String link = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Random";
        String linkList = "";
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK, link);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(GOOGLE_SHEET_COLUMN_LINK_LIST, linkList);

        exception.expect(GoogleSheetStatusException.class);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

}
