package com.example.cinema_client.models;

import lombok.Data;

@Data
public class SeatDTO {
    private int id;
    private String name;
    private boolean isActive;
    private boolean isVip;
    private int isOccupied;
    private boolean isChecked;
}
