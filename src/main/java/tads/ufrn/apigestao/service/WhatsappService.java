package tads.ufrn.apigestao.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.domain.PreSaleItem;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class WhatsappService {

    private static final String COMPANY_NAME = "MG Utilidades do Lar";
    private static final String TEMPLATE_NAME = "venda_aprovada";
    private static final String TEMPLATE_LANGUAGE = "pt_BR";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RestClient restClient;

    @Value("${whatsapp.api-url}")
    private String apiUrl;

    @Value("${whatsapp.phone-number-id}")
    private String phoneNumberId;

    @Value("${whatsapp.access-token}")
    private String accessToken;

    public WhatsappService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public void sendSaleApprovedMessage(
            String customerPhone,
            BigDecimal totalValue,
            Integer installmentsQuantity,
            List<PreSaleItem> items,
            List<Installment> generatedInstallments
    ) {
        String url = apiUrl + "/" + phoneNumberId + "/messages";

        String formattedPhone = normalizePhone(customerPhone);
        String formattedTotal = formatMoney(totalValue);
        String installmentsText = formatInstallmentsQuantity(installmentsQuantity);
        String productsText = buildProductsText(items);
        String installmentsDatesText = buildInstallmentsText(generatedInstallments);

        Map<String, Object> body = buildRequestBody(
                formattedPhone,
                formattedTotal,
                installmentsText,
                productsText,
                installmentsDatesText
        );

        String response = restClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        System.out.println("Resposta WhatsApp API: " + response);
    }

    private Map<String, Object> buildRequestBody(
            String phone,
            String total,
            String installments,
            String products,
            String installmentDates
    ) {
        return Map.of(
                "messaging_product", "whatsapp",
                "to", phone,
                "type", "template",
                "template", Map.of(
                        "name", TEMPLATE_NAME,
                        "language", Map.of(
                                "code", TEMPLATE_LANGUAGE
                        ),
                        "components", List.of(
                                Map.of(
                                        "type", "body",
                                        "parameters", List.of(
                                                buildTextParameter(COMPANY_NAME),
                                                buildTextParameter(total),
                                                buildTextParameter(installments),
                                                buildTextParameter(products),
                                                buildTextParameter(installmentDates)
                                        )
                                )
                        )
                )
        );
    }

    private Map<String, String> buildTextParameter(String value) {
        return Map.of(
                "type", "text",
                "text", sanitizeTemplateParam(value)
        );
    }

    private String buildProductsText(List<PreSaleItem> items) {
        if (items == null || items.isEmpty()) {
            return "-";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < items.size(); i++) {
            PreSaleItem item = items.get(i);

            if (i > 0) {
                builder.append(" | ");
            }

            builder.append(item.getProduct().getName())
                    .append(" qtd ")
                    .append(item.getQuantity());
        }

        return sanitizeTemplateParam(builder.toString());
    }

    private String buildInstallmentsText(List<Installment> installments) {
        if (installments == null || installments.isEmpty()) {
            return "Sem parcelas pendentes";
        }

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < installments.size(); i++) {
            Installment installment = installments.get(i);

            if (i > 0) {
                builder.append(" | ");
            }

            builder.append(i + 1)
                    .append("ª parcela: ")
                    .append(installment.getDueDate().format(DATE_FORMATTER))
                    .append(" - ")
                    .append(formatMoney(installment.getAmount()));
        }

        return sanitizeTemplateParam(builder.toString());
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Telefone do cliente não informado.");
        }

        String onlyNumbers = phone.replaceAll("\\D", "");

        if (!onlyNumbers.startsWith("55")) {
            onlyNumbers = "55" + onlyNumbers;
        }

        return onlyNumbers;
    }

    private String formatMoney(BigDecimal value) {
        if (value == null) {
            return formatMoney(BigDecimal.ZERO);
        }

        return NumberFormat
                .getCurrencyInstance(new Locale("pt", "BR"))
                .format(value);
    }

    private String formatInstallmentsQuantity(Integer installmentsQuantity) {
        if (installmentsQuantity == null || installmentsQuantity <= 0) {
            return "à vista";
        }

        return installmentsQuantity + "x";
    }

    private String sanitizeTemplateParam(String text) {
        if (text == null || text.isBlank()) {
            return "-";
        }

        return text
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ")
                .replaceAll(" {2,}", " ")
                .trim();
    }
}