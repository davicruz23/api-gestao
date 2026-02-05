package tads.ufrn.apigestao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    private OffsetDateTime deletedAt;

    private OffsetDateTime createdAt;

    public void delete(){
        this.deletedAt = OffsetDateTime.now();
    }

    public void create(){
        this.createdAt = OffsetDateTime.now();
    }

}