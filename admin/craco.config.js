const path = require(`path`);
console.log(path.resolve(__dirname, 'src'));
module.exports = {
  webpack: {
    alias: {
      '@': path.resolve(__dirname, 'src'),
      '@Components': path.resolve(__dirname, 'src/Components'),
      '@Utils': path.resolve(__dirname, 'src/Utils'),
      '@Containers': path.resolve(__dirname, 'src/Containers'),
      '@Pages': path.resolve(__dirname, 'src/Pages'),
      '@Mocks': path.resolve(__dirname, 'src/Mocks'),
      '@Consts': path.resolve(__dirname, 'src/Consts'),
      '@Redux': path.resolve(__dirname, 'src/Redux'),
      '@Services': path.resolve(__dirname, 'src/Services'),
      '@Enums': path.resolve(__dirname, 'src/Enums'),
    },
    resolve: {
      extensions: ['.tsx', '.jsx', '.js', '.ts', '.json']
    }
  },
};