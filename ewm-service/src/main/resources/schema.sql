CREATE TABLE IF NOT EXISTS users  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(250) NOT NULL,
    email varchar(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS locations  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat DECIMAL NOT NULL,
    lon DECIMAL NOT NULL
);

CREATE TABLE IF NOT EXISTS events  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation varchar(2000) NOT NULL,
    category_id INTEGER references categories(id),
    confirmed_requests INTEGER NOT NUll,
    created_on TIMESTAMP NOT NULL,
    description varchar(7000) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id INTEGER references users(id) NOT NULL,
    location_id INTEGER references locations(id) NOT NULL,
    paid Boolean NOT NULL,
    participant_limit INTEGER NOT NULL,
    published_on TIMESTAMP,
    request_moderation Boolean NOT NULL,
    state varchar(20) NOT NULL,
    title varchar(120) NOT NULL,
    views INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned Boolean,
    title varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    compilation_id INTEGER references compilations(id) NOT NULL,
    event_id INTEGER references events(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS requests  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created TIMESTAMP NOT Null,
    event_id INTEGER references events(id) NOT NULL,
    requester_id INTEGER references users(id) NOT NULL,
    status varchar(200) NOT NULL,
    UNIQUE (event_id, requester_id)
);

CREATE TABLE IF NOT EXISTS comments  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    author_id INTEGER references users(id) NOT NULL,
    event_id INTEGER references events(id) NOT NULL,
    created TIMESTAMP NOT Null,
    text varchar(2000) NOT NULL,
    is_positive boolean NOT NULL
);