package org.example.mhcommon.data.mappers;

import org.mapstruct.IterableMapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;

/*
lop anh xa truu tuong
 */
public abstract class BaseMap<Rq, Rp, Po> {

    @Named("toPOJO")
    public abstract Po toPOJO(Rq request);

    /*
    map list Requests to Pojos
     */
    @IterableMapping(qualifiedByName = "toPOJO")
    public abstract List<Po> toPOJOs(List<Rq> requests);

    @Named("toResponse")
    public abstract Rp toResponse(Po pojo);

    @IterableMapping(qualifiedByName = "toResponse")
    public abstract List<Rp> toResponses(List<Po> pojos);

    /*
    chuyen doi localdatetime thanh Long
     */
    protected Long map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
                localDateTime.atZone(ZoneId.systemDefault()).getLong(INSTANT_SECONDS) * 1000;
    }
    /*
    chuyen doi milisecond thanh localdatetime
     */
    protected LocalDateTime map(Long time) {
        return time == null ? null :
                Instant.ofEpochMilli(time).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
