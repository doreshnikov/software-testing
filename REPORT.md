## Homework 1

* ✔️ создана папка [`client`](client)
* ✔️ в ней размещено приложение на _Vue.js_
* ✔️ 5 страниц (+рутинг)
* ✔️ unit-тесты и компонентные тесты (`jest`, `@vue/test-utils`)


* ✔️ авторизация (`jwt` + `localStorage`, _Vuex_)
  * есть тесты для проверки авторизации на компонетном уровне
* ✔️ достаточно много наборов тестов
---

Тесты находятся в каталоге [`test`](client/test). Для unit-тестирования и компонентного
тестирования использовался `jest` с `@vue/test-utils`.

* `unit/` &ndash; unit-тесты
    * [`utils.test.js`](client/test/unit/utils.test.js)
      тестирование вспомогательных функций
    * [`store.test.js`](client/test/unit/store.test.js)
      тестирование _Vuex_-хранилищ
* `component/` &ndash; компонентные тесты
    * [`fragments.test.js`](client/test/component/fragments.test.js)
      тестирование общих компонент
    * `pages/`
      тестирование компонент, относящихся к определенным страницам
        * [`home.test.js`](client/test/component/pages/home.test.js)
        * [`login.test.js`](client/test/component/pages/login.test.js)
        * [`title.test.js`](client/test/component/pages/title.test.js)
    

    