module.exports = {
   webpack: config => {
      const rules = config.module.rules
            .find((rule) => typeof rule.oneOf === 'object')
            .oneOf.filter((rule) => Array.isArray(rule.use));

        rules.forEach((rule) => {
            rule.use.forEach((moduleLoader) => {
                if (moduleLoader.loader.includes('scss-loader') && !moduleLoader.loader.includes('postcss-loader')) {
                    delete moduleLoader.options.modules.getLocalIdent;
                    moduleLoader.options = {
                        ...moduleLoader.options,
                        modules: {
                            ...moduleLoader.options.modules,
                            localIdentName: '[hash:base64:5]'
                            // You can also add other css-loader options here
                        }
                    };
                }
            });
        });

        return config;
   }
}
