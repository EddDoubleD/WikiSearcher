package com.edddoubled.wikisearcher.model;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class LightweightWikiPage implements Serializable {
    @SerializedName("url")
    String link;

    @SerializedName("content")
    String content;
}

