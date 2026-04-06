import type { PluginModule } from "@nebula-labs/ui-shared";

const modules = import.meta.glob("./**/module.ts", {
  eager: true,
  import: "default",
}) as Record<string, PluginModule>;

export default modules;
