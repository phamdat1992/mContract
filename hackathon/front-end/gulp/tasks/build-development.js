const gulp = require('gulp');
const gulpSequence = require('gulp-sequence');

gulp.task('build:dev', function (cb) {
    gulpSequence(
        'clean',
        'copy',
        ['sass:watch', 'templates:watch'],
        'webpack-dev-server',
        cb
    );
});