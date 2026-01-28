package tads.ufrn.apigestao.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tads.ufrn.apigestao.domain.Installment;
import tads.ufrn.apigestao.exception.BusinessException;
import tads.ufrn.apigestao.exception.ResourceNotFoundException;
import tads.ufrn.apigestao.repository.InstallmentRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PixService {

    private final InstallmentRepository installmentRepository;

    public String generateBrCodeForInstallment(Long installmentId) {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new BusinessException("Parcela não encontrada: " + installmentId));

        String chavePix = "+5584994611450";
        String nomeBeneficiario = "DAVI SOUZA";
        String cidade = "MACAIBA";
        BigDecimal valor = installment.getAmount();

        return generatePixPayload(chavePix, nomeBeneficiario, cidade, valor);
    }

    private String generatePixPayload(String chavePix, String nomeBeneficiario, String cidade, BigDecimal valor) {
        StringBuilder payload = new StringBuilder();

        payload.append("000201"); // 00 (ID) + 02 (tamanho) + 01 (versão)

        // Point of Initiation Method (01 = estático, 12 = dinâmico)
        // Se quiser igual ao banco, use 12 para dinâmico
        payload.append("010212");

        // Merchant Account Information - PIX
        String gui = "0014br.gov.bcb.pix";
        String key = "01" + formatField(chavePix);
        String merchantAccount = gui + key;
        payload.append("26").append(formatField(merchantAccount));

        // Merchant Category Code
        payload.append("52040000");

        // Transaction Currency
        payload.append("5303986");

        // Transaction Amount
        if (valor.compareTo(BigDecimal.ZERO) > 0) {
            String valorStr = String.format("%.2f", valor).replace(",", ".");
            payload.append("54").append(formatField(valorStr));
        }

        // Country Code
        payload.append("5802BR");

        // Merchant Name
        payload.append("59").append(formatField(nomeBeneficiario));

        // Merchant City
        payload.append("60").append(formatField(cidade));

        // Additional Data Field Template (campo de referência)
        String additionalData = "0503***"; // *** indica campo livre
        payload.append("62").append(formatField(additionalData));

        // CRC16
        String dataWithoutCrc = payload.toString() + "6304";
        String crc = calculateCRC16(dataWithoutCrc);
        payload.append("6304").append(crc);

        return payload.toString();
    }

    private String formatField(String value) {
        return String.format("%02d", value.length()) + value;
    }

    private String calculateCRC16(String data) {
        int crc = 0xFFFF;
        byte[] bytes = data.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
            }
            crc &= 0xFFFF;
        }

        return String.format("%04X", crc).toUpperCase();
    }

    public byte[] generateQrCodeImage(String brCode, int width, int height) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 2);
        hints.put(EncodeHintType.ERROR_CORRECTION, com.google.zxing.qrcode.decoder.ErrorCorrectionLevel.M);

        BitMatrix matrix = new MultiFormatWriter().encode(
                brCode,
                BarcodeFormat.QR_CODE,
                width,
                height,
                hints
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", baos);
        return baos.toByteArray();
    }
}