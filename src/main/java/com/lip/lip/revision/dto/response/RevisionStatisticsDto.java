package com.lip.lip.revision.dto.response;

public record RevisionStatisticsDto(
                Long total,
                Long completed,
                Long pending,
                Long overdue,
                RevisionByTypeDto byRevision) {

        public record RevisionByTypeDto(
                        Long revision1,
                        Long revision2,
                        Long revision3) {
        }
}