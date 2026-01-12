package com.lip.lip.user.dto.response;

import com.lip.lip.user.entity.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDTO {
    private Long id;
    private Integer firstRevisionInterval;
    private Integer secondRevisionInterval;
    private Integer thirdRevisionInterval;

    public UserSettingsDTO(UserSettings settings) {
        this.id = settings.getId();
        this.firstRevisionInterval = settings.getFirstRevisionInterval();
        this.secondRevisionInterval = settings.getSecondRevisionInterval();
        this.thirdRevisionInterval = settings.getThirdRevisionInterval();
    }
}