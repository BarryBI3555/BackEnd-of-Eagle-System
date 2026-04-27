CREATE TABLE IF NOT EXISTS location_cache (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  longitude REAL NOT NULL,
  latitude REAL NOT NULL,
  address TEXT NOT NULL,
  create_time DATETIME NOT NULL,
  update_time DATETIME NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_coordinates ON location_cache (longitude, latitude);