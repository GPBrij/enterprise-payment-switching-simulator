package za.co.goalpostbrij.epss.issuer;

public record AuthorizationDecision(
        String status,
        String responseCode,
        String message
) {
}