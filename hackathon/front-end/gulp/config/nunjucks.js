const config = require('./');

module.exports = {
    src: config.srcDir + '/templates/',
    dest: config.destDir,
    watch: config.srcDir + '/templates/**/*.{html,njk}'
};