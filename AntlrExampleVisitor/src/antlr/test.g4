grammar test;

start: ID ':' INT_TYPE '=' ID;

ID: [a-z][a-zA-Z0-9_]*;
INT_TYPE: 'INT';