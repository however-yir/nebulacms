import path from "node:path";
import { fileURLToPath, URL } from "node:url";
import VueI18nPlugin from "@intlify/unplugin-vue-i18n/vite";
import Vue from "@vitejs/plugin-vue";
import Icons from "unplugin-icons/vite";
import dts from "vite-plugin-dts";
import { defineConfig } from "vite-plus";

export default ({ mode }: { mode: string }) => {
  const isProduction = mode === "production";

  return defineConfig({
    plugins: [
      Vue(),
      Icons({
        compiler: "vue3",
      }),
      isProduction &&
        dts({
          tsconfigPath: "./tsconfig.app.json",
          entryRoot: "./src",
          outDir: "./dist",
          insertTypesEntry: true,
        }),
      VueI18nPlugin({
        include: [path.resolve(__dirname, "./src/locales/*.json")],
      }),
    ],
    define: {
      "process.env.NODE_ENV": '"production"',
    },
    resolve: {
      alias: {
        "@": fileURLToPath(new URL("./src", import.meta.url)),
      },
    },
    build: {
      outDir: path.resolve(__dirname, "dist"),
      lib: {
        entry: path.resolve(__dirname, "src/index.ts"),
        name: "RichTextEditor",
        formats: ["es", "iife"],
        fileName: (format) => `index.${format}.js`,
        cssFileName: "style",
      },
      minify: isProduction,
      rollupOptions: {
        external: [
          "vue",
          "@nebula-labs/ui-shared",
          "@nebula-labs/api-client",
          "@nebula-labs/components",
        ],
        output: {
          globals: {
            vue: "Vue",
            "@nebula-labs/ui-shared": "HaloUiShared",
            "@nebula-labs/api-client": "HaloApiClient",
            "@nebula-labs/components": "HaloComponents",
          },
          exports: "named",
        },
      },
      sourcemap: false,
    },
  });
};
