# Seed API

A minimal RESTful API built with Node.js and TypeScript that generates time-sensitive QR code seeds. It includes functionality for seed generation, QR terminal display, and validation of active seeds. Suitable for testing mobile apps that integrate with dynamic QR authentication or validation flows.

---

## 🛠 Installation

### 1. Clone the repository

```bash
git clone https://github.com/rodcibils/sf-mobile-test.git
cd sf-mobile-test/api
```

### 2. Install dependencies

```bash
npm install
```

---

## 🚀 Running the API Locally

### Development Mode (with hot reload using ts-node)

```bash
npm run dev
```

### Production Mode (compiled)

```bash
npm run build
npm start
```

The API will be available at:

```
http://localhost:3000
```

---

## 🌐 Accessing the API from a Mobile Device

To test the API from a mobile device on the same network:

1. Find your local IP address (e.g., `192.168.1.100`).
2. Run the server as usual.
3. Access the API from your mobile app using:

```
http://<your-local-ip>:3000
```

Make sure your firewall allows incoming connections on port `3000`.

---

## 📖 API Documentation

### GET `/seed`

Generates a new seed string and expiration timestamp, valid for 5 minutes.

**Response: `200 OK`**

```json
{
  "seed": "d43397d129c3de9e4b6c3974c1c16d1f",
  "expires_at": "2025-05-16T13:10:42.240Z"
}
```

---

### GET `/seed_test`

Generates a new seed (valid for 5 minutes) and displays a QR code in the terminal using ASCII rendering.

**Response: `200 OK`**

Plain text response indicating that the QR code has been rendered in the terminal:

```
Seed generated and QR code displayed in terminal. Expires at: 2025-05-16T13:10:42.240Z
```

---

### POST `/validate`

Validates a previously generated seed.

**Request Body**

```json
{
  "seed": "d43397d129c3de9e4b6c3974c1c16d1f"
}
```

- If the seed matches the most recently generated seed and has not expired:  
  → `200 OK` with body: `Seed is valid`

- If the seed is incorrect or expired:  
  → `400 Bad Request` with body: `Seed is invalid`

---

## 📘 OpenAPI 3.0 Specification

```yaml
openapi: 3.0.0
info:
  title: Seed API
  version: 1.0.0
paths:
  /seed:
    get:
      summary: Generate a new seed
      responses:
        '200':
          description: Seed successfully generated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Seed'
  /seed_test:
    get:
      summary: Generate a new seed and render it as a QR code in the terminal
      responses:
        '200':
          description: Seed generated and QR displayed in terminal
          content:
            text/plain:
              schema:
                type: string
  /validate:
    post:
      summary: Validate a previously generated seed
      requestBody:
        required: true
        content:
          application/json:
            schema:
              type: object
              properties:
                seed:
                  type: string
                  example: d43397d129c3de9e4b6c3974c1c16d1f
      responses:
        '200':
          description: Seed is valid
        '400':
          description: Seed is invalid
components:
  schemas:
    Seed:
      type: object
      properties:
        seed:
          type: string
          example: d43397d129c3de9e4b6c3974c1c16d1f
        expires_at:
          type: string
          format: date-time
          example: '2025-05-16T13:10:42.240Z'
```

---

## 🧪 Testing Locally

You can test all endpoints using `curl` directly from your terminal.

---

### ✅ Test `/seed` — Generate a New Seed

This returns a JSON object with a seed and its expiration time.

```bash
curl -X GET http://localhost:3000/seed
```

**Pretty print with `jq`:**

```bash
curl -s http://localhost:3000/seed | jq
```

---

### ✅ Test `/seed_test` — Generate Seed and Display QR in Terminal

This generates a seed, stores it in memory, and prints the QR code directly in the terminal.

```bash
curl -X GET http://localhost:3000/seed_test
```

You’ll see the QR code rendered as ASCII in your terminal, along with the seed and expiration info.

---

### ✅ Test `/validate` — Validate the Last Generated Seed

1. First, call `/seed` or `/seed_test` to get the latest seed.
2. Then validate it with a POST request:

```bash
curl -X POST http://localhost:3000/validate \
  -H "Content-Type: application/json" \
  -d '{"seed":"<your-seed-here>"}'
```

**Expected outcomes:**

- `200 OK` — if the seed matches the most recent and is not expired
- `400 Bad Request` — if the seed is incorrect or expired

**Example:**

```bash
curl -X POST http://localhost:3000/validate \
  -H "Content-Type: application/json" \
  -d '{"seed":"d43397d129c3de9e4b6c3974c1c16d1f"}'
```

---

### 📦 `jq` Installation (Optional for JSON Formatting)

```bash
# macOS
brew install jq

# Ubuntu/Debian
sudo apt install jq

# Windows (with Chocolatey)
choco install jq
```

---

## 📁 Project Structure

```
.
├── src
│   └── index.ts        # Main entry point for the API
├── dist                # Transpiled JavaScript output
├── package.json
├── tsconfig.json
├── .gitignore
└── README.md
```

---

## 📦 Available NPM Scripts

| Script           | Description                             |
|------------------|-----------------------------------------|
| `npm run dev`    | Starts server in dev mode (ts-node)     |
| `npm run build`  | Compiles TypeScript to `dist/` folder   |
| `npm start`      | Runs the compiled JS (production mode)  |

---

## 📄 License

MIT
