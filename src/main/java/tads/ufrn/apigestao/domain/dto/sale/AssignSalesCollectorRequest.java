package tads.ufrn.apigestao.domain.dto.sale;

import java.util.List;

public record AssignSalesCollectorRequest(
        Long collectorId,
        List<Long> saleIds
) {}
