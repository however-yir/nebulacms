import BasicLayout from "@console/layouts/BasicLayout.vue";
import { IconServerLine } from "@nebula-labs/components";
import { definePlugin } from "@nebula-labs/ui-shared";
import { markRaw } from "vue";

export default definePlugin({
  components: {},
  routes: [
    {
      path: "/backup",
      name: "BackupRoot",
      component: BasicLayout,
      meta: {
        title: "core.backup.title",
        searchable: true,
        permissions: ["system:migrations:manage"],
        menu: {
          name: "core.sidebar.menu.items.backup",
          group: "system",
          icon: markRaw(IconServerLine),
          priority: 4,
        },
      },
      children: [
        {
          path: "",
          name: "Backup",
          component: () => import("./Backups.vue"),
        },
      ],
    },
  ],
});
