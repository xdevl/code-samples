const webpack = require('webpack');
const fs = require('fs');
const path = require('path');
const { spawn } = require('child_process');
const HtmlWebpackPlugin = require('html-webpack-plugin');

const lintRule = {
  test: /\.tsx?$/,
  enforce: 'pre',
  use: [{
    loader: 'tslint-loader',
    options: {
      failOnHint: true,
      configFile: 'tslint.json',
      tsConfigFile: 'tsconfig.json'
    }
  }],
  exclude: /node_modules/
};

const typeScriptRule = {
  test: /\.tsx?$/,
  use: [{
    loader: 'ts-loader',
  }],
  exclude: /node_modules/
};

const cssRule = {
  test: /\.css$/,
  use: [
    'css-loader'
  ]
};

const mediaRule = {
  test: /\.(png|svg|jpg|gif)$/,
  use: [
    'file-loader'
  ]
};

const htmlRule = {
  test: /\.(html)$/,
  use: {
    loader: 'html-loader',
    options: {
      interpolate: true,
      attrs: ['img:src', 'link:href']
    }
  }
};

const serverConfig = {
  target: 'node',
  node: {
    __dirname: false
  },
  entry: path.resolve(__dirname, 'src', 'server', 'index.ts'),
  module: {
    rules: [lintRule, typeScriptRule]
  },
  resolve: {
    extensions: [ '.tsx', '.ts', '.js' ]
  },
  output: {
    filename: 'index.js',
    path: path.resolve(__dirname, 'dist')
  },
  externals: fs.readdirSync('node_modules')
    .filter((x) => ['.bin'].indexOf(x) === -1)
    .reduce((modules, module) => Object.assign(modules, {[module]: `commonjs ${module}`}), {}),
  plugins: [
    new webpack.DefinePlugin({
      VERSION: JSON.stringify(process.env.npm_package_version),
    }),
    {apply: (compiler) => compiler.hooks.afterEmit.tap('NodemonPlugin', (compilation) =>
      this.ran = this.ran ||
        (compiler.watchMode && spawn('nodemon', ['-w', 'dist/index.js'], {stdio: 'inherit'}))
    )}
  ]
};

const browserConfig = {
  target: 'web',
  entry: path.resolve(__dirname, 'src', 'browser', 'index.tsx'),
  module: {
    rules: [htmlRule, lintRule, typeScriptRule, cssRule, mediaRule]
  },
  resolve: {
    extensions: [ '.tsx', '.ts', '.js' ]
  },
  output: {
    filename: '[name].[contenthash].js',
    path: path.resolve(__dirname, 'dist', 'browser')
  },
  node: {
    fs: 'empty'
  }, optimization: {
    runtimeChunk: 'single',
    splitChunks: {
      chunks: 'all',
    }
  },
  plugins: [
    new webpack.DefinePlugin({
       VERSION: JSON.stringify(process.env.npm_package_version),
     }),
    new HtmlWebpackPlugin({
      template: path.resolve(__dirname, 'src', 'resources', 'index.html')
    }),
  ]
};

module.exports = [serverConfig, browserConfig];
