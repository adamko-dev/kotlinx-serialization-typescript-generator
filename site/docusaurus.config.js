// @ts-check
// Note: type annotations allow type checking and IDEs autocompletion

const config = /** @type {import("@docusaurus/types").Config} */ {
  title: "kxs-ts-gen",
  tagline: "Generate TypeScript interfaces from Kotlin classes",
  url: "https://adamko-dev.github.io",
  // baseUrl: "/kotlinx-serialization-typescript-generator",
  baseUrl: "/",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  favicon: "img/icon.svg",

  // GitHub pages deployment config
  organizationName: "adamko-dev",
  projectName: "kotlinx-serialization-typescript-generator",

  i18n: {defaultLocale: "en", locales: ["en"]},

  presets: [
    [
      "classic", /** @type {import("@docusaurus/preset-classic").Options} */ ({
      docs: {
        sidebarPath: require.resolve("./sidebars.js"),
        sidebarCollapsible: false,
        editUrl: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/blob/main/",
      },
      theme: {
        customCss: [
          require.resolve("./src/css/custom.css"),
        ],
      },
    }),
    ],
  ],

  // scripts: [],

  clientModules: [
    require.resolve("./src/modules/global.scss"),
  ],

  themeConfig: /** @type {import("@docusaurus/preset-classic").ThemeConfig} */ ({
    colorMode: {
      defaultMode: "dark",
      disableSwitch: false,
      respectPrefersColorScheme: true,
    },
    metadata: [{
      name: "keywords",
      content: "kotlin, typescript, json, transform, convert, generate"
    }],
    navbar: {
      title: "kxs-ts-gen",
      logo: {alt: "kxs-ts-gen logo", src: "img/icon.svg"},
      items: [
        // {to: "/getting-started", label: "Getting started", position: "left"},
        {type: "doc", docId: "getting-started", label: "Docs", position: "left"},
        {
          href: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator",
          label: "GitHub",
          position: "right",
        },
      ],
    },
    footer: {
      style: "dark",
      links: [
        {
          title: "Docs",
          items: [
            // {label: "Getting started", to: "/getting-started"},
            {label: "Docs", to: "/docs"},
            {label: "Examples", to: "/docs/examples"},
          ],
        },
        {
          title: "More",
          items: [
            {
              label: "GitHub",
              href: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/",
            },
            {
              label: "Releases",
              href: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/releases",
            },
            {
              label: "Help and Discussions",
              href: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/discussions",
            },
            {
              label: "Issues and requests",
              href: "https://github.com/adamko-dev/kotlinx-serialization-typescript-generator/issues",
            },
          ]
        }
      ],
      copyright: `Copyright © 2021`,
    },
    prism: {
      // themes are managed by global.scss
      theme: {plain: {}, styles: []},
      darkTheme: {plain: {}, styles: []},
      additionalLanguages: ["kotlin", "typescript", "groovy", "markup"],
    },
  }),

  plugins: ["docusaurus-plugin-sass"],
};

module.exports = config;
