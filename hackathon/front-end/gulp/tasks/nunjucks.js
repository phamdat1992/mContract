const gulp = require('gulp');
const nunjucks = require('gulp-nunjucks');
const nunjucksConfig = require('../config/nunjucks');
const gulpSequence = require('gulp-sequence');

gulp.task('templates', function () {
    gulp.src(nunjucksConfig.src + '*.html')
        .pipe(nunjucks.compile({
            PRODUCTION: false
        }))
        .pipe(gulp.dest(nunjucksConfig.dest));
});

gulp.task('templates:prod', function () {
    gulp.src(nunjucksConfig.src + '*.html')
        .pipe(nunjucks.compile({
            PRODUCTION: true
        }))
        .pipe(gulp.dest(nunjucksConfig.dest));
});

gulp.task('templates:watch', function () {
    gulpSequence('templates', function () { });
    gulp.watch(nunjucksConfig.watch).on('change', function () {
        gulpSequence('templates', function () { });
    });
});
