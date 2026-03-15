package com.ecommerce.product.model;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Category {

        MENS_CLOTHING("Men's Clothing"),
        WOMENS_CLOTHING("Women's Clothing"),
        CHILDRENS_CLOTHING("Children's Clothing"),
        BABY_CLOTHING("Baby Clothing"),

        TOPS("Tops", MENS_CLOTHING, WOMENS_CLOTHING),
        T_SHIRTS("T-Shirts", MENS_CLOTHING, WOMENS_CLOTHING),
        DRESSES("Dresses", WOMENS_CLOTHING),
        PANTS("Pants", MENS_CLOTHING, WOMENS_CLOTHING),
        JEANS("Jeans", MENS_CLOTHING, WOMENS_CLOTHING),
        SKIRTS("Skirts", WOMENS_CLOTHING),
        JACKETS("Jackets", MENS_CLOTHING, WOMENS_CLOTHING),
        COATS("Coats", MENS_CLOTHING, WOMENS_CLOTHING),
        SWEATERS("Sweaters", MENS_CLOTHING, WOMENS_CLOTHING),
        HOODIES("Hoodies", MENS_CLOTHING, WOMENS_CLOTHING),

        HATS("Hats", MENS_CLOTHING, WOMENS_CLOTHING, CHILDRENS_CLOTHING),
        GLOVES("Gloves", MENS_CLOTHING, WOMENS_CLOTHING, CHILDRENS_CLOTHING),
        BAGS("Bags", WOMENS_CLOTHING),

        SNEAKERS("Sneakers", MENS_CLOTHING, WOMENS_CLOTHING, CHILDRENS_CLOTHING);

        private final String displayName;
        private final Category[] parents;

        Category(String displayName) {
                this.displayName = displayName;
                this.parents = new Category[0];
        }

        Category(String displayName, Category... parents) {
                this.displayName = displayName;
                this.parents = parents;
        }

        public static Category from(String value) {
                return Arrays.stream(values())
                        .filter(c -> c.name().equalsIgnoreCase(value) || c.displayName.equalsIgnoreCase(value))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid category name: " + value));
        }

}
