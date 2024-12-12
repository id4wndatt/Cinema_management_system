package com.example.cinema_client.models;

import java.util.Objects;

import lombok.Data;


@Data
public class Role {
    private Integer id;
    private String name;
	@Override
	public boolean equals(Object obj) {

		return Objects.equals(this.name, obj.toString());
	}
	@Override
	public int hashCode() {
		return Objects.hash(this.name);
	}
    
}
