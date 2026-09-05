package za.co.goalpostbrij.epss.routing;

public record RoutingDecision(
        String binPrefix,
        String issuerBank,
        String routeCode
) {
}
