package com.example.cinema_back_end.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.persistence.*;

@Data
@Table(name = "branch")
@Entity
@NoArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 2000)
    private String imgURL;
    private String name;
    private String diaChi;
    private String phoneNo;
    @OneToMany(mappedBy = "branch",fetch = FetchType.LAZY)
    private List<Schedule> scheduleList;
}
