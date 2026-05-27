package com.tsh.starter.befw.app.server.apService.cira.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReactionRequest {
	@NotBlank
	private String reactionType;
}
