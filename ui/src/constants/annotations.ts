// plugin
export enum pluginAnnotations {
  DISPLAY_NAME = "plugin.nebulacms.io/display-name",
}

// rbac
export enum rbacAnnotations {
  MODULE = "rbac.authorization.nebulacms.io/module",
  ROLE_NAMES = "rbac.authorization.nebulacms.io/role-names",
  DISPLAY_NAME = "rbac.authorization.nebulacms.io/display-name",
  DEPENDENCIES = "rbac.authorization.nebulacms.io/dependencies",
  AVATAR_ATTACHMENT_NAME = "nebulacms.io/avatar-attachment-name",
  LAST_AVATAR_ATTACHMENT_NAME = "nebulacms.io/last-avatar-attachment-name",
  DISALLOW_ACCESS_CONSOLE = "rbac.authorization.nebulacms.io/disallow-access-console",
}

// content

export enum contentAnnotations {
  PREFERRED_EDITOR = "content.nebulacms.io/preferred-editor",
  PATCHED_CONTENT = "content.nebulacms.io/patched-content",
  PATCHED_RAW = "content.nebulacms.io/patched-raw",
  CONTENT_JSON = "content.nebulacms.io/content-json",
  SCHEDULED_PUBLISH_AT = "content.nebulacms.io/scheduled-publish-at",
}

// pat
export enum patAnnotations {
  ACCESS_TOKEN = "security.nebulacms.io/access-token",
}

// Secret
export enum secretAnnotations {
  DESCRIPTION = "secret.nebulacms.io/description",
}
