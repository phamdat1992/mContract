const requireDir = require('require-dir');
// Require all tasks in ./gulp/tasks and including subfolders
requireDir('./gulp/tasks', { recurse: true });
