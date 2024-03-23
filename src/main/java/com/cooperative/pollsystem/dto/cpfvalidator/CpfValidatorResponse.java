package com.cooperative.pollsystem.dto.cpfvalidator;

import com.cooperative.pollsystem.service.cpfvalidator.enums.Status;

public record CpfValidatorResponse(Status status) {
}
