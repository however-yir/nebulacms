import { stores } from "@nebula-labs/ui-shared";
import type { Router } from "vue-router";

export function setupAuthCheckGuard(router: Router) {
  router.beforeEach((_to, _, next) => {
    const currentUserStore = stores.currentUser();

    if (currentUserStore.isAnonymous) {
      window.location.href = `/login?redirect_uri=${encodeURIComponent(
        window.location.href
      )}`;
      return;
    }

    next();
  });
}
