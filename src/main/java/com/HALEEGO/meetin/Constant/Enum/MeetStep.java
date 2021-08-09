package com.HALEEGO.meetin.Constant.Enum;

public enum MeetStep {
    BEFORE_START,
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH,
    SIXTH,
    SEVENTH,
    EIGHTH,
    NINTH,
    TENTH;
    public MeetStep meetstepnext(){
        switch(this){
            case BEFORE_START: return FIRST;
            case FIRST: return SECOND;
            case SECOND: return THIRD;
            case THIRD: return FOURTH;
            case FOURTH: return FIFTH;
            case FIFTH: return SIXTH;
            case SIXTH: return SEVENTH;
            case SEVENTH: return EIGHTH;
            case EIGHTH: return NINTH;
            case NINTH: return TENTH;
        }
        return FIRST;
    }

}


