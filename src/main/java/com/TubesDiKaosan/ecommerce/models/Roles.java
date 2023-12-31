package com.TubesDiKaosan.ecommerce.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public class Roles {
    @Id
    @GeneratedValue
    private Integer role_id;

    @Column(name = "role_name", length = 100)
    private String role_name;

    @CreationTimestamp
    @Column(name = "createdAt")
    @JsonIgnore private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    @JsonIgnore private LocalDateTime updatedAt;

    public Roles() {
    }

    public Roles(String role_name) {
        this.role_name = role_name;
    }

    public Roles(Integer role_id, String role_name, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}
