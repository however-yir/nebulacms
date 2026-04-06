<script setup lang="ts">
import type { OwnerInfo } from "@nebula-labs/api-client";
import { VAvatar } from "@nebula-labs/components";

defineProps<{
  owner: OwnerInfo;
}>();
</script>
<template>
  <div
    class="-m-1 inline-flex cursor-pointer items-center gap-1.5 rounded-lg p-1 transition-colors hover:bg-gray-100"
  >
    <VAvatar circle :src="owner?.avatar" :alt="owner?.displayName" size="xs" />
    <span class="text-sm font-medium text-gray-900">
      {{ owner?.displayName }}
    </span>
  </div>
</template>
