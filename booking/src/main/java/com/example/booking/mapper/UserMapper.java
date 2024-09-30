package com.example.booking.mapper;

import com.example.booking.entity.User;
import com.example.booking.web.model.request.UpsertUserRequest;
import com.example.booking.web.model.response.UserResponse;
import com.example.booking.web.model.response.UserUpdateResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {

    User upsertRequestToUser(UpsertUserRequest request);

    UserResponse userToResponse(User user);

    UserUpdateResponse userUpdateToResponse(User user);
}
