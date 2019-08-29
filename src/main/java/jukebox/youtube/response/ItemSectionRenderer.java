package jukebox.youtube.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ItemSectionRenderer {

    @JsonProperty("contents")
    private List<ItemSectionContents> itemSectionContents;

    public ItemSectionRenderer() {
    }

    public List<ItemSectionContents> getItemSectionContents() {
        return itemSectionContents;
    }

    public void setItemSectionContents(List<ItemSectionContents> itemSectionContents) {
        this.itemSectionContents = itemSectionContents;
    }
}
