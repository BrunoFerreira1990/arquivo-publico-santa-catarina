package com.example.apesc.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EducationLevel {

    ELEMENTARY_SCHOOL("Elementary School"),
    HIGH_SCHOOL("High School"),
    HIGHER_SCHOOL("Higher School"),
    SPECIALIZATION("Specialization School"),
    MASTER_DEGREE("Master Degree"),
    DOCTORAL_DEGREE("Doctoral Degree");

    private String displayName;

}
