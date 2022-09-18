const config = require('./');

module.exports = {
    src: [
        config.srcDir + '/assets/**/*.*'
    ],
    dest: config.destDir,
    options: {
        prefix: 1
    }
};