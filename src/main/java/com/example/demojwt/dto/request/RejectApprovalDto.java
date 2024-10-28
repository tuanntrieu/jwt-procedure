package com.example.demojwt.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RejectApprovalDto extends ChangeStatusRequestDto {
    @NotBlank(message = "Reason is required")
    private String rejectReason;
}
