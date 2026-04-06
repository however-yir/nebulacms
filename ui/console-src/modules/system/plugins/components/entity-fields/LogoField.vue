<script lang="ts" setup>
import type { Plugin } from "@nebula-labs/api-client";
import { VAvatar, VEntityField } from "@nebula-labs/components";
withDefaults(
  defineProps<{
    plugin: Plugin;
  }>(),
  {}
);
</script>

<template>
  <VEntityField>
    <template #description>
      <VAvatar
        :alt="plugin.spec.displayName"
        :src="plugin.status?.logo"
        size="md"
      ></VAvatar>
    </template>
  </VEntityField>
</template>
