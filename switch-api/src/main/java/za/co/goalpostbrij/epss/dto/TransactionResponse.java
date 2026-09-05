package za.co.goalpostbrij.epss.dto;

public record TransactionResponse(
        String status,
        String approvalCode,
        String issuerBank,
        String responseCode,
        String message
) {
}