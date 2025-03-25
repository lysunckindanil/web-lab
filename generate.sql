    # noinspection SqlWithoutWhereForFile

    INSERT INTO roles (name)
    SELECT 'ROLE_USER'
    FROM DUAL
    WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ROLE_USER');

    #     insert user1
    INSERT INTO users (id, username, password)
    SELECT 10, 'user10', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
    FROM DUAL
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user10');

    INSERT INTO users_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u,
         roles r
    WHERE u.username = 'user10'
      AND r.name IN ('ROLE_USER')
      AND NOT EXISTS (SELECT 1
                      FROM users_roles ur
                      WHERE ur.user_id = u.id
                        AND ur.role_id = r.id);

    # insert user2
    INSERT INTO users (id, username, password)
    SELECT 11,'user11', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
    FROM DUAL
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user11');

    INSERT INTO users_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u,
         roles r
    WHERE u.username = 'user11'
      AND r.name IN ('ROLE_USER')
      AND NOT EXISTS (SELECT 1
                      FROM users_roles ur
                      WHERE ur.user_id = u.id
                        AND ur.role_id = r.id);


    # insert user3
    INSERT INTO users (id, username, password)
    SELECT 12,'user12', '$2a$10$rgOVlIFwz8ig6KXWRcdNHeywm/qo2HSd1TFFtgNH/9Ucd/yEqRo/y'
    FROM DUAL
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user12');

    INSERT INTO users_roles (user_id, role_id)
    SELECT u.id, r.id
    FROM users u,
         roles r
    WHERE u.username = 'user12'
      AND r.name IN ('ROLE_USER')
      AND NOT EXISTS (SELECT 1
                      FROM users_roles ur
                      WHERE ur.user_id = u.id
                        AND ur.role_id = r.id);


    # issues

    INSERT INTO issues (id, title, description, created_at, author_id)
    VALUES (10, 'Проблема с адаптивной версткой',
            'При разработке сайта для компании столкнулся с проблемой адаптивной верстки. На мобильных устройствах элементы накладываются друг на друга, и текст становится нечитаемым. Пробовал использовать медиазапросы, но результат неудовлетворительный. Возможно, есть более эффективные способы решения этой проблемы? Буду благодарен за советы и примеры кода.',
            NOW(),
            10);


    INSERT INTO issues (id, title, description, created_at, author_id)
    VALUES (11,'Ошибка в работе JavaScript на старых браузерах',
            'Столкнулся с проблемой при тестировании сайта на старых версиях браузеров. Скрипты, написанные на ES6, не работают в Internet Explorer 11. Пробовал использовать Babel для транспиляции, но некоторые функции все равно не работают. Как правильно настроить Babel или есть ли другие способы поддержки старых браузеров?',
            NOW(),
            11);
    SET @issue2 = LAST_INSERT_ID();

    INSERT INTO issues (id,title, description, created_at, author_id)
    VALUES (12,'Проблема с оптимизацией загрузки изображений',
            'На сайте много изображений, и это сильно влияет на скорость загрузки страниц. Пробовал использовать сжатие изображений и формат WebP, но хотелось бы узнать, как еще можно оптимизировать загрузку. Какие инструменты или библиотеки лучше использовать для ленивой загрузки изображений?',
            NOW(),
            12);
    SET @issue3 = LAST_INSERT_ID();

    INSERT INTO issues (id,title, description, created_at, author_id)
    VALUES (13,'Ошибка в работе API при интеграции с CRM',
            'При интеграции сайта с CRM-системой возникла ошибка в работе API. Запросы на сервер возвращают статус 500, хотя на стороне CRM все настроено правильно. В логах сервера вижу ошибки, связанные с форматом данных. Как правильно настроить API и какие инструменты использовать для отладки?',
            NOW(),
            10);
    SET @issue4 = LAST_INSERT_ID();

    INSERT INTO issues (id,title, description, created_at, author_id)
    VALUES (14,'Проблема с SEO-оптимизацией',
            'После запуска сайта заметил, что он плохо индексируется поисковыми системами. Провел базовую SEO-оптимизацию: прописал метатеги, добавил alt-тексты для изображений, но результат не улучшился. Какие еще шаги можно предпринять для улучшения индексации? Может быть, есть рекомендации по структуре сайта или использованию микроразметки?',
            NOW(),
            11);
    SET @issue5 = LAST_INSERT_ID();


    # issue1
    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для адаптивной верстки попробуйте использовать CSS-фреймворк, например, Bootstrap. Он упрощает создание адаптивных макетов.',
            NOW(),
            11,
            10);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для поддержки старых браузеров можно использовать полифиллы. Например, полифилл для Promise или fetch.',
            NOW(),
            11,
            12);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для оптимизации изображений можно использовать CDN, который автоматически сжимает и преобразует изображения.',
            NOW(),
            12,
            10);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Проверьте, правильно ли настроены заголовки запросов. Иногда проблема может быть в Content-Type.',
            NOW(),
            12,
            10);


    # issue2
    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для улучшения SEO добавьте карту сайта и настройте robots.txt. Также полезно использовать микроразметку Schema.org.',
            NOW(),
            10,
            11);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Используйте медиазапросы для разных разрешений экрана. Это поможет сделать верстку более гибкой.',
            NOW(),
            12,
            11);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для отладки API можно использовать Postman. Он позволяет отправлять запросы и анализировать ответы.',
            NOW(),
            10,
            11);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Попробуйте использовать lazy loading для изображений. Это может значительно ускорить загрузку страницы.',
            NOW(),
            10,
            11);


    # issue 3
    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для улучшения индексации добавьте больше качественного контента и внутренних ссылок.',
            NOW(),
            11,
            12);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Убедитесь, что ваш Babel настроен на поддержку нужных версий браузеров. Проверьте конфигурацию .babelrc.',
            NOW(),
            10,
            12);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для адаптивной верстки попробуйте использовать CSS-фреймворк, например, Bootstrap. Он упрощает создание адаптивных макетов.',
            NOW(),
            11,
            12);


    #issue 4
    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для поддержки старых браузеров можно использовать полифиллы. Например, полифилл для Promise или fetch.',
            NOW(),
            11,
            13);

    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Для оптимизации изображений можно использовать CDN, который автоматически сжимает и преобразует изображения.',
            NOW(),
            12,
            13);


    # issue 5
    INSERT INTO comments (content, created_at, author_id, issue_id)
    VALUES ('Проверьте, правильно ли настроены заголовки запросов. Иногда проблема может быть в Content-Type.',
            NOW(),
            10,
            14);
