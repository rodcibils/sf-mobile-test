# Seed API

A minimal Node.js + TypeScript REST API that exposes a single endpoint to generate a seed value, suitable for use with QR code systems.

---

## 🛠 Installation

1. Clone the repository:

```
git clone https://github.com/rodcibils/sf-mobile-test.git
cd sf-mobile-test/api
```

2. Install dependencies:

```
npm install
```

---

## 📖 API Documentation

### GET `/seed`

Returns a seed string and an expiration timestamp. The seed expires always after 5 minutes from `now` at the moment of the API call.

#### ✅ Response: `200 OK`

```
{
  "seed": "d43397d129c3de9e4b6c3974c1c16d1f",
  "expires_at": "1979-11-12T13:10:42.240Z"
}
```

#### Response Schema (OpenAPI 3.0):

```
paths:
  /seed:
    get:
      description: Get a seed that can be used to generate a QR code
      responses:
        '200':
          description: seed generated
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Seed'
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
          description: ISO date-time
          example: '1979-11-12T13:10:42.24Z'
```

---

## 🚀 Running Locally

### Development Mode (with hot reload using ts-node)

```
npm run dev
```

### Production Mode (compiled)

1. Build the TypeScript files:

```
npm run build
```

2. Start the server:

```
npm start
```

The API will be available at:  
➡️ `http://localhost:3000/seed`

---

## 🧪 Testing Locally

### Basic Test with `curl`

```
curl -X GET http://localhost:3000/seed
```

### Pretty Print JSON Response (requires `jq`)

```
curl -s http://localhost:3000/seed | jq
```

If you don't have `jq`, you can install it via:

```
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
│   └── index.ts       # Main entry point
├── dist               # Compiled JS output (ignored by git)
├── package.json
├── tsconfig.json
├── .gitignore
└── README.md
```

---

## 📦 Scripts

| Command         | Description                      |
|-----------------|----------------------------------|
| `npm run dev`   | Run in development mode          |
| `npm run build` | Compile TypeScript to JavaScript |
| `npm start`     | Run compiled app (production)    |

---

## 📄 License

MIT
