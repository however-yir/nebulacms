<script lang="ts" setup>
import type { ListedSinglePage } from "@nebula-labs/api-client";
import { VEntityField } from "@nebula-labs/components";
import { utils } from "@nebula-labs/ui-shared";

withDefaults(
  defineProps<{
    singlePage: ListedSinglePage;
  }>(),
  {}
);
</script>

<template>
  <VEntityField
    v-if="singlePage.page.spec.publishTime"
    v-tooltip="utils.date.format(singlePage.page.spec.publishTime)"
    :description="utils.date.timeAgo(singlePage.page.spec.publishTime)"
  >
  </VEntityField>
</template>
