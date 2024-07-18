package com.example.entity.vo.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
@AllArgsConstructor
public class StaffSaveVO {
    Integer id;
    @Length(min = 2,max = 4)
    String name;
    @Min(1)
    Integer gender;
    @Length(min = 3,max = 8)
    String department;
}
