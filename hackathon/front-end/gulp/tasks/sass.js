const gulp = require('gulp');
const sass = require('gulp-sass');
const sourcemaps = require('gulp-sourcemaps');
const config = require('../config/sass');
const autoprefixer = require('gulp-autoprefixer');
const browserSync = require('browser-sync');
const gulpSequence = require('gulp-sequence');
const strip_comments = require('gulp-strip-css-comments');

gulp.task('sass', function () {
    return gulp.src(config.src + '/*.{scss,sass}')
        .pipe(sourcemaps.init())
        .pipe(sass({
            includePaths: require('node-bourbon').includePaths
        }))
        .pipe(sass().on('error', sass.logError))
        .pipe(autoprefixer())
        .pipe(sourcemaps.write('./maps'))
        .pipe(gulp.dest(config.dest))
        .pipe(browserSync.stream());
});

gulp.task('sass:optimize', function () {
    return gulp.src(config.src + '/*.{scss,sass}')
        .pipe(sass({
            includePaths: require('node-bourbon').includePaths
        }))
        .pipe(autoprefixer())
        .pipe(sass({ outputStyle: 'compressed' }))
        .pipe(strip_comments())
        .pipe(gulp.dest(config.dest))
        .pipe(browserSync.stream());
});

gulp.task('sass:watch', function () {
    gulpSequence('sass', function () { });
    gulp.watch(config.src + config.pattern).on('change', function () {
        gulpSequence('sass', function () { });
    });
});
