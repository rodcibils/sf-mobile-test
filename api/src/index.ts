import express from 'express';
import crypto from 'crypto';
import qrcode from 'qrcode-terminal';

interface SeedResponse {
  readonly seed: string;
  readonly expires_at: string;
 }

const app = express();
const PORT = 3000;

// Middleware to parse JSON bodies
app.use(express.json());

let lastGeneratedSeed: SeedResponse | null = null;

app.get('/seed', (_req, res) => {
  const seed = crypto.randomBytes(16).toString('hex'); // 32-character hex string
  const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString(); // expires in 5 minutes
  const response: SeedResponse = { seed, expires_at: expiresAt };

  res.status(200).json(response);
});

app.get('/seed_test', (_req, res) => {
  const seed = crypto.randomBytes(16).toString('hex');
  const expiresAt = new Date(Date.now() + 5 * 60 * 1000).toISOString();

  // Save to local memory
  lastGeneratedSeed = { seed, expires_at: expiresAt };

  // Generate and display QR code in terminal
  console.log('\nGenerated Seed QR Code:\n');
  qrcode.generate(seed, { small: true });
  console.log(`\nSeed: ${seed}`);
  console.log(`Expires at: ${expiresAt}\n`);

  res.status(200).send(`Seed generated and QR code displayed in terminal. Expires at: ${expiresAt}`);
});

app.post('/validate', (req, res) => {
  const { seed } = req.body;

  console.log(`Received seed: ${seed}`);
  const expiredAt = lastGeneratedSeed?.expires_at ? new Date(lastGeneratedSeed.expires_at).getTime() : undefined;
  if (expiredAt !== undefined && Date.now() < expiredAt && seed === lastGeneratedSeed?.seed) {
    res.status(200).send(`Seed is valid`);
  } else {
    res.status(400).send('Seed is invalid');
  }
});

app.listen(PORT, () => {
  console.log(`Server is running at http://localhost:${PORT}`);
});
