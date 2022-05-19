package com.prasad;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrichmentJob {
    private String fileName;
    private String enrichmentStatus;
}
