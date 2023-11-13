module.exports = ({ env }) => ({
  defaultConnection: 'default',
  connections: {
    default: {
      connector: 'bookshelf',
      settings: {
        client: 'sqlite',
        filename: env('DATABASE_FILENAME', 'db/data.db'),
      },
      options: {
        useNullAsDefault: true,
      },
    },
  },
});


//for mySQL configuration 
/*
module.exports = ({ env }) => ({
  defaultConnection: 'default',
  connections: {
    default: {
      connector: 'bookshelf',
      settings: {
        client: "mysql",
        host: env('DATABASE_HOST', 'localhost'),
        port: env('DATABASE_PORT', 3307),
        database: env('DATABASE_NAME', 'marketplace'),
        username: env('DATABASE_USERNAME', 'root'),
        password: env('DATABASE_PASSWORD', 'root'),
      },
      options: {
        useNullAsDefault: true,
      },
    },
  },
});
*/