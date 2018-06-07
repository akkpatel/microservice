package com.atdms.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Sku.
 */
@Entity
@Table(name = "sku")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sku implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "current_sales")
    private Integer currentSales;

    @Column(name = "previous_sales")
    private Integer previousSales;

    @Column(name = "percent_change")
    private Integer percentChange;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Sku name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Sku quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getCurrentSales() {
        return currentSales;
    }

    public Sku currentSales(Integer currentSales) {
        this.currentSales = currentSales;
        return this;
    }

    public void setCurrentSales(Integer currentSales) {
        this.currentSales = currentSales;
    }

    public Integer getPreviousSales() {
        return previousSales;
    }

    public Sku previousSales(Integer previousSales) {
        this.previousSales = previousSales;
        return this;
    }

    public void setPreviousSales(Integer previousSales) {
        this.previousSales = previousSales;
    }

    public Integer getPercentChange() {
        return percentChange;
    }

    public Sku percentChange(Integer percentChange) {
        this.percentChange = percentChange;
        return this;
    }

    public void setPercentChange(Integer percentChange) {
        this.percentChange = percentChange;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sku sku = (Sku) o;
        if (sku.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sku.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Sku{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", quantity=" + getQuantity() +
            ", currentSales=" + getCurrentSales() +
            ", previousSales=" + getPreviousSales() +
            ", percentChange=" + getPercentChange() +
            "}";
    }
}
