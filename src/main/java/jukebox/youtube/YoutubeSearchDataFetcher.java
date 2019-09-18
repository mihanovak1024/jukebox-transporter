package jukebox.youtube;

import jukebox.Util;
import jukebox.network.NetworkDataCallback;
import jukebox.network.NetworkDataFetcher;
import jukebox.youtube.response.*;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jukebox.youtube.YoutubeParserException.*;

public class YoutubeSearchDataFetcher implements NetworkDataFetcher<YoutubeSearchInfo, YoutubeSearchData> {

    private static final String VIDEO_LIST_REGEX = "ytInitialData\"] = (\\{\"responseContext\":.*);\n";
    private static final int CONTENT_JSON_RESPONSE_GROUP = 1;

    private Pattern regexPattern = Pattern.compile(VIDEO_LIST_REGEX);
    private YoutubeService youtubeService;

    public YoutubeSearchDataFetcher() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(YoutubeConstants.YOUTUBE_BASE_URL)
                .build();

        youtubeService = retrofit.create(YoutubeService.class);
    }

    @Override
    public void fetchDataAsync(YoutubeSearchInfo youtubeSearchInfo, NetworkDataCallback<YoutubeSearchData> callback, ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                YoutubeSearchData youtubeSearchData = createRequest(youtubeSearchInfo);
                callback.onDataReceived(youtubeSearchData);
            } catch (Exception e) { // TODO: 2019-08-29 more specific exception
                callback.onFailure("Fetch data async error"); // TODO: 2019-08-29 more specific error
            }
        });
    }

    @Override
    public YoutubeSearchData fetchData(YoutubeSearchInfo youtubeSearchInfo) {
        // TODO: 2019-09-04 change this
        try {
            return createRequest(youtubeSearchInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private YoutubeSearchData createRequest(YoutubeSearchInfo youtubeSearchInfo) throws IOException {
        String query = createArtistSongQuery(youtubeSearchInfo);
        Call<String> searchResultsHtmlCall = youtubeService.getSearchResult(query);

        Response<String> searchResultsHtmlResponse = searchResultsHtmlCall.execute();
        String searchResultHtml = searchResultsHtmlResponse.body();

        String requestContextJson = getRequestContextJson(searchResultHtml);

        YoutubeSearchResponse youtubeSearchResponse = Util.readJSONToObject(requestContextJson, YoutubeSearchResponse.class);

        List<VideoRenderer> videoRenderers = getVideoRendererList(youtubeSearchResponse);
        YoutubeSearchData youtubeSearchData = createYoutubeSearchDataFromVideoRenderer(videoRenderers, youtubeSearchInfo.getPreviousLinks());
        return youtubeSearchData;
    }

    private String getRequestContextJson(String searchResultHtml) {
        Matcher matcher = regexPattern.matcher(searchResultHtml);
        if (matcher.find()) {
            String responseJson = matcher.group(CONTENT_JSON_RESPONSE_GROUP);
            return responseJson;
        }
        throw new YoutubeParserException(SEARCH_JSON_RESPONSE_PARSE_ERROR);
    }

    private String createArtistSongQuery(YoutubeSearchInfo youtubeSearchInfo) {
        String artist = youtubeSearchInfo.getArtist();
        String song = youtubeSearchInfo.getSong();
        if (!Util.isNullOrEmpty(artist)) {
            if (!Util.isNullOrEmpty(song)) {
                return artist + "+" + song;
            } else {
                return artist;
            }
        } else if (!Util.isNullOrEmpty(song)) {
            return song;
        }
        throw new IllegalStateException("Both artist and song are null!");
    }

    private List<VideoRenderer> getVideoRendererList(YoutubeSearchResponse youtubeSearchResponse) {
        RootContents rootContents = youtubeSearchResponse.getRootContents();
        if (rootContents != null) {
            TwoColumnSearchResultsRenderer twoColumnSearchResultsRenderer = rootContents.getTwoColumnSearchResultsRenderer();
            if (twoColumnSearchResultsRenderer != null) {
                PrimaryContents primaryContents = twoColumnSearchResultsRenderer.getPrimaryContents();
                if (primaryContents != null) {
                    SectionListRenderer sectionListRenderer = primaryContents.getSectionListRenderer();
                    if (sectionListRenderer != null) {
                        List<SectionContents> sectionContentList = sectionListRenderer.getSectionContents();
                        if (!Util.isNullOrEmpty(sectionContentList)) {
                            ItemSectionRenderer itemSectionRenderer = sectionContentList.get(0).getItemSectionRenderer();
                            if (itemSectionRenderer != null) {
                                List<ItemSectionContents> itemSectionContentList = itemSectionRenderer.getItemSectionContents();
                                List<VideoRenderer> videoRenderers = new ArrayList<>();
                                if (!Util.isNullOrEmpty(itemSectionContentList)) {
                                    for (ItemSectionContents itemSectionContents : itemSectionContentList) {
                                        List<VideoRenderer> itemSectionVideoRenderers = itemSectionContents.getVideoRenderer();
                                        if (!Util.isNullOrEmpty(itemSectionVideoRenderers)) {
                                            for (VideoRenderer itemSectionVideoRenderer : itemSectionVideoRenderers) {
                                                videoRenderers.add(itemSectionVideoRenderer);
                                            }
                                        }
                                    }
                                    return videoRenderers;
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new YoutubeParserException(SEARCH_DATA_PARSE_ERROR);
    }

    private YoutubeSearchData createYoutubeSearchDataFromVideoRenderer(List<VideoRenderer> videoRenderers, List<String> rejectedVideoLinks) throws YoutubeParserException {
        YoutubeLinkUtils youtubeLinkUtils = YoutubeLinkUtils.getInstance();
        List<String> rejectedVideoIds = youtubeLinkUtils.transformVideoLinkListToVideoIdList(rejectedVideoLinks);

        for (VideoRenderer videoRenderer : videoRenderers) {
            String currentVideoId = videoRenderer.getVideoId();
            if (Util.isNullOrEmpty(currentVideoId)) {
                System.out.println("Video id is null/empty. Skipping this one."); // TODO: 2019-09-18 replace System.out.println with SLF4J
                continue;
            }

            if (isVideoIdRejected(rejectedVideoIds, currentVideoId)) {
                System.out.println("Video id was already rejected. Skipping this one.");
                continue;
            }

            String titleString = getVideoTitle(videoRenderer);
            if (Util.isNullOrEmpty(titleString)) {
                System.out.println("Video title is null/empty. Skipping this one.");
                continue;
            }
            String currentVideoLink = youtubeLinkUtils.createLinkFromVideoId(currentVideoId);
            return new YoutubeSearchData(titleString, currentVideoLink);
        }
        throw new YoutubeParserException(SEARCH_DATA_VIDEO_RENDERER_PARSE_ERROR);
    }

    private boolean isVideoIdRejected(List<String> rejectedVideoIds, String videoId) {
        for (String rejectedVideoId : rejectedVideoIds) {
            if (videoId.equals(rejectedVideoId)) {
                return true;
            }
        }
        return false;
    }

    private String getVideoTitle(VideoRenderer videoRenderer) {
        Title title = videoRenderer.getTitle();
        String titleString = null;
        if (title != null) {
            List<Run> runs = title.getRuns();
            if (!Util.isNullOrEmpty(runs)) {
                for (Run run : runs) {
                    titleString = run.getText();
                    if (!Util.isNullOrEmpty(titleString)) {
                        break;
                    }
                }
            }
        }
        return titleString;
    }
}
