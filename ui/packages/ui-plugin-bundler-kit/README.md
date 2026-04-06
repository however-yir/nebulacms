# @nebula-labs/ui-plugin-bundler-kit

A frontend build toolkit for Halo plugin development, supporting both Vite and Rsbuild build systems.

## Introduction

`@nebula-labs/ui-plugin-bundler-kit` is a frontend build configuration toolkit specifically designed for Halo plugin development. It provides pre-configured build settings to help developers quickly set up and build frontend interfaces for Halo plugins.

### Key Features

- 🚀 **Ready to Use** - Provides pre-configured Vite and Rsbuild build settings
- 📦 **Multi-Build Tool Support** - Supports both Vite and Rsbuild
- 🔧 **Flexible Configuration** - Supports custom build configurations
- 🎯 **Halo Optimized** - External dependencies and global variables optimized for Halo plugin development
- 📁 **Smart Output** - Automatically selects output directory based on environment

## Installation

```bash
# Using npm
npm install @nebula-labs/ui-plugin-bundler-kit

# Using yarn
yarn add @nebula-labs/ui-plugin-bundler-kit

# Using pnpm
pnpm add @nebula-labs/ui-plugin-bundler-kit
```

### Additional Dependencies

**For Vite users**, you need to install Vite:

```bash
npm install vite
```

**For Rsbuild users**, you need to install Rsbuild:

```bash
npm install @rsbuild/core
```

## Usage

### Vite Configuration

Create or update `vite.config.ts` file in your project root:

```typescript
import { viteConfig } from "@nebula-labs/ui-plugin-bundler-kit";

export default viteConfig({
  vite: {
    // Your custom Vite configuration
    plugins: [
      // Additional plugins (Vue plugin is already included)
    ],
    // Other configurations...
  },
});
```

> **Note**: Vue plugin is pre-configured, no need to add it manually.

### Rsbuild Configuration

Create or update `rsbuild.config.ts` file in your project root:

```typescript
import { rsbuildConfig } from "@nebula-labs/ui-plugin-bundler-kit";

export default rsbuildConfig({
  rsbuild: {
    // Your custom Rsbuild configuration
    plugins: [
      // Additional plugins (Vue plugin is already included)
    ],
    // Other configurations...
  },
});
```

> **Note**: Vue plugin is pre-configured, no need to add it manually.

### Legacy Configuration (Deprecated)

> ⚠️ **Note**: The `HaloUIPluginBundlerKit` function is deprecated. Please use `viteConfig` or `rsbuildConfig` instead.

```typescript
import { HaloUIPluginBundlerKit } from "@nebula-labs/ui-plugin-bundler-kit";

export default {
  plugins: [
    HaloUIPluginBundlerKit({
      // Configuration options
    }),
  ],
};
```

## Configuration Options

### Vite Configuration Options

```typescript
interface ViteUserConfig {
  /**
   * Halo plugin manifest file path
   * @default "../src/main/resources/plugin.yaml"
   */
  manifestPath?: string;

  /**
   * Custom Vite configuration
   */
  vite: UserConfig | UserConfigFnObject;
}
```

### Rsbuild Configuration Options

```typescript
interface RsBuildUserConfig {
  /**
   * Halo plugin manifest file path
   * @default "../src/main/resources/plugin.yaml"
   */
  manifestPath?: string;

  /**
   * Custom Rsbuild configuration
   */
  rsbuild: RsbuildConfig | ((env: ConfigParams) => RsbuildConfig);
}
```

## Advanced Configuration Examples

### Adding Path Aliases (Vite)

```typescript
import { viteConfig } from "@nebula-labs/ui-plugin-bundler-kit";
import path from "path";

export default viteConfig({
  vite: {
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "src"),
        "@components": path.resolve(__dirname, "src/components"),
      },
    },
  },
});
```

### Adding Path Aliases (Rsbuild)

```typescript
import { rsbuildConfig } from "@nebula-labs/ui-plugin-bundler-kit";

export default rsbuildConfig({
  rsbuild: {
    source: {
      alias: {
        "@": "./src",
        "@components": "./src/components",
      },
    },
  },
});
```

### Adding Additional Vite Plugins

```typescript
import { viteConfig } from "@nebula-labs/ui-plugin-bundler-kit";
import { defineConfig } from "vite";
import UnoCSS from "unocss/vite";

export default viteConfig({
  vite: {
    plugins: [
      UnoCSS(), // Add UnoCSS plugin
    ],
  },
});
```

