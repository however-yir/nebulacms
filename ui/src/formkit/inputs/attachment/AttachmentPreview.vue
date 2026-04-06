<script lang="ts" setup>
import { GetThumbnailByUriSizeEnum } from "@nebula-labs/api-client";
import { utils } from "@nebula-labs/ui-shared";
import mime from "mime/lite";
import { computed } from "vue";
import LazyVideo from "@/components/video/LazyVideo.vue";
import { isImage } from "@/utils/image";

const props = defineProps<{
  url: string;
}>();

const mediaType = computed(() => {
  return mime.getType(props.url);
});
</script>

<template>
  <img
    v-if="isImage(mediaType)"
    :src="utils.attachment.getThumbnailUrl(url, GetThumbnailByUriSizeEnum.S)"
    class="size-full object-cover"
  />
  <LazyVideo
    v-else-if="mediaType?.startsWith('video/')"
    classes="size-full object-cover"
    :src="url"
  />
  <AttachmentFileTypeIcon v-else :file-name="url" />
</template>
