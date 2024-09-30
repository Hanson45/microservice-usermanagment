package com.authentication.UserManagementSystem.auth.mapper;

import com.authentication.UserManagementSystem.auth.model.dto.PhoneDto;
import com.authentication.UserManagementSystem.auth.model.entity.Phone;

import java.util.List;
import java.util.stream.Collectors;

public class PhoneMapper {

    public static List<Phone> toEntity(List<PhoneDto> phones){
        if(phones == null){
            return List.of();
        }

        return phones.stream()
                .map( phone -> Phone.builder()
                        .number(phone.getNumber())
                        .cityCode(phone.getCityCode())
                        .countryCode(phone.getCountryCode())
                        .build())
                .collect(Collectors.toList());

    }

    public static List<PhoneDto> toDTO(List<Phone> phones){
        if(phones == null){
            return List.of();
        }

        return phones.stream()
                .map( phone -> PhoneDto.builder()
                        .number(phone.getNumber())
                        .cityCode(phone.getCityCode())
                        .countryCode(phone.getCountryCode())
                        .build())
                .collect(Collectors.toList());

    }
}
