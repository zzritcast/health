package com.itheima.pojo;

import java.io.Serializable;

/**
 * @ClassName Address
 * @Author HETAO
 * @Date 2020/5/3 8:39
 */
public class Address implements Serializable {
    private Integer id;
    private String address;
    private String longitude;
    private String dimension;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", address='" + address + '\'' +
                ", longitude='" + longitude + '\'' +
                ", dimension='" + dimension + '\'' +
                '}';
    }
}
