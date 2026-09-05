package za.co.goalpostbrij.epss.issuer;

import org.springframework.stereotype.Service;

@Service
public class IssuerSimulatorService {

    public AuthorizationDecision authorize(String cardNumber) {

        return switch (cardNumber) {

            case "4000002222222222" ->
                    new AuthorizationDecision(
                            "DECLINED",
                            "51",
                            "INSUFFICIENT_FUNDS");

            case "4000003333333333" ->
                    new AuthorizationDecision(
                            "DECLINED",
                            "54",
                            "CARD_EXPIRED");

            case "4000004444444444" ->
                    new AuthorizationDecision(
                            "DECLINED",
                            "41",
                            "LOST_OR_STOLEN");

            default ->
                    new AuthorizationDecision(
                            "APPROVED",
                            "00",
                            "APPROVED");
        };
    }
}