const gulp = require('gulp');
const gulpSequence = require('gulp-sequence');

gulp.task('build:production', function (cb) {
    gulpSequence(
        'clean',
        'copy',
        'sass:optimize',
        'webpack:prod',
        'templates:prod',
        cb);
});