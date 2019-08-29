package jukebox.youtube.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SectionListRenderer {

    @JsonProperty("contents")
    private List<SectionContents> sectionContents;

    public SectionListRenderer() {
    }

    public List<SectionContents> getSectionContents() {
        return sectionContents;
    }

    public void setSectionContents(List<SectionContents> sectionContents) {
        this.sectionContents = sectionContents;
    }
}
