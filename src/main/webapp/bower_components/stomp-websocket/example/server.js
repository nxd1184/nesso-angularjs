var connect = require('connect');

connect()
  .use(connect.logger('dev'))
  .use(connect.static(__dirname))
  .use(connect.static(__dirname + '/../lib/'))
  .listen(9090);
console.log('Server running at http://0.0.0.0:9090/');
