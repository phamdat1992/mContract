const gulp = require('gulp');

gulp.task('build', function () {
    if (!process.env.ENV || process.env.EVN == 'development') {
        gulp.start('build:development');
    } else {
        gulp.start('build:production');
    }
});