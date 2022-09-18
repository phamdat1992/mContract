const config = require('../config/copy');
const gulp = require('gulp');
const copy = require('gulp-copy');

gulp.task('copy', function () {
    return gulp.src(config.src)
        .pipe(copy(config.dest, config.options));
});
