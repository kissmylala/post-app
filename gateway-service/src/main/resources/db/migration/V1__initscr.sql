CREATE TABLE IF NOT EXISTS public.roles (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL UNIQUE
);


CREATE TABLE IF NOT EXISTS public.users (
                                     id SERIAL PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
                                     username VARCHAR(255) NOT NULL UNIQUE,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL,
                                     enabled BOOLEAN NOT NULL DEFAULT TRUE,
                                     created_at TIMESTAMP NOT NULL,
                                     role_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS public.user_tokens(
                                          id SERIAL PRIMARY KEY,
                                          token VARCHAR(255) NOT NULL,
                                          token_type VARCHAR(255) NOT NULL DEFAULT 'BEARER',
                                          revoked BOOLEAN NOT NULL,
                                          expired BOOLEAN NOT NULL,
                                          user_id BIGINT REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS public.user_roles (
                                          user_id BIGINT NOT NULL,
                                          role_id BIGINT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
                                          FOREIGN KEY (user_id) REFERENCES users (id),
                                          FOREIGN KEY (role_id) REFERENCES roles (id)
);

