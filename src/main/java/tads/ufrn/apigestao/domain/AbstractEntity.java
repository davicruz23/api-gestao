package tads.ufrn.apigestao.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {

    private LocalDate deletedAt;

    private LocalDate createdAt;

    public void delete(){
        this.deletedAt = LocalDate.now();
    }

    public void create(){
        this.createdAt = LocalDate.now();
    }

}