# Manga reader

#### ITMO 7th term [Software Testing] course tasks

![Client unit/component tests](https://github.com/doreshnikov/software-testing/workflows/Client%20unit/component%20testing%20CI/badge.svg?branch=main)
![Client e2e tests](https://github.com/doreshnikov/software-testing/workflows/Client%20e2e%20testing%20CI/badge.svg?branch=main)
![Ktor server tests](https://github.com/doreshnikov/software-testing/workflows/Ktor%20server%20testing%20CI/badge.svg)

## Run

#### Run client

```bash
cd client && npm install
npm run serve
```

---

#### Run server (express.js version)

```bash
cd server-js && npm install
npm run start
```

---

#### Run server (ktor version)

```bash
cd server-jvm

# server with in-memory h2 database
./gradlew run
# OR server with postgres database
docker-compose -f docker-compose.yaml up -d postgres manga-reader
```