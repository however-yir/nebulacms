<script lang="ts" setup>
import type { ListedSinglePage } from "@nebula-labs/api-client";
import { VEntityField } from "@nebula-labs/components";
import PostContributorList from "@/components/user/PostContributorList.vue";

withDefaults(
  defineProps<{
    singlePage: ListedSinglePage;
  }>(),
  {}
);
</script>

<template>
  <VEntityField>
    <template #description>
      <PostContributorList
        :owner="singlePage.owner"
        :contributors="singlePage.contributors"
      />
    </template>
  </VEntityField>
</template>
