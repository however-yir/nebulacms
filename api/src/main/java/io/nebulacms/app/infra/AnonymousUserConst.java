package io.nebulacms.app.infra;

public interface AnonymousUserConst {
    String PRINCIPAL = "anonymousUser";

    String Role = "anonymous";

    static boolean isAnonymousUser(String principal) {
        return PRINCIPAL.equals(principal);
    }
}
