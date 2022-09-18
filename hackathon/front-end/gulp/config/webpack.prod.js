const webpackConfig = require('./webpack');
const merge = require('webpack-merge');
const TerserPlugin = require('terser-webpack-plugin');
const npmScript = process.env.npm_lifecycle_event;
const PRODUCTION = ['buildjs', 'prod'].indexOf(npmScript) > -1;

module.exports = merge(webpackConfig, {
    mode: 'production',
    devtool: false,
    output: {
        filename: '[name].min.js'
    },
    optimization: {
        minimizer: [
            new TerserPlugin({
                cache: true,
                parallel: true,
                sourceMap: !PRODUCTION,
                terserOptions: {
                    output: {
                        comments: false,
                    },
                    compress: {
                        drop_console: PRODUCTION
                    }
                },
            }),
        ],
    }
});