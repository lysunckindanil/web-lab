INSERT INTO roles (name)
SELECT 'ROLE_USER'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

#     insert user1
INSERT INTO users (id, username, password)
SELECT 10, 'expert1', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'expert1');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'expert1'
  AND r.name IN ('ROLE_USER')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);

# insert user2
INSERT INTO users (id, username, password)
SELECT 11, 'expert2', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'expert2');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'expert2'
  AND r.name IN ('ROLE_USER')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);


# insert user3
INSERT INTO users (id, username, password)
SELECT 12, 'expert3', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'expert3');

INSERT INTO users_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u,
     roles r
WHERE u.username = 'expert3'
  AND r.name IN ('ROLE_USER')
  AND NOT EXISTS (SELECT 1
                  FROM users_roles ur
                  WHERE ur.user_id = u.id
                    AND ur.role_id = r.id);


# issues

INSERT INTO issues (id, title, description, created_at, author_id)
VALUES (10, 'Как правильно настроить баланс белого?',
        'Баланс белого зависит от освещения. Для точной настройки используйте серую карту или ручной режим. При съёмке в RAW корректируйте температуру в постобработке. В помещении с лампами накаливания выбирайте "Тungsten", а для облачной погоды — "Cloudy". Автоматический режим не всегда точен.',
        NOW(),
        10);


INSERT INTO issues (id, title, description, created_at, author_id)
VALUES (11, 'Какие объективы лучше для портретов?',
        'Для портретов идеальны светосильные фикс-объективы (50mm f/1.8, 85mm f/1.4). Они создают красивое размытие фона (боке) и передают детали. Зумы типа 24-70mm тоже подходят, но дают менее выраженное боке. Избегайте широкоугольных линз — они искажают черты лица.',
        NOW(),
        11);
SET @issue2 = LAST_INSERT_ID();

INSERT INTO issues (id, title, description, created_at, author_id)
VALUES (12, 'Как избежать шумов на ночных фото?',
        'Чтобы снизить шумы при ночной съёмке, используйте низкое ISO (400–800), штатив и длительную выдержку. Снимайте в RAW для лучшей обработки. В редакторах (Lightroom, DxO) применяйте шумоподавление, но не переусердствуйте — можно потерять детали.',
        NOW(),
        12);
SET @issue3 = LAST_INSERT_ID();

INSERT INTO issues (id, title, description, created_at, author_id)
VALUES (13, 'Какие настройки использовать для съёмки воды?',
        'Для съёмки воды экспериментируйте с выдержкой. Короткая (1/500 сек) "заморозит" брызги, а длинная (2+ сек) создаст эффект тумана. Используйте нейтральные фильтры (ND) для защиты от пересвета. Диафрагму закройте до f/8–f/11 для резкости.',
        NOW(),
        10);
SET @issue4 = LAST_INSERT_ID();

INSERT INTO issues (id, title, description, created_at, author_id)
VALUES (14, 'Как редактировать фото в стиле ретро?',
        'Ретро-стиль достигается тонированием (сепия, тёплые оттенки), добавлением зерна и виньетирования. В Lightroom используйте пресеты "Vintage". Уменьшите насыщенность, повысьте контраст. Для плёночного эффекта попробуйте плагины (VSCO, RNI Films). Важно имитировать мелкие царапины и неравномерное освещение.',
        NOW(),
        11);
SET @issue5 = LAST_INSERT_ID();


# issue1
INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Снимайте в RAW, чтобы легко корректировать температуру цвета. Для точности используйте серую карту или ручной замер.',
        NOW(),
        11,
        10);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Лучше всего подходят фикс-линзы 50mm или 85mm. Они дают красивое боке и не искажают пропорции лица.',
        NOW(),
        11,
        12);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Штатив обязателен! ISO не выше 1600, выдержка от 1 сек. Шумы убирайте в Lightroom.',
        NOW(),
        12,
        10);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Для "шёлка" — выдержка 2+ сек и ND-фильтр. Для брызг — 1/500 сек.',
        NOW(),
        12,
        10);


# issue2
INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Добавьте зерно, виньетирование и тёплые тона. Пресеты VSCO или RNI Films упростят работу.',
        NOW(),
        10,
        11);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Протрите объектив, используйте ручной фокус. Штатив или упор помогут избежать смазов.',
        NOW(),
        12,
        11);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Добавьте передний план (ветки, камни) и контровой свет. Это сделает фото объёмнее.',
        NOW(),
        10,
        11);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Диафрагма f/2.8, выдержка 20 сек, ISO 3200. Фокусируйтесь вручную на яркой звезде.',
        NOW(),
        10,
        11);


# issue 3
INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Работайте с тенями и текстурами. Добавьте контраст в постобработке.',
        NOW(),
        11,
        12);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Будьте незаметны. Объектив 35mm, режим серийной съёмки. Ловите эмоции и движение.',
        NOW(),
        10,
        12);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Лучше всего подходят фикс-линзы 50mm или 85mm. Они дают красивое боке и не искажают пропорции лица.',
        NOW(),
        11,
        12);


#issue 4
INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Лучше всего подходят фикс-линзы 50mm или 85mm. Они дают красивое боке и не искажают пропорции лица.',
        NOW(),
        11,
        13);

INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Штатив обязателен! ISO не выше 1600, выдержка от 1 сек. Шумы убирайте в Lightroom.',
        NOW(),
        12,
        13);


# issue 5
INSERT INTO comments (content, created_at, author_id, issue_id)
VALUES ('Для "шёлка" — выдержка 2+ сек и ND-фильтр. Для брызг — 1/500 сек.',
        NOW(),
        10,
        14);