### Adding Additional Rsbuild Plugins

```typescript
import { rsbuildConfig } from "@nebula-labs/ui-plugin-bundler-kit";
import { pluginSass } from "@rsbuild/plugin-sass";

export default rsbuildConfig({
  rsbuild: {
    plugins: [
      pluginSass(), // Add Sass plugin
    ],
  },
});
```

### Custom Plugin Manifest Path

```typescript
import { viteConfig } from "@nebula-labs/ui-plugin-bundler-kit";

export default viteConfig({
  manifestPath: "application/src/main/resources/plugin.yaml", // Custom manifest file path
  vite: {
    // Other configurations...
  },
});
```

## Development Scripts

Recommended scripts to add to your `package.json`:

```json
{
  "scripts": {
    "dev": "vite dev --mode=development --watch",
    "build": "vite build"
  }
}
```

For Rsbuild:

```json
{
  "scripts": {
    "dev": "rsbuild dev --env-mode=development --watch",
    "build": "rsbuild build"
  }
}
```

## Build Output

> Relative to the root directory of the Halo plugin project

- **Development**: `build/resources/main/console`
- **Production**: `ui/build/dist`

> **Note**: The production build output directory of `HaloUIPluginBundlerKit` is still `src/main/resources/console` to ensure compatibility.

## Requirements

- **Node.js**: ^18.0.0 || >=20.0.0
- **Peer Dependencies**:
  - `@rsbuild/core`: ^1.0.0 (when using Rsbuild)
  - `@rsbuild/plugin-vue`: ^1.0.0 (when using Rsbuild)
  - `@vitejs/plugin-vue`: ^4.0.0 || ^5.0.0 (when using Vite)
  - `vite`: ^4.0.0 || ^5.0.0 || ^6.0.0 (when using Vite)

## Vite vs Rsbuild

Both Vite and Rsbuild are excellent build tools, but they have different strengths depending on your use case:

### When to Use Rsbuild

**Recommended for large-scale plugins**

- ✅ **Code Splitting Support** - Rsbuild provides excellent support for code splitting and lazy loading
- ✅ **Better Performance** - Generally faster build times and smaller bundle sizes for complex applications
- ✅ **Dynamic Imports** - Perfect for plugins with heavy frontend components

**Example with dynamic imports:**

```typescript
import { definePlugin } from "@nebula-labs/ui-shared";
import { defineAsyncComponent } from "vue";
import { VLoading } from "@nebula-labs/components";

export default definePlugin({
  routes: [
    {
      parentName: "Root",
      route: {
        path: "demo",
        name: "DemoPage",
        // Lazy load heavy components
        component: defineAsyncComponent({
          loader: () => import("./views/DemoPage.vue"),
          loadingComponent: VLoading,
        }),
      },
    },
  ],
  extensionPoints: {},
});
```

### When to Use Vite

**Recommended for simple to medium-scale plugins**

- ✅ **Vue Ecosystem Friendly** - Better integration with Vue ecosystem tools and plugins
- ✅ **Rich Plugin Ecosystem** - Extensive collection of Vite plugins available
- ✅ **Simple Configuration** - Easier to configure for straightforward use cases

### Summary

| Feature           | Vite         | Rsbuild      |
| ----------------- | ------------ | ------------ |
| Code Splitting    | ❌ Limited   | ✅ Excellent |
| Vue Ecosystem     | ✅ Excellent | ✅ Good      |
| Build Performance | ✅ Good      | ✅ Excellent |
| Dev Experience    | ✅ Excellent | ✅ Excellent |
| Plugin Ecosystem  | ✅ Rich      | ✅ Growing   |
| Configuration     | ✅ Simple    | ⚖️ Moderate  |

**Recommendation**: Use **Rsbuild** for complex plugins with large frontend codebases, and **Vite** for simpler plugins or when you need extensive Vue ecosystem integration.

## License

GPL-3.0

## Contributing

Issues and Pull Requests are welcome! Please check our [Contributing Guide](https://github.com/nebula-labs/nebulacms/blob/main/CONTRIBUTING.md) for more information.

## Related Links

- [Halo Website](https://www.nebulacms.io/)
- [Halo Documentation](https://docs.nebulacms.io/)
- [GitHub Repository](https://github.com/nebula-labs/nebulacms)
- [Plugin Development Guide](https://docs.nebulacms.io/category/ui)
