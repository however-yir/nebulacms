// plugin
export enum pluginLabels {
  NAME = "plugin.nebulacms.io/plugin-name",
  SYSTEM_RESERVED = "plugin.nebulacms.io/system-reserved",
}

// role
export enum roleLabels {
  TEMPLATE = "nebulacms.io/role-template",
  HIDDEN = "nebulacms.io/hidden",
  SYSTEM_RESERVED = "rbac.authorization.nebulacms.io/system-reserved",
}

// post
export enum postLabels {
  DELETED = "content.nebulacms.io/deleted",
  PUBLISHED = "content.nebulacms.io/published",
  OWNER = "content.nebulacms.io/owner",
  VISIBLE = "content.nebulacms.io/visible",
  PHASE = "content.nebulacms.io/phase",
  SCHEDULING_PUBLISH = "content.nebulacms.io/scheduling-publish",
}

// singlePage
export enum singlePageLabels {
  DELETED = "content.nebulacms.io/deleted",
  PUBLISHED = "content.nebulacms.io/published",
  OWNER = "content.nebulacms.io/owner",
  VISIBLE = "content.nebulacms.io/visible",
  PHASE = "content.nebulacms.io/phase",
}

// attachment
export enum attachmentPolicyLabels {
  // Used for ui display only
  HIDDEN = "storage.nebulacms.io/policy-hidden-in-upload-ui",
  HIDDEN_WITH_JSON_PATCH = "storage.nebulacms.io~1policy-hidden-in-upload-ui",
  PRIORITY = "storage.nebulacms.io/policy-priority-in-upload-ui",
  PRIORITY_WITH_JSON_PATCH = "storage.nebulacms.io~1policy-priority-in-upload-ui",
}
