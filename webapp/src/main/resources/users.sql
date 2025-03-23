INSERT INTO roles (name)
SELECT 'ROLE_ADMIN'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_ADMIN');

INSERT INTO roles (name)
SELECT 'ROLE_USER'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

INSERT INTO roles (name)
SELECT 'ROLE_REDACTOR'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_REDACTOR');

INSERT INTO users (username, password)
SELECT 'admin', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'admin'
  AND r.name IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_REDACTOR')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);

