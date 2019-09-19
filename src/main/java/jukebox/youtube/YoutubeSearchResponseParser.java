package jukebox.youtube;

import jukebox.Util;
import jukebox.youtube.response.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static jukebox.youtube.YoutubeParserException.*;

public class YoutubeSearchResponseParser {

    private static final String VIDEO_LIST_REGEX = "ytInitialData\"] = (\\{\"responseContext\":.*);\n";
    private static final int CONTENT_JSON_RESPONSE_GROUP = 1;

    private Pattern regexPattern = Pattern.compile(VIDEO_LIST_REGEX);

    YoutubeSearchData createYoutubeSearchDataFromResponse(String searchResultHtml, YoutubeSearchInfo youtubeSearchInfo) throws IOException {
        String requestContextJson = getRequestContextJson(searchResultHtml);

        YoutubeSearchResponse youtubeSearchResponse = Util.readJSONToObject(requestContextJson, YoutubeSearchResponse.class);

        List<VideoRenderer> videoRenderers = getVideoRendererList(youtubeSearchResponse);
        return createYoutubeSearchDataFromVideoRenderer(videoRenderers, youtubeSearchInfo.getPreviousUrls());
    }

    private String getRequestContextJson(String searchResultHtml) {
        Matcher matcher = regexPattern.matcher(searchResultHtml);
        if (matcher.find()) {
            return matcher.group(CONTENT_JSON_RESPONSE_GROUP);
        }
        throw new YoutubeParserException(SEARCH_JSON_RESPONSE_PARSE_ERROR);
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
                                            videoRenderers.addAll(itemSectionVideoRenderers);
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

    private YoutubeSearchData createYoutubeSearchDataFromVideoRenderer(List<VideoRenderer> videoRenderers, List<String> rejectedVideoUrls) throws YoutubeParserException {
        YoutubeUrlUtils youtubeUrlUtils = YoutubeUrlUtils.getInstance();
        List<String> rejectedVideoIds = youtubeUrlUtils.transformVideoUrlListToVideoIdList(rejectedVideoUrls);

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
            String currentVideoUrl = youtubeUrlUtils.createUrlFromVideoId(currentVideoId);
            return new YoutubeSearchData(titleString, currentVideoUrl);
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
