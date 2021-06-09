package com.ansar.jeticketprinter.model.database.api;

import java.sql.SQLException;

public interface IWritable {
    @Deprecated
    public Integer update(Integer updateType, String newValue, String id) throws SQLException;

}
