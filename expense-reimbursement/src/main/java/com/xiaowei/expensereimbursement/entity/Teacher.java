package com.xiaowei.expensereimbursement.entity;

import com.xiaowei.core.basic.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Teacher")
@Data
public class Teacher extends BaseEntity {
    private String tname;
    private String code;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="test_many",
            joinColumns={@JoinColumn(name="t_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="b_id",referencedColumnName="id")})
    private Set<Book> books;
}
