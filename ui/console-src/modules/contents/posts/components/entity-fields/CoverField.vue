<script lang="ts" setup>
import {
  GetThumbnailByUriSizeEnum,
  type ListedPost,
} from "@nebula-labs/api-client";
import { VEntityField } from "@nebula-labs/components";
import { utils } from "@nebula-labs/ui-shared";

withDefaults(
  defineProps<{
    post: ListedPost;
  }>(),
  {}
);
</script>

<template>
  <VEntityField v-if="post.post.spec.cover">
    <template #description>
      <div class="aspect-h-2 aspect-w-3 w-20 overflow-hidden rounded-md">
        <img
          class="h-full w-full object-cover"
          :src="
            utils.attachment.getThumbnailUrl(
              post.post.spec.cover,
              GetThumbnailByUriSizeEnum.S
            )
          "
        />
      </div>
    </template>
  </VEntityField>
</template>
