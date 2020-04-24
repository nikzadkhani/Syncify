package com.syncify.app.domain;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

public class SongRequest implements Serializable {
    private UUID syncifyId;

    private String name;

    public SongRequest(){};

    public SongRequest(UUID syncifyId, String name) {
        this.syncifyId = syncifyId;
        this.name = name;
    }

    @JsonGetter("syncifyId")
    public UUID getSyncifyId() {
        return syncifyId;
    }

    public void setSyncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
    }

    @JsonGetter("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SongRequest{" +
            "syncifyId=" + syncifyId +
            ", name='" + name + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SongRequest that = (SongRequest) o;

        if (syncifyId != null ? !syncifyId.equals(that.syncifyId) : that.syncifyId != null) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = syncifyId != null ? syncifyId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public SongRequest syncifyId(UUID syncifyId) {
        this.syncifyId = syncifyId;
        return this;
    }


    public SongRequest name(String name) {
        this.name = name;
        return this;
    }
}
