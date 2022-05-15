package edu.pjatk.app.report;

public enum EntityTypeEnum {
    PROJECT, USER;

    public static EntityTypeEnum stringToEnum(String entityType) {
        return EntityTypeEnum.PROJECT.name().equals(entityType) ? PROJECT : USER;
    }

}
