package com.ilimitech.delivery.application.usecase;

import java.util.*;

/**
 * Permisos alineados con {@code AdminPermission} del frontend.
 */
public final class AdminPermissionCatalog {

    private static final List<String> ALL = List.of(
            "VIEW_RESTAURANTS", "CREATE_RESTAURANTS", "EDIT_RESTAURANTS", "DELETE_RESTAURANTS", "MANAGE_RESTAURANT_STATUS",
            "VIEW_DISHES", "CREATE_DISHES", "EDIT_DISHES", "DELETE_DISHES", "MANAGE_DISH_AVAILABILITY",
            "VIEW_ORDERS", "MANAGE_ORDER_STATUS", "CANCEL_ORDERS", "REFUND_ORDERS",
            "VIEW_USERS", "EDIT_USERS", "DEACTIVATE_USERS", "DELETE_USERS",
            "VIEW_CATEGORIES", "CREATE_CATEGORIES", "EDIT_CATEGORIES", "DELETE_CATEGORIES",
            "VIEW_ANALYTICS", "EXPORT_REPORTS",
            "MANAGE_ADMINS", "SYSTEM_SETTINGS"
    );

    private static final List<String> ADMIN_NO_MANAGE_ADMINS = ALL.stream()
            .filter(p -> !p.equals("MANAGE_ADMINS"))
            .toList();

    private static final List<String> MANAGER = List.of(
            "VIEW_RESTAURANTS", "CREATE_RESTAURANTS", "EDIT_RESTAURANTS", "MANAGE_RESTAURANT_STATUS",
            "VIEW_DISHES", "CREATE_DISHES", "EDIT_DISHES", "MANAGE_DISH_AVAILABILITY",
            "VIEW_ORDERS", "MANAGE_ORDER_STATUS", "CANCEL_ORDERS",
            "VIEW_USERS", "EDIT_USERS",
            "VIEW_CATEGORIES", "CREATE_CATEGORIES", "EDIT_CATEGORIES",
            "VIEW_ANALYTICS"
    );

    private static final List<String> MODERATOR = List.of(
            "VIEW_RESTAURANTS", "VIEW_DISHES", "VIEW_ORDERS", "VIEW_USERS", "VIEW_CATEGORIES", "VIEW_ANALYTICS"
    );

    private AdminPermissionCatalog() {
    }

    public static List<String> permissionsForRole(String roleUpper) {
        if (roleUpper == null) {
            return MODERATOR;
        }
        return switch (roleUpper) {
            case "SUPER_ADMIN" -> ALL;
            case "ADMIN" -> ADMIN_NO_MANAGE_ADMINS;
            case "MANAGER" -> MANAGER;
            case "MODERATOR" -> MODERATOR;
            default -> MODERATOR;
        };
    }
}
