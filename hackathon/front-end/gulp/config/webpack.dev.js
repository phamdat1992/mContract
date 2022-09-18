const webpackConfig = require('./webpack');
const merge = require('webpack-merge');

module.exports = merge(webpackConfig, {
    devtool: 'eval-source-map',
    watchOptions: {
        ignored: /node_modules/
    },
    output: {
        pathinfo: false,
        filename: '[name].js'
    }
});