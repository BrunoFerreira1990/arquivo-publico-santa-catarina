package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StudyArea {

    POLITICAL_HISTORY("Political History"),
    ECONOMIC_HISTORY("Economic History"),
    SOCIAL_HISTORY("Social History"),
    ADMINSTRATIVE_HISTORY("Administrative History"),
    REGIONAL_HISTORY("Regional History"),
    GENEALOGY("Genealogy"),
    BIOGRAPHICAL_HISTORY("Biographical History"),
    LAND_ACQUISITION("Land Acquisition"),
    OTHER_SUBJECTS("Other Subjects");

    private String displayName;

}
