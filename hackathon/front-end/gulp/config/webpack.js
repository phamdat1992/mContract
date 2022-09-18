const config = require('.');
const path = require('path');
const bourbon = require('node-bourbon');
const PROJECT_PATH = path.join(__dirname, '../..');
const npmScript = process.env.npm_lifecycle_event;
const PRODUCTION = ['buildjs', 'prod'].indexOf(npmScript) > -1;
const autoprefixer = require('autoprefixer');
const ASSET_PATH = process.env.ASSET_PATH || 'http://localhost:3000/';

module.exports = {
    resolve: {
        alias: {
            src: path.resolve(PROJECT_PATH, 'src')
        },
        extensions: ['.mjs', '.js']
    },
    entry: {
        app: path.resolve(config.jsSrcDir + '/index')
    },
    output: {
        path: path.resolve(config.jsDestDir),
        filename: '[name].js',
        publicPath: ASSET_PATH
    },
    module: {
        rules: [
            {
                test: /\.js$/,
                loader: 'cache-loader',
                include: path.resolve(config.jsSrcDir),
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                include: path.resolve(config.jsSrcDir),
                exclude: /(node_modules)/,
                options: {
                    compact: true
                }
            },
            {
                test: /\.(scss|css)/,
                use: [
                    {
                        loader: 'style-loader'
                    },
                    {
                        loader: 'css-loader',
                        options: {
                            sourceMap: !PRODUCTION
                        }
                    },
                    {
                        loader: 'postcss-loader',
                        options: {
                            plugins: () => [autoprefixer()]
                        }
                    },
                    {
                        loader: 'sass-loader',
                        options: {
                            sourceMap: !PRODUCTION,
                            sassOptions: {
                                includePaths: [bourbon.includePaths]
                            }
                        }
                    }
                ]
            }
        ]
    }
};