package com.demo.shop.business.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "ITEM")
public class ItemEO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 64, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @CreatedDate
    @Temporal(TemporalType.DATE)
    @Column(name = "created_on_date", nullable = false)
    private Date createdOnDate;

    @Version
    @Column(name = "version")
    private Integer version;
    
}
