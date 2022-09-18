const config = require('./index');

module.exports = {
    src: config.srcDir + '/sass',
    dest: config.destDir + '/css',
    pattern: '/**/*.{sass,scss}',
    settings: {
        indentedSyntax: true, // Enable .sass syntax!
        imagePath: 'images' // Used by the image-url helper
    }
};
