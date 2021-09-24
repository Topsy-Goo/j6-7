package ru.gb.antonov.j67.entities.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AopStatisticDto
{
    private long userServiceDuration = 0L;
    private long productServiceDuration = 0L;
    private long roleServiceDuration = 0L;

    public AopStatisticDto (long user, long product, long role)
    {
        userServiceDuration = user;
        productServiceDuration = product;
        roleServiceDuration = role;
    }
}
