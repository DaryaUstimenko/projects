package com.example.booking.web.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @NotNull
    @PositiveOrZero
    private Integer pageSize;

    @NotNull
    @PositiveOrZero
    private Integer pageNumber;

    public PageRequest pageRequest() {
        return PageRequest.of(pageNumber, pageSize);
    }

}
