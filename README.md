# 2.0
- Added PostgreSQL and Redis for persistent database and cache management
- Added GenericJackson2JsonRedisSerializer to convert the cahce data to JSON from Java classes and vice versa for human-readble. Instead of simple `implements Serializable` as it converts to binary and not possible to read data from Redis
