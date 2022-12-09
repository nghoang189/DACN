package com.edu.hutech.major.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Category category;

    @ColumnDefault("0")
    private double price;

    @ColumnDefault("0")
    private double weight;

    private String description;

    private String imageName;

    @ColumnDefault("1")
    private int status;

}//create table mapping trong db
