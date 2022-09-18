const gulp = require('gulp');
const webpack = require('webpack');
const config = require('../config/webpack.prod');

gulp.task('webpack:prod', function (cb) {
    webpack(config, function () {
        cb();
    });
});