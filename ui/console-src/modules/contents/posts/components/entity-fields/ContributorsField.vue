<script lang="ts" setup>
import type { ListedPost } from "@nebula-labs/api-client";
import { VEntityField } from "@nebula-labs/components";
import PostContributorList from "@/components/user/PostContributorList.vue";

withDefaults(
  defineProps<{
    post: ListedPost;
  }>(),
  {}
);
</script>

<template>
  <VEntityField>
    <template #description>
      <PostContributorList
        :owner="post.owner"
        :contributors="post.contributors"
      />
    </template>
  </VEntityField>
</template>
