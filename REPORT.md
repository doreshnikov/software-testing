## Homework 1

* ✔️ создана папка [`client`](client)
* ✔️ в ней размещено приложение на _Vue.js_
* ✔️ 5 страниц (+рутинг)
* ✔️ unit-тесты и компонентные тесты (_jest_, _@vue/test-utils_)


* ✔️ авторизация (`jwt` + `localStorage`, _Vuex_)
    * есть тесты для проверки авторизации на компонентном уровне
* ✔️ достаточно много наборов тестов

---

```bash
cd client
npm run test:uc
# jest test/unit test/component
```

Тесты находятся в каталоге [`test`](client/test). Для unit-тестирования и компонентного тестирования использовался _
jest_ с _@vue/test-utils_.

* `test/unit/` &ndash; unit-тесты
    * [`utils.test.js`](client/test/unit/utils.test.js)
      тестирование вспомогательных функций
    * [`store.test.js`](client/test/unit/store.test.js)
      тестирование _Vuex_-хранилищ
* `test/component/` &ndash; компонентные тесты
    * [`fragments.test.js`](client/test/component/fragments.test.js)
      тестирование общих компонент
    * `pages/`
      тестирование компонент, относящихся к определенным страницам
        * [`home.test.js`](client/test/component/pages/home.test.js)
        * [`login.test.js`](client/test/component/pages/login.test.js)
        * [`title.test.js`](client/test/component/pages/title.test.js)

---

## Homework 2

* ✔️ написан примитивный [`server-js`](server-js) с использованием _Express.js_
* ✔️ e2e тесты (_Cypress_, _Playwright_)
    * тесты на прохождение авторизации
    * тесты на стили и внешний вид сервиса
    * тесты на нетривиальные сюжеты взаимодействия пользователя и сервиса

---

```bash
cd client && npm run deploy
# cd server && npm run start && cd ../client && npm run serve

npm run test:e2e:c
# npx cypress run
npm run test:e2e
# jest test/e2e/playwright
```

* `test/e2e/cypress` &ndash; тесты _Cypress_
    * [`authentication.test.js`](client/test/e2e/cypress/authentication.test.js)
      тестирование авторизации и возможности сохранять состояние
    * [`navigation.test.js`](client/test/e2e/cypress/navigation.test.js)
      тестирование реакции сервиса на navbar с _VueRouter_
    * [`requests.test.js`](client/test/e2e/cypress/requests.test.js)
      тестирование возможностей авторизованного пользователя
* `test/e2e/playwright` &ndash; тесты _Playwright_
    * [`interceptors.test.js`](client/test/e2e/playwright/interceptors.test.js)
      тестирование надстройки над _axios_, обрабатывающей ошибки доступа
    * [`views.test.js`](client/test/e2e/playwright/views.test.js)
      тестирование внешнего вида компонент (формально не являются e2e-тестами, 
      однако не могут быть без реальной отрисовки компонент)

---

## Homework 3

#### In progress...

---

## Homework 4

* ✔️ созданы _GitHub Actions_ для запуска тестов UI
    * [`test.client.uc.yaml`](.github/workflows/test.client.uc.yaml) unit и component тесты
    * [`test.client.e2e.yaml`](.github/workflows/test.client.e2e.yaml) e2e тесты с разверткой _Express.js_-бэкэнда
* ❌ пока нет тестов бэкэнда
* ❌ пока нет CD на Azure/etc