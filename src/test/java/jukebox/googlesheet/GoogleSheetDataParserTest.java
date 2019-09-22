package jukebox.googlesheet;

import jukebox.BaseTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static jukebox.googlesheet.GoogleSheetDataParser.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class GoogleSheetDataParserTest extends BaseTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void parseData_newValidData_googleSheetDataReturned() {
        // given
        String artist = "";
        String song = "Rose Tattoo";
        String title = "";
        String url = "";
        String directory = "";
        String status = "New";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        assertThat(GoogleSheetStatus.NEW, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), nullValue());
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), nullValue());
        assertThat(resultGoogleSheetData.getYoutubeVideoUrl(), nullValue());
        assertThat(resultGoogleSheetData.getDirectory(), nullValue());
        assertThat(resultGoogleSheetData.getAllYoutubeVideoUrls(), notNullValue());
        assertThat(resultGoogleSheetData.getIndex(), equalTo(index));
    }

    @Test
    public void parseData_fullValidData_googleSheetDataReturned() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedUrlList = new ArrayList<>();
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), equalTo(artist));
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), equalTo(title));
        assertThat(resultGoogleSheetData.getYoutubeVideoUrl(), equalTo(url));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoUrls(), equalTo(updatedUrlList));
        assertThat(resultGoogleSheetData.getIndex(), equalTo(index));
    }

    @Test
    public void parseData_fullValidDataMultipleUrls_googleSheetDataReturned() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String urlList = url + "," + url + "," + url;
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedUrlList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            updatedUrlList.add(url);
        }
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), equalTo(artist));
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), equalTo(title));
        assertThat(resultGoogleSheetData.getYoutubeVideoUrl(), equalTo(url));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoUrls(), equalTo(updatedUrlList));
        assertThat(resultGoogleSheetData.getIndex(), equalTo(index));
    }

    @Test
    public void parseData_fullValidData_googleSheetDataReturnedWithNullValues() {
        // given
        String artist = "";
        String song = "Rose Tattoo";
        String title = "";
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        GoogleSheetData resultGoogleSheetData = googleSheetDataParser.parseData(dataList);

        // then
        List<String> updatedUrlList = new ArrayList<>();
        assertThat(GoogleSheetStatus.PENDING, equalTo(resultGoogleSheetData.getGoogleSheetStatus()));
        assertThat(resultGoogleSheetData.getArtist(), nullValue());
        assertThat(resultGoogleSheetData.getSong(), equalTo(song));
        assertThat(resultGoogleSheetData.getYoutubeVideoTitle(), nullValue());
        assertThat(resultGoogleSheetData.getYoutubeVideoUrl(), equalTo(url));
        assertThat(resultGoogleSheetData.getDirectory(), equalTo(directory));
        assertThat(resultGoogleSheetData.getAllYoutubeVideoUrls(), equalTo(updatedUrlList));
        assertThat(resultGoogleSheetData.getIndex(), equalTo(index));
    }

    @Test
    public void parseData_invalidObjectType_GoogleSheetParserException() {
        // given
        String artist = "Dropkick Murphys";
        String song = "Rose Tattoo";
        String title = "Dropkick Murphys - \"Rose Tattoo\" (Video)";
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        Integer directory = 5;
        String status = "Pending";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

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
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        int directory = 5;
        String status = "Pending";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

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
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String urlList = "";
        String extraObject = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add(extraObject);
        dataList.add("" + index);

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
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Pending";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

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
        String url = "https://www.youtube.com/watch?v=9d8SzG4FPyM";
        String directory = "Rock";
        String status = "Random";
        String urlList = "";
        int index = 0;
        List dataList = new ArrayList();
        dataList.add(GOOGLE_SHEET_COLUMN_ARTIST, artist);
        dataList.add(GOOGLE_SHEET_COLUMN_SONG, song);
        dataList.add(GOOGLE_SHEET_COLUMN_TITLE, title);
        dataList.add(GOOGLE_SHEET_COLUMN_URL, url);
        dataList.add(GOOGLE_SHEET_COLUMN_DIRECTORY, directory);
        dataList.add(GOOGLE_SHEET_COLUMN_URL_LIST, urlList);
        dataList.add(GOOGLE_SHEET_COLUMN_STATUS, status);
        dataList.add("" + index);

        exception.expect(GoogleSheetStatusException.class);

        // when
        GoogleSheetDataParser googleSheetDataParser = new GoogleSheetDataParser();
        googleSheetDataParser.parseData(dataList);
    }

}
