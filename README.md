# Manga reader

ITMO 7th term [Software Testing] course tasks

#### Client

![Client test [unit/component]](https://github.com/doreshnikov/software-testing/workflows/Client%20test%20%5Bunit/component%5D/badge.svg)
![Client test [e2e]](https://github.com/doreshnikov/software-testing/workflows/Client%20test%20%5Be2e%5D/badge.svg)

#### Server

![Ktor server test [mashup]](https://github.com/doreshnikov/software-testing/workflows/Ktor%20server%20test%20%5Bmashup%5D/badge.svg)
![Ktor server test [e2e]](https://github.com/doreshnikov/software-testing/workflows/Ktor%20server%20test%20%5Be2e%5D/badge.svg)

---

## Run

#### Run client

```bash
cd client && npm install
npm run serve
```

#### Run server (express.js version)

```bash
cd server-js && npm install
npm run start
```

#### Run server (ktor version)

```bash
cd server-jvm

# server with in-memory h2 database
./gradlew run
# OR server with postgres database
docker-compose -f docker-compose.yaml up -d postgres manga-reader
```