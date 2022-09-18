const gulp = require('gulp');
const webpack = require('webpack');
const config = require('../config/webpack.dev');
const WebpackDevServer = require('webpack-dev-server');
const path = require('path');
const configPath = require('../config');

gulp.task('webpack-dev-server', function () {
    const compiler = webpack(config);
    new WebpackDevServer(compiler, {
        contentBase: [path.resolve(configPath.destDir)],
        watchContentBase: true,
        publicPath: '/',
        open: true,
        compress: true,
        allowedHosts: [], // custom host
        watchOptions: {
            poll: 1000 // this option to make sure page refresh after compiled changed
        }
    }).listen(3000, 'localhost', function () { });
});

gulp.task('webpack:dev', function (cb) {
    webpack(config, function () {
        cb();
    });
});