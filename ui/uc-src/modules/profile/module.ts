import { IconAccountCircleLine } from "@nebula-labs/components";
import { definePlugin } from "@nebula-labs/ui-shared";
import BasicLayout from "@uc/layouts/BasicLayout.vue";
import { markRaw } from "vue";

export default definePlugin({
  ucRoutes: [
    {
      path: "/",
      component: BasicLayout,
      name: "Root",
      redirect: "/profile",
      children: [
        {
          path: "profile",
          name: "Profile",
          component: () => import("./Profile.vue"),
          meta: {
            title: "core.uc_profile.title",
            searchable: true,
            menu: {
              name: "core.uc_sidebar.menu.items.profile",
              group: "dashboard",
              icon: markRaw(IconAccountCircleLine),
              priority: 0,
              mobile: true,
            },
          },
        },
      ],
    },
  ],
});
