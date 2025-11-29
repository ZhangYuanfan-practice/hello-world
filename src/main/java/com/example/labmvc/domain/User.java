package com.example.labmvc.domain;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class User {
    private Long id;          // 由后端生成，无需校验

    @NotBlank(message = "姓名不能为空")
    @Size(max = 20, message = "姓名长度不能超过 20 个字符")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 120, message = "年龄不能大于 120")
    private Integer age;
}
