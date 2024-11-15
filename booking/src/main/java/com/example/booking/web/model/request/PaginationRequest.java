package com.example.booking.web.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    private Integer pageSize;

    private Integer pageNumber;

    public PageRequest pageRequest() {
        return PageRequest.of(pageNumber, pageSize);
    }

}
