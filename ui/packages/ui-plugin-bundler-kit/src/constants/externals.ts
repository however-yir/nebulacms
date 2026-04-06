const GLOBALS = {
  vue: "Vue",
  "vue-router": "VueRouter",
  pinia: "Pinia",
  "@vueuse/core": "VueUse",
  "@vueuse/components": "VueUse",
  "@vueuse/router": "VueUse",
  "@nebula-labs/ui-shared": "HaloUiShared",
  "@nebula-labs/components": "HaloComponents",
  "@nebula-labs/api-client": "HaloApiClient",
  "@nebula-labs/richtext-editor": "RichTextEditor",
  axios: "axios",
};

const EXTERNALS = Object.keys(GLOBALS) as string[];

export { EXTERNALS, GLOBALS };
